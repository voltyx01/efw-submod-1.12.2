package com.voltyx.mwccf.block.lamp;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityFlickeringLamp extends TileEntity implements ITickable {

    private float brightness = 1.0F;
    private float prevBrightness = 1.0F;
    private int emittedLight = 15;
    private int lastUpdatedLight = 15;
    private long posSeed = 0L;
    private boolean seedInitialized = false;
    private boolean initialLightChecked = false;

    @Override
    public void update() {
        if (!seedInitialized && pos != null) {
            posSeed = (long) pos.getX() * 3129871L ^ (long) pos.getY() * 116129L ^ (long) pos.getZ();
            seedInitialized = true;
        }

        prevBrightness = brightness;

        boolean isPowered = false;
        if (this.world != null && this.pos != null) {
            IBlockState state = this.world.getBlockState(this.pos);
            if (state.getBlock() instanceof BlockFlickeringLamp) {
                isPowered = state.getValue(BlockFlickeringLamp.POWERED).booleanValue();
            }
        }

        int targetLight;
        if (isPowered) {
            brightness = 0.0F;
            targetLight = 0;
        } else {
            LampFlickerType type = getFlickerType();
            long worldTime = this.world != null ? this.world.getTotalWorldTime() : 0L;
            long localTime = worldTime + (posSeed & 0xFFFFL);

            switch (type) {
                case SOFT: {
                    // 7-second cycle (140 ticks)
                    long cycle = localTime % 140;
                    if (cycle >= 125 && cycle <= 135) {
                        // Periodic voltage dip: smoothly dips light level down to 8 and back
                        double progress = (cycle - 125) / 10.0;
                        double dip = Math.sin(progress * Math.PI) * 0.45;
                        brightness = (float) (0.95 - dip);
                        targetLight = MathHelper.clamp(Math.round(brightness * 15.0F), 0, 15);
                    } else {
                        // Normal steady operation: room light stays locked at level 15 (zero chunk updates = zero lag)
                        // Filament texture has subtle 50/60Hz electrical shimmer
                        double tSec = localTime * 0.05;
                        double hum = Math.sin(tSec * 22.0) * 0.03 + Math.sin(tSec * 47.0 + 1.2) * 0.02;
                        brightness = MathHelper.clamp((float) (0.95 + hum), 0.85F, 1.0F);
                        targetLight = 15;
                    }
                    break;
                }

                case BROKEN: {
                    // 9-second cycle (180 ticks)
                    long cycle = localTime % 180;
                    if (cycle < 80) {
                        // Phase 1 (4.0s): Steady buzzing, level 13 (zero chunk updates)
                        double shake = Math.sin(localTime * 1.7) * 0.05;
                        brightness = MathHelper.clamp((float) (0.88 + shake), 0.75F, 1.0F);
                        targetLight = 13;
                    } else if (cycle < 120) {
                        // Phase 2 (2.0s): Starter chatter with distinct, synchronized light steps
                        long sub = cycle - 80; // 0..39
                        if (sub < 8) {
                            brightness = 0.15F; targetLight = 2;
                        } else if (sub < 16) {
                            brightness = 0.92F; targetLight = 14;
                        } else if (sub < 24) {
                            brightness = 0.04F; targetLight = 0;
                        } else if (sub < 32) {
                            brightness = 0.75F; targetLight = 11;
                        } else {
                            brightness = 0.08F; targetLight = 1;
                        }
                    } else if (cycle < 155) {
                        // Phase 3 (1.75s): Near-total blackout (faint glowing ember in filament, room dark)
                        brightness = 0.05F;
                        targetLight = 0;
                    } else {
                        // Phase 4 (1.25s): Arc ignition strikes
                        long sub = cycle - 155; // 0..24
                        if (sub >= 2 && sub <= 5) {
                            brightness = 0.98F; targetLight = 15;
                        } else if (sub >= 10 && sub <= 13) {
                            brightness = 0.95F; targetLight = 14;
                        } else if (sub >= 18 && sub <= 21) {
                            brightness = 0.98F; targetLight = 15;
                        } else if (sub >= 23) {
                            brightness = 0.88F; targetLight = 13;
                        } else {
                            brightness = 0.02F; targetLight = 0;
                        }
                    }
                    break;
                }

                case DYING: {
                    // 11-second cycle (220 ticks)
                    long cycle = localTime % 220;
                    if (cycle < 195) {
                        // Phase 1 (9.75s): Completely dark (zero chunk updates)
                        brightness = 0.0F;
                        targetLight = 0;
                    } else {
                        // Phase 2 (1.25s): Sudden electrical spasm bursts
                        long sub = cycle - 195; // 0..24
                        if (sub >= 2 && sub <= 6) {
                            brightness = 0.95F; targetLight = 14;
                        } else if (sub >= 11 && sub <= 16) {
                            brightness = 0.98F; targetLight = 15;
                        } else if (sub >= 19 && sub <= 22) {
                            brightness = 0.82F; targetLight = 12;
                        } else {
                            brightness = 0.0F; targetLight = 0;
                        }
                    }
                    break;
                }

                default: {
                    brightness = 1.0F;
                    targetLight = 15;
                    break;
                }
            }
        }

        // Apply vanilla block light update immediately when targetLight changes
        // Because updates only occur on actual state shifts (a few times per cycle),
        // the chunk compile queue never backs up: ZERO lag and PERFECT sync between chests/beds and blocks!
        if (this.world != null && this.pos != null && this.world.isBlockLoaded(this.pos)) {
            if (!initialLightChecked) {
                initialLightChecked = true;
                this.emittedLight = targetLight;
                this.lastUpdatedLight = targetLight;
                this.world.checkLightFor(EnumSkyBlock.BLOCK, this.pos);
            } else if (targetLight != lastUpdatedLight) {
                this.emittedLight = targetLight;
                this.lastUpdatedLight = targetLight;
                this.world.checkLightFor(EnumSkyBlock.BLOCK, this.pos);
            }
        }
    }

    public LampFlickerType getFlickerType() {
        if (this.world != null && this.pos != null) {
            Block block = this.world.getBlockState(this.pos).getBlock();
            if (block instanceof BlockFlickeringLamp) {
                return ((BlockFlickeringLamp) block).getFlickerType();
            }
        }
        return LampFlickerType.SOFT;
    }

    public float getInterpolatedBrightness(float partialTicks) {
        return this.prevBrightness + (this.brightness - this.prevBrightness) * partialTicks;
    }

    public int getEmittedLight() {
        return this.emittedLight;
    }

    public long getPosSeed() {
        return this.posSeed;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setFloat("Brightness", this.brightness);
        compound.setInteger("EmittedLight", this.emittedLight);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("Brightness")) {
            this.brightness = compound.getFloat("Brightness");
            this.prevBrightness = this.brightness;
        }
        if (compound.hasKey("EmittedLight")) {
            this.emittedLight = compound.getInteger("EmittedLight");
            this.lastUpdatedLight = this.emittedLight;
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.pos, this.pos.add(1, 1, 1));
    }
}
