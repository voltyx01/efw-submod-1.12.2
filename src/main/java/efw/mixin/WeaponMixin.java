package efw.mixin;

import com.paneedah.weaponlib.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;

import java.util.*;

@Pseudo
@Mixin(Weapon.class)
public abstract class WeaponMixin {

    private static boolean isRussian() {
        try {
            Minecraft mc = Minecraft.getMinecraft();
            String lang = mc.getLanguageManager() != null && mc.getLanguageManager().getCurrentLanguage() != null
                    ? mc.getLanguageManager().getCurrentLanguage().getLanguageCode()
                    : "en_us";
            return lang != null && lang.toLowerCase().startsWith("ru");
        } catch (Throwable t) {
            return false;
        }
    }

    private static String getCleanBulletName(ItemBullet bullet) {
        if (bullet == null) return "";
        ItemStack stack = new ItemStack(bullet);
        String name = stack.getDisplayName();
        if (name == null) return "";
        return name.replace(" Bullet", "").replace(" bullet", "")
                   .replace("Патрон ", "").replace(" патрон", "")
                   .trim();
    }

    @Overwrite(remap = true)
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        Weapon weapon = (Weapon) (Object) this;
        boolean ru = isRussian();

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            Set<ItemBullet> defaultBullets = new LinkedHashSet<>();
            Set<ItemBullet> modBullets = new LinkedHashSet<>();

            // 1. Direct BULLET attachments
            for (CompatibleAttachment<?> ca : weapon.getCompatibleAttachments(AttachmentCategory.BULLET)) {
                if (ca.getAttachment() instanceof ItemBullet) {
                    ItemBullet bullet = (ItemBullet) ca.getAttachment();
                    if (ca.isDefault() || defaultBullets.isEmpty()) {
                        defaultBullets.add(bullet);
                    } else {
                        modBullets.add(bullet);
                    }
                }
            }

            // 2. Default MAGAZINE attachments
            for (CompatibleAttachment<?> ca : weapon.getCompatibleAttachments(AttachmentCategory.MAGAZINE)) {
                if (ca.getAttachment() instanceof ItemMagazine) {
                    ItemMagazine mag = (ItemMagazine) ca.getAttachment();
                    if (ca.isDefault() && mag.getCompatibleBullets() != null) {
                        defaultBullets.addAll(mag.getCompatibleBullets());
                    }
                }
            }

            // If no default magazine was marked as default, take first compatible magazine's bullets
            if (defaultBullets.isEmpty()) {
                for (CompatibleAttachment<?> ca : weapon.getCompatibleAttachments(AttachmentCategory.MAGAZINE)) {
                    if (ca.getAttachment() instanceof ItemMagazine) {
                        ItemMagazine mag = (ItemMagazine) ca.getAttachment();
                        if (mag.getCompatibleBullets() != null && !mag.getCompatibleBullets().isEmpty()) {
                            defaultBullets.addAll(mag.getCompatibleBullets());
                            break;
                        }
                    }
                }
            }

            // 3. All other magazines (check for conversion modifications)
            for (CompatibleAttachment<?> ca : weapon.getCompatibleAttachments(AttachmentCategory.MAGAZINE)) {
                if (ca.getAttachment() instanceof ItemMagazine) {
                    ItemMagazine mag = (ItemMagazine) ca.getAttachment();
                    if (mag.getCompatibleBullets() != null) {
                        for (ItemBullet b : mag.getCompatibleBullets()) {
                            if (!defaultBullets.contains(b)) {
                                modBullets.add(b);
                            }
                        }
                    }
                }
            }

            String calPrefix = ru ? "Калибр: " : "Caliber: ";

            for (ItemBullet b : defaultBullets) {
                String bulletName = getCleanBulletName(b);
                tooltip.add(TextFormatting.RED + calPrefix + TextFormatting.YELLOW + bulletName);
            }

            for (ItemBullet b : modBullets) {
                String bulletName = getCleanBulletName(b);
                tooltip.add(TextFormatting.GOLD + "[Mod] " + TextFormatting.RED + calPrefix + TextFormatting.YELLOW + bulletName);
            }
        } else {
            tooltip.add(TextFormatting.GOLD + (ru ? "Зажмите [Shift] для информации о калибре" : "Press left shift to see caliber info"));
        }

        tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.modding"));
        tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.mwccf.inspect"));
    }
}