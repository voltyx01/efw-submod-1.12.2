package com.voltyx.mwccf.fireweapon;

import com.voltyx.mwccf.fireweapon.client.FireWeaponSparkManager;
import com.voltyx.mwccf.fireweapon.smolder.SmolderingManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FireWeaponCombatHandler {

    /**
     * Extra spark burst and sound on weapon strike.
     * Sparks fly away from the impact point in the opposite direction of attack.
     */
    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player == null) return;

        ItemStack stack = player.getHeldItemMainhand();
        if (FireWeaponHelper.isIgnited(stack)) {
            if (player.world.isRemote && event.getTarget() != null) {
                double dx = event.getTarget().posX - player.posX;
                double dy = event.getTarget().posY + event.getTarget().height * 0.5D - (player.posY + player.getEyeHeight());
                double dz = event.getTarget().posZ - player.posZ;
                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

                if (dist > 0.01D) {
                    float dirX = (float) (dx / dist);
                    float dirY = (float) (dy / dist);
                    float dirZ = (float) (dz / dist);

                    Vec3d impactPos = event.getTarget().getPositionVector().add(0.0D, event.getTarget().height * 0.5D, 0.0D);

                    // SOAKED mode: bigger, more spectacular burst
                    boolean soaked = FireWeaponHelper.isSoaked(stack);
                    int count = soaked ? 70 : 45;
                    float speed = soaked ? 3.5F : 2.5F;
                    FireWeaponSparkManager.spawnImpactBurst(impactPos, dirX, dirY, dirZ, count, speed);
                }
            }
            player.world.playSound(null, player.posX, player.posY, player.posZ,
                    SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 0.45F, 1.6F);
        }
    }

    /**
    * Adds the configured fire damage and applies the per-hit smolder to the mob.
     * "при попадании по мобу, на нем должны появится эти черные обгорающие пятна которые будут тлеть первые несколько сек,
     * и его будет дамажить в это время. реально поджигать моба не нужно."
     * "при этом урон нанесенный этим оружием должен считаться как за typedamage fire. базово поджигание должно добавлять +5 дамага."
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource() == null) return;
        if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
            EntityLivingBase attacker = (EntityLivingBase) event.getSource().getTrueSource();
            ItemStack weapon = attacker.getHeldItemMainhand();

            if (FireWeaponHelper.isIgnited(weapon)) {
                // Mode-dependent fire damage bonus and smoldering DoT
                event.setAmount(event.getAmount() + FireWeaponHelper.getFireDamageBonus(weapon));
                event.getSource().setFireDamage();

                EntityLivingBase victim = event.getEntityLiving();
                if (victim != null && !victim.world.isRemote) {
                    double sideX = attacker.posX - victim.posX;
                    double sideZ = attacker.posZ - victim.posZ;
                    double bodyRotation = Math.toRadians(180.0D - victim.renderYawOffset);
                    double localX = Math.cos(bodyRotation) * sideX - Math.sin(bodyRotation) * sideZ;
                    double localZ = Math.sin(bodyRotation) * sideX + Math.cos(bodyRotation) * sideZ;
                    double angle = Math.atan2(localZ, localX) / (Math.PI * 2.0D) + 0.5D;
                    float u = 0.25F + (float) (angle - Math.floor(angle)) * 0.375F;
                    float bodyHeight = 0.2F + victim.getRNG().nextFloat() * 0.6F;
                    float v = 0.25F + (1.0F - bodyHeight) * 0.25F;
                    boolean soaked = FireWeaponHelper.isSoaked(weapon);
                    SmolderingManager.applySmolder(victim, FireWeaponHelper.getSmolderTicks(weapon),
                            FireWeaponHelper.getSmolderDamage(weapon), u, v, soaked, victim.getRNG().nextInt());
                }
            }
        }
    }

    /**
     * Stylish tooltips indicating wrapped cloth, fire damage, and remaining burn time.
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;

        if (stack.getItem() instanceof efw.item.ItemCloth) {
            event.getToolTip().add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.fireweapon.cloth_hint"));
            if (net.minecraft.client.gui.GuiScreen.isShiftKeyDown()) {
                event.getToolTip().add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.fireweapon.cloth_instructions"));
            }
            return;
        }

        if (FireWeaponHelper.isIgnited(stack)) {
            boolean soaked = FireWeaponHelper.isSoaked(stack);
            int mode = FireWeaponHelper.getBurnMode(stack);
            int sec = FireWeaponHelper.getRemainingSeconds(stack);
            int min = sec / 60;
            int remSec = sec % 60;
            float damage = FireWeaponHelper.getFireDamageBonus(stack);

            String statusKey = soaked ? "tooltip.mwccf.fireweapon.soaked" : "tooltip.mwccf.fireweapon.burning";
            event.getToolTip().add(net.minecraft.client.resources.I18n.format(statusKey, Math.round(damage)));

            if (mode == 1) {
                // CLOTH mode: show cloth % bar
                int pct = FireWeaponHelper.getClothPct(stack);
                int totalBars = 10;
                int filledBars = Math.max(1, (int) Math.ceil(pct / 10.0F));
                StringBuilder bar = new StringBuilder(soaked ? "§c" : "§e");
                for (int i = 0; i < totalBars; i++) {
                    if (i == filledBars) bar.append("§8");
                    bar.append("■");
                }
                bar.append(" ").append(net.minecraft.client.resources.I18n.format("tooltip.mwccf.fireweapon.cloth_percent", pct));
                event.getToolTip().add(bar.toString());
            } else if (mode == 2) {
                // SOAKED mode: show countdown timer bar
                float prog = FireWeaponHelper.getBurnProgress(stack);
                int totalBars = 10;
                int filledBars = Math.max(1, (int) Math.ceil((1.0F - prog) * totalBars));
                StringBuilder bar = new StringBuilder("§c");
                for (int i = 0; i < totalBars; i++) {
                    if (i == filledBars) bar.append("§8");
                    bar.append("■");
                }
                bar.append(" ").append(net.minecraft.client.resources.I18n.format("tooltip.mwccf.fireweapon.timer", min, remSec));
                event.getToolTip().add(bar.toString());
            }

        } else if (FireWeaponHelper.isInCharPhase(stack)) {
            event.getToolTip().add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.fireweapon.charred"));
        } else if (FireWeaponHelper.isSoakedReady(stack)) {
            event.getToolTip().add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.fireweapon.soaked_ready"));
        } else if (FireWeaponHelper.isWrapped(stack)) {
            int pct = FireWeaponHelper.getClothPct(stack);
            if (pct < 100) {
                event.getToolTip().add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.fireweapon.wrapped_percent", pct));
            } else {
                event.getToolTip().add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.fireweapon.wrapped"));
            }
        }

        if (FireWeaponHelper.isWrapped(stack) || FireWeaponHelper.isInCharPhase(stack)
                || FireWeaponHelper.isSoakedReady(stack) || FireWeaponHelper.isIgnited(stack)) {
            addIgnitionTooltip(event.getToolTip());
        }
    }

    @SideOnly(Side.CLIENT)
    private static void addIgnitionTooltip(java.util.List<String> tooltip) {
        tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.fireweapon.hint"));
        if (net.minecraft.client.gui.GuiScreen.isShiftKeyDown()) {
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.fireweapon.instructions"));
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.fireweapon.enhancers"));
        }
    }
}
