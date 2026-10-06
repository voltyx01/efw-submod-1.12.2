package efw.mixin;

import com.voltyx.mwccf.HeadshotDamageHandler;
import ichttt.mods.firstaid.api.IDamageDistribution;
import ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel;
import ichttt.mods.firstaid.api.enums.EnumPlayerPart;
import ichttt.mods.firstaid.common.damagesystem.distribution.DamageDistribution;
import ichttt.mods.firstaid.common.damagesystem.distribution.StandardDamageDistribution;
import ichttt.mods.firstaid.common.util.CommonUtils;
import java.util.Collections;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "ichttt.mods.firstaid.common.EventHandler", remap = false)
public abstract class FirstAidEventHandlerMixin {

    @Redirect(
        method = "onLivingHurt",
        at = @At(
            value = "INVOKE",
            target = "Lichttt/mods/firstaid/common/damagesystem/distribution/DamageDistribution;handleDamageTaken(Lichttt/mods/firstaid/api/IDamageDistribution;Lichttt/mods/firstaid/api/damagesystem/AbstractPlayerDamageModel;FLnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/DamageSource;ZZ)F"
        ),
        remap = false
    )
    private static float efw$redirectHandleDamageTaken(
            IDamageDistribution damageDistribution,
            AbstractPlayerDamageModel damageModel,
            float damage,
            EntityPlayer player,
            DamageSource source,
            boolean addStat,
            boolean redistributeIfLeft) {

        EntityEquipmentSlot hitSlot = HeadshotDamageHandler.getHitSlot(source);
        if (hitSlot != null) {
            EnumPlayerPart[] possibleParts = CommonUtils.getPartArrayForSlot(hitSlot);
            damageDistribution = new StandardDamageDistribution(
                Collections.singletonList(Pair.of(hitSlot, possibleParts)),
                false,
                true
            );
        }

        try {
            return DamageDistribution.handleDamageTaken(damageDistribution, damageModel, damage, player, source, addStat, redistributeIfLeft);
        } finally {
            HeadshotDamageHandler.clearDamageSource(source);
        }
    }
}
