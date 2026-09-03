package efw.mixin;

import com.paneedah.weaponlib.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Pseudo;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Mixin(ItemAttachment.class)
public abstract class MixinItemAttachment {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinItemAttachment class loaded!");
    }

    @Shadow @Final private List<Weapon> compatibleWeapons;
    @Shadow @Final private AttachmentCategory category;

    @Overwrite
    public void addInformation(ItemStack itemStack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (itemStack == null || tooltip == null) return;

        final TextFormatting green = TextFormatting.RED;
        final TextFormatting grey = TextFormatting.YELLOW;
        final TextFormatting gold = TextFormatting.GOLD;
        final ArrayList<String> tooltipLines = new ArrayList<>();

        if (category != null) {
            tooltipLines.add(green + "Type: " + grey + category.name());
        }

        if (category != AttachmentCategory.SKIN && compatibleWeapons != null && !compatibleWeapons.isEmpty()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltipLines.add(green + "Compatible Weapons:");
                StringBuilder row = new StringBuilder();

                for (int i = 0; i < compatibleWeapons.size(); i++) {
                    Weapon weapon = compatibleWeapons.get(i);
                    if (weapon != null) {
                        row.append(grey).append(I18n.format(weapon.getTranslationKey() + ".name"));
                    }

                    if ((i + 1) % 3 == 0 || i == compatibleWeapons.size() - 1) {
                        tooltipLines.add(row.toString());
                        row = new StringBuilder();
                    } else {
                        row.append(", ");
                    }
                }
            } else {
                tooltipLines.add(gold + "Press left shift to see compatible weapons");
            }
        }

        if (itemStack.getItem() instanceof ItemMagazine) {
            ItemMagazine mag = (ItemMagazine) itemStack.getItem();
            if (mag != null) {
                tooltipLines.add(green + "Ammunition: "
                        + grey + Tags.getAmmo(itemStack)
                        + "/" + mag.getCapacity());
            }
        }

        if (itemStack.getItem() instanceof ItemAttachment) {
            com.paneedah.weaponlib.stats.AttachmentStatData stats =
                    com.paneedah.weaponlib.stats.AttachmentStatsManager.getStats((ItemAttachment<?>) itemStack.getItem());
            if (stats != null) {
                boolean isRu = false;
                try {
                    net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
                    if (mc != null && mc.getLanguageManager() != null && mc.getLanguageManager().getCurrentLanguage() != null) {
                        String code = mc.getLanguageManager().getCurrentLanguage().getLanguageCode();
                        isRu = code != null && code.toLowerCase().startsWith("ru");
                    }
                } catch (Throwable ignored) {}

                // Recoil
                if (Math.abs(stats.recoilMultiplier - 1.0) > 0.001) {
                    double pct = (1.0 - stats.recoilMultiplier) * 100.0;
                    if (pct > 0) {
                        tooltipLines.add(TextFormatting.GREEN + " ▲ " + (isRu ? "Контроль отдачи: +" : "Recoil Control: +") + String.format("%.0f%%", pct));
                    } else {
                        tooltipLines.add(TextFormatting.RED + " ▼ " + (isRu ? "Отдача: +" : "Recoil: +") + String.format("%.0f%%", -pct));
                    }
                }

                // Visual Recoil (LERP)
                if (Math.abs(stats.visualRecoilMultiplier - 1.0) > 0.001) {
                    double pct = (1.0 - stats.visualRecoilMultiplier) * 100.0;
                    if (pct > 0) {
                        tooltipLines.add(TextFormatting.GREEN + " ▲ " + (isRu ? "Стабилизация в руках: +" : "Hand Stabilization: +") + String.format("%.0f%%", pct));
                    } else {
                        tooltipLines.add(TextFormatting.RED + " ▼ " + (isRu ? "Смещение в руках: +" : "Weapon Kick: +") + String.format("%.0f%%", -pct));
                    }
                }

                // Hip fire spread
                if (Math.abs(stats.hipSpreadMultiplier - 1.0) > 0.001) {
                    double pct = (1.0 - stats.hipSpreadMultiplier) * 100.0;
                    if (pct > 0) {
                        tooltipLines.add(TextFormatting.GREEN + " ▲ " + (isRu ? "Точность от бедра: +" : "Hip-Fire Accuracy: +") + String.format("%.0f%%", pct));
                    } else {
                        tooltipLines.add(TextFormatting.RED + " ▼ " + (isRu ? "Разброс от бедра: +" : "Hip-Fire Spread: +") + String.format("%.0f%%", -pct));
                    }
                }

                // Aim spread
                if (Math.abs(stats.aimSpreadMultiplier - 1.0) > 0.001) {
                    double pct = (1.0 - stats.aimSpreadMultiplier) * 100.0;
                    if (pct > 0) {
                        tooltipLines.add(TextFormatting.GREEN + " ▲ " + (isRu ? "Точность в прицеле: +" : "Aim Accuracy: +") + String.format("%.0f%%", pct));
                    } else {
                        tooltipLines.add(TextFormatting.RED + " ▼ " + (isRu ? "Разброс в прицеле: +" : "Aim Spread: +") + String.format("%.0f%%", -pct));
                    }
                }

                // ADS Speed
                if (Math.abs(stats.adsSpeedMultiplier - 1.0) > 0.001) {
                    double pct = (stats.adsSpeedMultiplier - 1.0) * 100.0;
                    if (pct > 0) {
                        tooltipLines.add(TextFormatting.GREEN + " ▲ " + (isRu ? "Скорость прицеливания: +" : "ADS Speed: +") + String.format("%.0f%%", pct));
                    } else {
                        tooltipLines.add(TextFormatting.RED + " ▼ " + (isRu ? "Скорость прицеливания: " : "ADS Speed: ") + String.format("%.0f%%", pct));
                    }
                }

                // Draw Speed
                if (Math.abs(stats.drawSpeedMultiplier - 1.0) > 0.001) {
                    double pct = (stats.drawSpeedMultiplier - 1.0) * 100.0;
                    if (pct > 0) {
                        tooltipLines.add(TextFormatting.GREEN + " ▲ " + (isRu ? "Скорость доставания: +" : "Draw Speed: +") + String.format("%.0f%%", pct));
                    } else {
                        tooltipLines.add(TextFormatting.RED + " ▼ " + (isRu ? "Скорость доставания: " : "Draw Speed: ") + String.format("%.0f%%", pct));
                    }
                }

                // Reload Speed
                if (Math.abs(stats.reloadSpeedMultiplier - 1.0) > 0.001) {
                    double pct = (stats.reloadSpeedMultiplier - 1.0) * 100.0;
                    if (pct > 0) {
                        tooltipLines.add(TextFormatting.GREEN + " ▲ " + (isRu ? "Скорость перезарядки: +" : "Reload Speed: +") + String.format("%.0f%%", pct));
                    } else {
                        tooltipLines.add(TextFormatting.RED + " ▼ " + (isRu ? "Скорость перезарядки: " : "Reload Speed: ") + String.format("%.0f%%", pct));
                    }
                }

                // Weight
                if (Math.abs(stats.weight) > 0.001) {
                    if (stats.weight > 0) {
                        tooltipLines.add(TextFormatting.RED + " ▼ " + (isRu ? "Вес: +" : "Weight: +") + String.format("%.2f кг", stats.weight));
                    } else {
                        tooltipLines.add(TextFormatting.GREEN + " ▲ " + (isRu ? "Вес: " : "Weight: ") + String.format("%.2f кг", stats.weight));
                    }
                }
            }
        }

        tooltip.addAll(tooltipLines);
    }
}