package efw.item;

import efw.init.EfwModSounds;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemMedKit extends Item {

    public ItemMedKit() {
        super();
        setMaxStackSize(4);
        setTranslationKey("mcore.med_kit");
        setRegistryName("mwccf", "med_kit");
        setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        // Only heal if health is not full
        if (player.getHealth() < player.getMaxHealth()) {
            world.playSound(null, player.posX, player.posY, player.posZ, EfwModSounds.MED, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            if (!world.isRemote) {
                // Regeneration III for 520 ticks (~26 seconds)
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 520, 2, false, false));
                if (!player.capabilities.isCreativeMode) {
                    stack.shrink(1);
                }
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }

        return new ActionResult<>(EnumActionResult.FAIL, stack);
    }
}
