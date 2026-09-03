package efw.mixin;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@Mixin(InventoryPlayer.class)
public abstract class InventoryPickupMixin {
    static {
        System.out.println("[EFW-MIXIN-LOAD] InventoryPickupMixin class loaded!");
    }

    @Shadow public NonNullList<ItemStack> mainInventory;

    private static final Set<String> PISTOLS = new HashSet<>();
    private static final Set<String> WEAPONS = new HashSet<>();

    static {
        // --- pistols (РєР°Рє Сѓ С‚РµР±СЏ Р±С‹Р»Рѕ) ---
        String[] pistols = {
            "mwc:glock_18c", "mwc:python", "mwc:taurus_raging_hunter", "mwc:sw_500_magnum",
            "mwc:chiappa_rhino", "mwc:aps", "mwc:makarov_pm", "mwc:desert_eagle",
            "mwc:glock_19", "mwc:fiveseven", "mwc:m9a1", "mwc:p226", "mwc:mp443", "mwc:vp70",
            "mwc:m17", "mwc:sccy_cpx_2", "mwc:hk_p12", "mwc:mas_21", "mwc:g2_contender",
            "mwc:m712", "mwc:m1911", "mwc:browning_hi_power"
        };
        for (String id : pistols) PISTOLS.add(id);

       // --- weapons (РєР°Рє Сѓ С‚РµР±СЏ Р±С‹Р»Рѕ) ---
        String[] weapons = {
            "mwc:ak74", "mwc:m4a1", "mwc:ar15", "mwc:m16a1", "mwc:m16a4", "mwc:m38_dmr",
            "mwc:acr", "mwc:ngsw_r", "mwc:sig_mcx", "mwc:aac_honey_badger", "mwc:ak47",
            "mwc:ak15", "mwc:malyuk", "mwc:ak12_kal", "mwc:zhmash_ak12", "mwc:ak101",
            "mwc:kbp_9a91", "mwc:k2c1", "mwc:scar_h_cqc", "mwc:scar_l", "mwc:sig556",
            "mwc:cz805_bren", "mwc:arx160", "mwc:type20", "mwc:famas_f1g36c", "mwc:g11",
            "mwc:f2000", "mwc:steyr_aug_a1", "mwc:stg44", "mwc:m1_garand", "mwc:m1941_jonson_rifle",
            "mwc:g43_gewehr", "mwc:m1_carbine", "mwc:m1873", "mwc:mares_leg", "mwc:ar10_super_sass",
            "mwc:beowulf_50_cal", "mwc:m110_sass", "mwc:zbroyar_z10", "mwc:hk_417", "mwc:mk14_ebr",
            "mwc:fnfal", "mwc:g3", "mwc:springfield", "mwc:m82_barrett", "mwc:m40a6",
            "mwc:svd_dragunov", "mwc:vss_vintorez", "mwc:as50", "mwc:m200_intervention",
            "mwc:dsr1", "mwc:l96a1", "mwc:remington_700", "mwc:sv98", "mwc:krag_jorgensen",
            "mwc:kar98k", "mwc:mp5a5", "mwc:mp7", "mwc:p90", "mwc:mac10", "mwc:kriss_vector",
            "mwc:ump_45", "mwc:scorpion_evo_a1", "mwc:sig_mpx", "mwc:fmg9", "mwc:uzi",
            "mwc:s7_10_tricun", "mwc:apc9", "mwc:pp91_kedr", "mwc:origin12", "mwc:saiga12",
            "mwc:spas_12", "mwc:m1014", "mwc:supernova", "mwc:remington870", "mwc:ks23",
            "mwc:m1897", "mwc:hs12", "mwc:mp43e", "mwc:browning_auto_5", "mwc:ssg_08", "mwc:m1928_thompson"
            
        };
        for (String id : weapons) WEAPONS.add(id);
    }

    @Inject(method = "addItemStackToInventory", at = @At("HEAD"), cancellable = true)
    private void onPickup(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack == null || stack.isEmpty()) return;

        ResourceLocation id = stack.getItem().getRegistryName();
        if (id == null) return;
        String idStr = id.toString();

        int targetSlot = -1;

        // РћРїСЂРµРґРµР»СЏРµРј РїСЂР°РІРёР»СЊРЅС‹Р№ СЃР»РѕС‚
        if (PISTOLS.contains(idStr)) {
            targetSlot = 1; // РїРёСЃС‚РѕР»РµС‚С‹ в†’ СЃР»РѕС‚ 1
        } else if (WEAPONS.contains(idStr)) {
            targetSlot = 0; // РѕСЂСѓР¶РёРµ в†’ СЃР»РѕС‚ 0
        } else {
            return; // РЅРµ РЅР°С€ РїСЂРµРґРјРµС‚ вЂ” РѕСЃС‚Р°РІР»СЏРµРј vanilla РїРѕРІРµРґРµРЅРёРµ
        }

        // --- 1. Р•СЃР»Рё РїСЂР°РІРёР»СЊРЅС‹Р№ СЃР»РѕС‚ РїСѓСЃС‚ ---
        if (mainInventory.get(targetSlot).isEmpty()) {
            mainInventory.set(targetSlot, stack.copy());
            stack.setCount(0); // СѓР±РёСЂР°РµРј РёР· РјРёСЂР°
            cir.setReturnValue(true);
            return;
        }

        // --- 2. Р•СЃР»Рё Р·Р°РЅСЏС‚ вЂ” РёС‰РµРј РјРµСЃС‚Рѕ РІ РѕСЃРЅРѕРІРЅРѕРј РёРЅРІРµРЅС‚Р°СЂРµ (9..35) ---
        for (int i = 9; i <= 35; i++) {
            if (mainInventory.get(i).isEmpty()) {
                mainInventory.set(i, stack.copy());
                stack.setCount(0);
                cir.setReturnValue(true);
                return;
            }
        }

        // --- 3. Р•СЃР»Рё РЅРµС‚ РјРµСЃС‚Р° РІ РёРЅРІРµРЅС‚Р°СЂРµ вЂ” РЅРµ РїРѕРґР±РёСЂР°РµРј ---
        cir.setReturnValue(false);
    }
}
