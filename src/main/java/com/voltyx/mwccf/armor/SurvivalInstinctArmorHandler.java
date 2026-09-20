package com.voltyx.mwccf.armor;

import com.voltyx.mwccf.mcore.ItemCustomArmor;
import com.voltyx.mwccf.mcore.MCoreItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SurvivalInstinctArmorHandler {

    private static final String EXO_DASH_CD_TAG = "mwccf_exo_dash_cd";

    public static int getExoDashCooldown(EntityPlayer player) {
        return player.getEntityData().getInteger(EXO_DASH_CD_TAG);
    }

    public static void setExoDashCooldown(EntityPlayer player, int ticks) {
        player.getEntityData().setInteger(EXO_DASH_CD_TAG, ticks);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase living = event.getEntityLiving();
        if (living == null || living.world == null) return;

        // Decrement cooldown if player
        if (living instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) living;
            int cd = getExoDashCooldown(player);
            if (cd > 0) {
                setExoDashCooldown(player, cd - 1);
            }
        }

        ItemStack helm = living.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        ItemStack chest = living.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        ItemStack legs = living.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        ItemStack boots = living.getItemStackFromSlot(EntityEquipmentSlot.FEET);

        Item helmItem = helm.isEmpty() ? null : helm.getItem();
        Item chestItem = chest.isEmpty() ? null : chest.getItem();
        Item legsItem = legs.isEmpty() ? null : legs.getItem();
        Item bootsItem = boots.isEmpty() ? null : boots.getItem();

        // 1. Gas Mask (Helmet only)
        if (isGasMask(helmItem)) {
            living.removePotionEffect(MobEffects.POISON);
            living.removePotionEffect(MobEffects.WITHER);
        }

        // 2. Night Vision Helmets (NVG, Green Hunter, Desert Hunter, Black Hunter, Heavy Exo)
        boolean isNVG = isNVGHelmet(helmItem);
        boolean hasNVGActive = isNVG && ItemCustomArmor.isNVGActive(helm);
        boolean hadNVG = living.getEntityData().getBoolean("SI_HadNVG");

        if (hasNVGActive) {
            if (!living.world.isRemote) {
                // Drain battery charge: 20 units every 20 ticks (1 sec)
                if (living.ticksExisted % 20 == 0) {
                    net.minecraft.nbt.NBTTagCompound tag = helm.getTagCompound();
                    int charge = tag != null && tag.hasKey("battery_charge") ? tag.getInteger("battery_charge") : 0;
                    if (charge > 0) {
                        charge = Math.max(0, charge - 20);
                        tag.setInteger("battery_charge", charge);
                        if (charge <= 0) {
                            tag.setBoolean("nv_active", false);
                            hasNVGActive = false;
                            living.removePotionEffect(MobEffects.NIGHT_VISION);
                            living.world.playSound(null, living.posX, living.posY, living.posZ,
                                    com.voltyx.mwccf.ModSounds.NVG_TOGGLE != null ? com.voltyx.mwccf.ModSounds.NVG_TOGGLE : net.minecraft.init.SoundEvents.BLOCK_LEVER_CLICK,
                                    net.minecraft.util.SoundCategory.PLAYERS, 0.8F, 1.0F);
                            if (living instanceof net.minecraft.entity.player.EntityPlayerMP) {
                                net.minecraft.entity.player.EntityPlayerMP mp = (net.minecraft.entity.player.EntityPlayerMP) living;
                                mp.connection.sendPacket(
                                        new net.minecraft.network.play.server.SPacketChat(
                                                new net.minecraft.util.text.TextComponentTranslation("tooltip.mwccf.walkie_talkie.no_battery"),
                                                net.minecraft.util.text.ChatType.GAME_INFO));
                                mp.connection.sendPacket(
                                        new net.minecraft.network.play.server.SPacketEntityEquipment(living.getEntityId(), EntityEquipmentSlot.HEAD, helm));
                            }
                            if (living.world instanceof net.minecraft.world.WorldServer) {
                                ((net.minecraft.world.WorldServer) living.world).getEntityTracker().sendToTracking(living,
                                        new net.minecraft.network.play.server.SPacketEntityEquipment(living.getEntityId(), EntityEquipmentSlot.HEAD, helm));
                            }
                        }
                    } else {
                        if (tag != null) {
                            tag.setBoolean("nv_active", false);
                        }
                        hasNVGActive = false;
                        living.removePotionEffect(MobEffects.NIGHT_VISION);
                    }
                }
                if (hasNVGActive) {
                    living.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 220, 0, true, false));
                }
            }
            if (hasNVGActive) {
                living.getEntityData().setBoolean("SI_HadNVG", true);
            } else if (hadNVG) {
                living.removePotionEffect(MobEffects.NIGHT_VISION);
                living.getEntityData().removeTag("SI_HadNVG");
            }
        } else if (hadNVG) {
            living.removePotionEffect(MobEffects.NIGHT_VISION);
            living.getEntityData().removeTag("SI_HadNVG");
        }

        // 3. Hazmat Suit (Full set)
        if (isWearingFullHazmat(helmItem, chestItem, legsItem, bootsItem)) {
            living.removePotionEffect(MobEffects.POISON);
            living.removePotionEffect(MobEffects.WITHER);
            living.removePotionEffect(MobEffects.SLOWNESS);
            living.removePotionEffect(MobEffects.MINING_FATIGUE);
            living.removePotionEffect(MobEffects.NAUSEA);
            living.removePotionEffect(MobEffects.BLINDNESS);
            living.removePotionEffect(MobEffects.HUNGER);
            living.removePotionEffect(MobEffects.WEAKNESS);
            living.removePotionEffect(MobEffects.LEVITATION);
            living.removePotionEffect(MobEffects.GLOWING);
        }

        // 4. Firefighter Suit (Full set)
        if (isWearingFullFirefighter(helmItem, chestItem, legsItem, bootsItem)) {
            if (living.isBurning()) {
                living.extinguish();
            }
        }

        // 5. Ghillie Suit (Full set)
        if (isWearingFullGhillie(helmItem, chestItem, legsItem, bootsItem)) {
            if (living.isSneaking()) {
                if (!living.world.isRemote) {
                    living.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 30, 0, true, false));
                }
            }
        }

        // 6. Juggernaut Armor (Full set)
        if (isWearingFullJuggernaut(helmItem, chestItem, legsItem, bootsItem)) {
            if (!living.world.isRemote) {
                living.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 30, 0, true, false));
                living.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 30, 0, true, false));
            }
        }

        // 7. Standard Exosuit (Full set)
        if (isWearingStandardExo(helmItem, chestItem, legsItem, bootsItem)) {
            if (!living.world.isRemote) {
                living.addPotionEffect(new PotionEffect(MobEffects.SPEED, 30, 0, true, false));
                living.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 30, 0, true, false));
                living.addPotionEffect(new PotionEffect(MobEffects.HASTE, 30, 0, true, false));
                if (living.onGround && living.isSneaking()) {
                    living.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 15, 3, true, false));
                }
            }
        }

        // 8. Heavy Exosuit (Full set)
        if (isWearingHeavyExo(helmItem, chestItem, legsItem, bootsItem)) {
            living.removePotionEffect(MobEffects.BLINDNESS);
            if (!living.world.isRemote) {
                living.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 30, 1, true, false));
                living.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 30, 1, true, false));
                if (living.onGround && living.isSneaking()) {
                    living.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 15, 3, true, false));
                }
            }
        }
    }

    public static boolean isGasMask(Item h) {
        if (h == null || h.getRegistryName() == null) return false;
        return h.getRegistryName().getPath().contains("gas_mask");
    }

    public static boolean isNVGHelmet(Item h) {
        if (h == null || h.getRegistryName() == null) return false;
        String name = h.getRegistryName().getPath();
        return name.contains("night_vision") || name.contains("hunter_helmet") || name.startsWith("exo_heavy");
    }

    public static boolean isWearingFullHazmat(Item h, Item c, Item l, Item b) {
        if (h == null || c == null || l == null || b == null) return false;
        String hn = h.getRegistryName().getPath();
        String cn = c.getRegistryName().getPath();
        String ln = l.getRegistryName().getPath();
        String bn = b.getRegistryName().getPath();
        return hn.contains("hazmat") && cn.contains("hazmat") && ln.contains("hazmat") && bn.contains("hazmat");
    }

    public static boolean isWearingFullFirefighter(Item h, Item c, Item l, Item b) {
        if (h == null || c == null || l == null || b == null) return false;
        String hn = h.getRegistryName().getPath();
        String cn = c.getRegistryName().getPath();
        String ln = l.getRegistryName().getPath();
        String bn = b.getRegistryName().getPath();
        return hn.contains("fire_fighter") && cn.contains("fire_fighter") && ln.contains("fire_fighter") && bn.contains("fire_fighter");
    }

    public static boolean isWearingFullGhillie(Item h, Item c, Item l, Item b) {
        if (h == null || c == null || l == null || b == null) return false;
        String hn = h.getRegistryName().getPath();
        String cn = c.getRegistryName().getPath();
        String ln = l.getRegistryName().getPath();
        String bn = b.getRegistryName().getPath();
        return (hn.contains("ghillie") || hn.contains("guillie")) &&
               (cn.contains("ghillie") || cn.contains("guillie")) &&
               (ln.contains("ghillie") || ln.contains("guillie")) &&
               (bn.contains("ghillie") || bn.contains("guillie"));
    }

    public static boolean isWearingFullJuggernaut(Item h, Item c, Item l, Item b) {
        if (h == null || c == null || l == null || b == null) return false;
        String hn = h.getRegistryName().getPath();
        String cn = c.getRegistryName().getPath();
        String ln = l.getRegistryName().getPath();
        String bn = b.getRegistryName().getPath();
        return hn.contains("juggernaut") && cn.contains("juggernaut") && ln.contains("juggernaut") && bn.contains("juggernaut");
    }

    public static boolean isWearingStandardExo(Item h, Item c, Item l, Item b) {
        if (h == null || c == null || l == null || b == null) return false;
        String hn = h.getRegistryName().getPath();
        String cn = c.getRegistryName().getPath();
        String ln = l.getRegistryName().getPath();
        String bn = b.getRegistryName().getPath();
        return hn.equals("exo_helmet") && cn.equals("exo_chestplate") && ln.equals("exo_leggings") && bn.equals("exo_boots");
    }

    public static boolean isWearingHeavyExo(Item h, Item c, Item l, Item b) {
        if (h == null || c == null || l == null || b == null) return false;
        String hn = h.getRegistryName().getPath();
        String cn = c.getRegistryName().getPath();
        String ln = l.getRegistryName().getPath();
        String bn = b.getRegistryName().getPath();
        return hn.startsWith("exo_heavy") && cn.startsWith("exo_heavy") && ln.startsWith("exo_heavy") && bn.startsWith("exo_heavy");
    }

    public static boolean isWearingFullExo(EntityLivingBase living) {
        ItemStack helm = living.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        ItemStack chest = living.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        ItemStack legs = living.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        ItemStack boots = living.getItemStackFromSlot(EntityEquipmentSlot.FEET);

        Item h = helm.isEmpty() ? null : helm.getItem();
        Item c = chest.isEmpty() ? null : chest.getItem();
        Item l = legs.isEmpty() ? null : legs.getItem();
        Item b = boots.isEmpty() ? null : boots.getItem();

        return isWearingStandardExo(h, c, l, b) || isWearingHeavyExo(h, c, l, b);
    }
}
