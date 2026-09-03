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

public class ItemBandage extends Item {

    public ItemBandage() {
        super();
        setMaxStackSize(16);
        setTranslationKey("mcore.bandage");
        setRegistryName("mwccf", "bandage");
        setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        // Only heal if health is not full
        if (player.getHealth() < player.getMaxHealth()) {
            world.playSound(null, player.posX, player.posY, player.posZ, EfwModSounds.BANDAGE, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            if (!world.isRemote) {
                // Regeneration I for 150 ticks (~7.5 seconds)
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 150, 0, false, false));
                if (!player.capabilities.isCreativeMode) {
                    stack.shrink(1);
                }
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }

        return new ActionResult<>(EnumActionResult.FAIL, stack);
    }
}
