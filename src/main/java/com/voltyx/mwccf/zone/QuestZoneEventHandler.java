package com.voltyx.mwccf.zone;

import com.voltyx.mwccf.MwccfMod;
import com.voltyx.mwccf.zone.network.PacketSyncZones;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuestZoneEventHandler {

    public static void syncZonesToPlayer(EntityPlayerMP player) {
        QuestZoneData data = QuestZoneData.get(player.world);
        if (data != null) {
            MwccfMod.PACKET_HANDLER.sendTo(new PacketSyncZones(new ArrayList<>(data.getZones())), player);
        }
    }

    public static void syncZonesToAll(World world) {
        QuestZoneData data = QuestZoneData.get(world);
        if (data != null && !world.isRemote) {
            MwccfMod.PACKET_HANDLER.sendToAll(new PacketSyncZones(new ArrayList<>(data.getZones())));
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            syncZonesToPlayer((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (player != null && player.capabilities.isCreativeMode) {
            // Креативщикам разрешено строить и ломать в любой зоне
            return;
        }

        World world = event.getWorld();
        if (world.isRemote) return;

        QuestZoneData data = QuestZoneData.get(world);
        if (data != null && data.isProtected(world.provider.getDimension(), event.getPos())) {
            event.setCanceled(true);
            if (player != null) {
                player.sendStatusMessage(new TextComponentString(TextFormatting.RED + "Зона защищена от разрушений!"), true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockEvent.PlaceEvent event) {
        EntityPlayer player = event.getPlayer();
        if (player != null && player.capabilities.isCreativeMode) {
            return;
        }

        World world = event.getWorld();
        if (world.isRemote) return;

        QuestZoneData data = QuestZoneData.get(world);
        if (data != null && data.isProtected(world.provider.getDimension(), event.getPos())) {
            event.setCanceled(true);
            if (player instanceof EntityPlayerMP) {
                ((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
            }
            if (player != null) {
                player.sendStatusMessage(new TextComponentString(TextFormatting.RED + "Здесь нельзя строить!"), true);
            }
        }
    }

    @SubscribeEvent
    public void onRightClickBlock(net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        if (world.isRemote) return;

        BlockPos pos = event.getPos();
        net.minecraft.block.state.IBlockState state = world.getBlockState(pos);
        net.minecraft.block.Block block = state.getBlock();

        // Проверяем, является ли блок дверью (BlockDoor, BlockTrapDoor, BlockFenceGate)
        boolean isDoor = block instanceof net.minecraft.block.BlockDoor ||
                         block instanceof net.minecraft.block.BlockTrapDoor ||
                         block instanceof net.minecraft.block.BlockFenceGate ||
                         block.getClass().getName().toLowerCase().contains("door");

        if (isDoor) {
            int dim = world.provider.getDimension();
            QuestZoneData data = QuestZoneData.get(world);
            if (data == null) return;

            // Если дверь - двухблочная (BlockDoor), нижняя половина содержит свойство OPEN
            BlockPos lowerPos = pos;
            if (block instanceof net.minecraft.block.BlockDoor) {
                if (state.getValue(net.minecraft.block.BlockDoor.HALF) == net.minecraft.block.BlockDoor.EnumDoorHalf.UPPER) {
                    lowerPos = pos.down();
                }
            }

            final BlockPos checkPos = lowerPos;
            QuestZone zone = data.findZoneAt(dim, pos);
            if (zone == null) {
                zone = data.findZoneAt(dim, checkPos);
            }
            if (zone == null) {
                // Если дверь стоит на границе стены или впритык к комнате
                zone = data.findZoneTouching(dim, checkPos, 1);
            }

            if (zone != null) {
                final QuestZone targetZone = zone;
                EntityPlayer player = event.getEntityPlayer();

                // Проверяем текущее состояние двери прямо в момент клика ДО изменения
                net.minecraft.block.state.IBlockState currentState = world.getBlockState(checkPos);
                final boolean previouslyOpen = isDoorOpen(currentState);

                if (world instanceof net.minecraft.world.WorldServer) {
                    ((net.minecraft.world.WorldServer) world).addScheduledTask(() -> {
                        net.minecraft.block.state.IBlockState nextState = world.getBlockState(checkPos);
                        boolean isNowOpen = isDoorOpen(nextState);

                        // Проверяем, заперта ли дверь замком мода Locks
                        boolean isLocked = LocksCompat.isDoorLocked(world, pos, checkPos);

                        // Зона защиты снимается ТОЛЬКО если дверь РЕАЛЬНО открылась:
                        // 1. Дверь сейчас открыта (isNowOpen == true)
                        // 2. До этого она была закрыта (!previouslyOpen)
                        // 3. На ней нет запертого замка (!isLocked)
                        if (!isLocked && !previouslyOpen && isNowOpen) {
                            data.removeZone(targetZone.getId());
                            syncZonesToAll(world);
                            if (player != null) {
                                player.sendMessage(new TextComponentString(
                                        TextFormatting.GOLD + "[Зоны] " +
                                        TextFormatting.GREEN + "Дверь открыта! Защита с комнаты \"" + targetZone.getName() + "\" снята."
                                ));
                            }
                        }
                    });
                }
            }
        }
    }

    private static boolean isDoorOpen(net.minecraft.block.state.IBlockState state) {
        if (state == null) return false;
        if (state.getBlock() instanceof net.minecraft.block.BlockDoor) {
            return state.getValue(net.minecraft.block.BlockDoor.OPEN);
        } else if (state.getBlock() instanceof net.minecraft.block.BlockTrapDoor) {
            return state.getValue(net.minecraft.block.BlockTrapDoor.OPEN);
        } else if (state.getBlock() instanceof net.minecraft.block.BlockFenceGate) {
            return state.getValue(net.minecraft.block.BlockFenceGate.OPEN);
        }
        for (net.minecraft.block.properties.IProperty<?> prop : state.getPropertyKeys()) {
            if ("open".equalsIgnoreCase(prop.getName()) && prop.getValueClass() == Boolean.class) {
                return (Boolean) state.getValue(prop);
            }
        }
        return false;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onExplosionDetonate(ExplosionEvent.Detonate event) {
        World world = event.getWorld();
        if (world.isRemote) return;

        QuestZoneData data = QuestZoneData.get(world);
        if (data == null) return;

        int dim = world.provider.getDimension();
        List<BlockPos> affected = event.getAffectedBlocks();
        Iterator<BlockPos> it = affected.iterator();
        while (it.hasNext()) {
            BlockPos pos = it.next();
            if (data.isProtected(dim, pos)) {
                it.remove(); // Спасаем блоки комнаты от любого взрыва
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMobGriefing(net.minecraftforge.event.entity.EntityMobGriefingEvent event) {
        if (event.getEntity() != null) {
            World world = event.getEntity().world;
            if (!world.isRemote) {
                QuestZoneData data = QuestZoneData.get(world);
                if (data != null && data.isProtected(world.provider.getDimension(), event.getEntity().getPosition())) {
                    event.setResult(net.minecraftforge.fml.common.eventhandler.Event.Result.DENY);
                }
            }
        }
    }
}
