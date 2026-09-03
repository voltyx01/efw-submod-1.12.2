package com.paneedah.weaponlib;

import com.paneedah.weaponlib.crafting.CraftingEntry;
import com.paneedah.weaponlib.crafting.CraftingGroup;
import com.paneedah.weaponlib.crafting.IModernCraftingRecipe;
import com.paneedah.weaponlib.melee.PlayerMeleeInstance;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static com.paneedah.mwc.utils.ModReference.ID;

public class ItemAttachment<T> extends Item implements ModelSource, IModernCraftingRecipe {

	private AttachmentCategory category;
	private String crosshair;
	private ApplyHandler<T> apply;
	private ApplyHandler<T> remove;
	protected ApplyHandler2<T> apply2;
	protected ApplyHandler2<T> remove2;
	protected MeleeWeaponApplyHandler<T> apply3;
	protected MeleeWeaponApplyHandler<T> remove3;
	private List<Tuple<ModelBase, String>> texturedModels = new ArrayList<>();
	private List<CustomRenderer<?>> postRenderer = new ArrayList<>();
	private CustomRenderer<?> preRenderer;
	private Part renderablePart;
	private String name;
	private Function<ItemStack, String> informationProvider;
	protected int maxStackSize = 1;

	private CraftingEntry[] modernRecipe;
	private CraftingGroup craftGroup;
	
	private List<CompatibleAttachment<T>> attachments = new ArrayList<>();

	private List<Weapon> compatibleWeapons = new ArrayList<>();
	
	private List<ItemAttachment<T>> requiredAttachments = new ArrayList<>();

	protected String textureName;
	
	public Vec3d rotationPoint = Vec3d.ZERO;
	

	public static interface ApplyHandler<T> {
		public void apply(ItemAttachment<T> itemAttachment, T target, EntityLivingBase player);
	}

	public static interface ApplyHandler2<T> {
		public void apply(ItemAttachment<T> itemAttachment, PlayerWeaponInstance instance);
	}

	public static interface MeleeWeaponApplyHandler<T> {
        public void apply(ItemAttachment<T> itemAttachment, PlayerMeleeInstance instance);
    }

	protected ItemAttachment(AttachmentCategory category, ModelBase model, String textureName, String crosshair,
			ApplyHandler<T> apply, ApplyHandler<T> remove) {
		this.category = category;
//		if(model != null) {
//			this.texturedModels.add(new Tuple<ModelBase, String>(model, textureName));
//		}
		this.textureName = textureName.toLowerCase();
		this.crosshair = crosshair != null ? ID + ":" + "textures/crosshairs/" + crosshair + ".png" : null;
		this.apply = apply;
		this.remove = remove;
	}

	protected ItemAttachment(AttachmentCategory category, String crosshair,
			ApplyHandler<T> apply, ApplyHandler<T> remove) {
		this.category = category;
		this.crosshair = crosshair != null ? ID + ":" + "textures/crosshairs/" + crosshair + ".png" : null;
		this.apply = apply;
		this.remove = remove;
	}

	@Override
	public int getItemStackLimit() {
		return maxStackSize;
	}

	public Item setTextureName(String name) {
		this.textureName = name;
		return this;
	}

	public String getTextureName() {
		return textureName;
	}
	
	@Override
	public CraftingGroup getCraftingGroup() {
		return this.craftGroup;
	}
	
	public void setCraftingGroup(CraftingGroup cg) {
		this.craftGroup = cg;
	}
	
	public void setModernRecipe(CraftingEntry...is) {
		this.modernRecipe = is;
	}


	public Part getRenderablePart() {
		return renderablePart;
	}

	protected void setRenderablePart(Part renderablePart) {
		this.renderablePart = renderablePart;
	}

	protected Function<ItemStack, String> getInformationProvider() {
		return informationProvider;
	}

	protected void setInformationProvider(
			Function<ItemStack, String> informationProvider) {
		this.informationProvider = informationProvider;
	}
	
	protected void setRequiredAttachments(List<ItemAttachment<T>> requiredAttachments) {
        this.requiredAttachments = Collections.unmodifiableList(requiredAttachments);
    }
	
	public List<ItemAttachment<T>> getRequiredAttachments() {
        return requiredAttachments;
    }

	@Deprecated
	public ItemAttachment<T> addModel(ModelBase model, String textureName) {
		texturedModels.add(new Tuple<>(model, textureName));
		return this;
	}

	public ItemAttachment(AttachmentCategory category, String crosshair) {
		this(category, crosshair, (a, w, p) -> {}, (a, w, p) -> {});
	}

	public ItemAttachment(AttachmentCategory category, ModelBase attachment, String textureName, String crosshair) {
		this(category, attachment, textureName, crosshair, (a, w, p) -> {}, (a, w ,p) -> {});
	}

	public AttachmentCategory getCategory() {
		return category;
	}

	public List<Tuple<ModelBase, String>> getTexturedModels() {
		return texturedModels;
	}
	
	/**
	 * For use with the "magic mag"
	 * @param model
	 */
	public void setFirstModel(ItemAttachment<Weapon> model) {
		texturedModels.set(0, model.getTexturedModels().get(0));
	}

	public String getCrosshair() {
		return crosshair;
	}

	public ApplyHandler<T> getApply() {
		return apply;
	}

	public ApplyHandler<T> getRemove() {
		return remove;
	}

	public void addCompatibleWeapon(Weapon weapon) {
		compatibleWeapons.add(weapon);
	}

	@Override
    public void addInformation(ItemStack itemStack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if(tooltip != null && informationProvider != null) {
			tooltip.add(informationProvider.apply(itemStack));
		}

		if (tooltip != null) {
			com.paneedah.weaponlib.stats.AttachmentStatData stats = com.paneedah.weaponlib.stats.AttachmentStatsManager.getStats(this);
			if (stats != null) {
				boolean isRu = isRussianLanguage();

				// Recoil
				if (Math.abs(stats.recoilMultiplier - 1.0) > 0.001) {
					double pct = (1.0 - stats.recoilMultiplier) * 100.0;
					if (pct > 0) {
						tooltip.add(net.minecraft.util.text.TextFormatting.GREEN + " ▲ " + (isRu ? "Контроль отдачи: +" : "Recoil Control: +") + String.format("%.0f%%", pct));
					} else {
						tooltip.add(net.minecraft.util.text.TextFormatting.RED + " ▼ " + (isRu ? "Отдача: +" : "Recoil: +") + String.format("%.0f%%", -pct));
					}
				}

				// Visual Recoil (LERP)
				if (Math.abs(stats.visualRecoilMultiplier - 1.0) > 0.001) {
					double pct = (1.0 - stats.visualRecoilMultiplier) * 100.0;
					if (pct > 0) {
						tooltip.add(net.minecraft.util.text.TextFormatting.GREEN + " ▲ " + (isRu ? "Стабилизация в руках: +" : "Hand Stabilization: +") + String.format("%.0f%%", pct));
					} else {
						tooltip.add(net.minecraft.util.text.TextFormatting.RED + " ▼ " + (isRu ? "Смещение в руках: +" : "Weapon Kick: +") + String.format("%.0f%%", -pct));
					}
				}

				// Hip fire spread
				if (Math.abs(stats.hipSpreadMultiplier - 1.0) > 0.001) {
					double pct = (1.0 - stats.hipSpreadMultiplier) * 100.0;
					if (pct > 0) {
						tooltip.add(net.minecraft.util.text.TextFormatting.GREEN + " ▲ " + (isRu ? "Точность от бедра: +" : "Hip-Fire Accuracy: +") + String.format("%.0f%%", pct));
					} else {
						tooltip.add(net.minecraft.util.text.TextFormatting.RED + " ▼ " + (isRu ? "Разброс от бедра: +" : "Hip-Fire Spread: +") + String.format("%.0f%%", -pct));
					}
				}

				// Aim spread
				if (Math.abs(stats.aimSpreadMultiplier - 1.0) > 0.001) {
					double pct = (1.0 - stats.aimSpreadMultiplier) * 100.0;
					if (pct > 0) {
						tooltip.add(net.minecraft.util.text.TextFormatting.GREEN + " ▲ " + (isRu ? "Точность в прицеле: +" : "Aim Accuracy: +") + String.format("%.0f%%", pct));
					} else {
						tooltip.add(net.minecraft.util.text.TextFormatting.RED + " ▼ " + (isRu ? "Разброс в прицеле: +" : "Aim Spread: +") + String.format("%.0f%%", -pct));
					}
				}

				// ADS Speed
				if (Math.abs(stats.adsSpeedMultiplier - 1.0) > 0.001) {
					double pct = (stats.adsSpeedMultiplier - 1.0) * 100.0;
					if (pct > 0) {
						tooltip.add(net.minecraft.util.text.TextFormatting.GREEN + " ▲ " + (isRu ? "Скорость прицеливания: +" : "ADS Speed: +") + String.format("%.0f%%", pct));
					} else {
						tooltip.add(net.minecraft.util.text.TextFormatting.RED + " ▼ " + (isRu ? "Скорость прицеливания: " : "ADS Speed: ") + String.format("%.0f%%", pct));
					}
				}

				// Draw Speed
				if (Math.abs(stats.drawSpeedMultiplier - 1.0) > 0.001) {
					double pct = (stats.drawSpeedMultiplier - 1.0) * 100.0;
					if (pct > 0) {
						tooltip.add(net.minecraft.util.text.TextFormatting.GREEN + " ▲ " + (isRu ? "Скорость доставания: +" : "Draw Speed: +") + String.format("%.0f%%", pct));
					} else {
						tooltip.add(net.minecraft.util.text.TextFormatting.RED + " ▼ " + (isRu ? "Скорость доставания: " : "Draw Speed: ") + String.format("%.0f%%", pct));
					}
				}

				// Reload Speed
				if (Math.abs(stats.reloadSpeedMultiplier - 1.0) > 0.001) {
					double pct = (stats.reloadSpeedMultiplier - 1.0) * 100.0;
					if (pct > 0) {
						tooltip.add(net.minecraft.util.text.TextFormatting.GREEN + " ▲ " + (isRu ? "Скорость перезарядки: +" : "Reload Speed: +") + String.format("%.0f%%", pct));
					} else {
						tooltip.add(net.minecraft.util.text.TextFormatting.RED + " ▼ " + (isRu ? "Скорость перезарядки: " : "Reload Speed: ") + String.format("%.0f%%", pct));
					}
				}

				// Weight
				if (Math.abs(stats.weight) > 0.001) {
					if (stats.weight > 0) {
						tooltip.add(net.minecraft.util.text.TextFormatting.RED + " ▼ " + (isRu ? "Вес: +" : "Weight: +") + String.format("%.2f кг", stats.weight));
					} else {
						tooltip.add(net.minecraft.util.text.TextFormatting.GREEN + " ▲ " + (isRu ? "Вес: " : "Weight: ") + String.format("%.2f кг", stats.weight));
					}
				}
			}
		}
	}

	private static boolean isRussianLanguage() {
		try {
			net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
			if (mc != null && mc.getLanguageManager() != null && mc.getLanguageManager().getCurrentLanguage() != null) {
				String code = mc.getLanguageManager().getCurrentLanguage().getLanguageCode();
				return code != null && code.toLowerCase().startsWith("ru");
			}
		} catch (Throwable ignored) {}
		return false;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setPostRenderers(List<CustomRenderer<?>> postRenderer) {
		postRenderer = postRenderer;
	}


	@Override
	public CustomRenderer<?> getPostRenderer() {
		return postRenderer.isEmpty() ? null : postRenderer.get(0);
	}
	
	public List<CustomRenderer<?>> getAllPostRenderers() {
		return postRenderer;
	}

	public CustomRenderer<?> getPreRenderer() {
		return preRenderer;
	}

	public void setPreRenderer(CustomRenderer<?> preRenderer) {
		this.preRenderer = preRenderer;
	}

	protected void addCompatibleAttachment(CompatibleAttachment<T> attachment) {
		attachments.add(attachment);
	}

	public List<CompatibleAttachment<T>> getAttachments() {
		return Collections.unmodifiableList(attachments);
	}

	@Override
	public String toString() {
		return name != null ? "Attachment [" + name + "]" : super.toString();
	}

	public ApplyHandler2<T> getApply2() {
		return apply2;
	}

	protected ApplyHandler2<T> getRemove2() {
		return remove2;
	}

    public MeleeWeaponApplyHandler<T> getApply3() {
        return apply3;
    }

    public MeleeWeaponApplyHandler<T> getRemove3() {
        return remove3;
    }

	public void setPostRenderer(List<CustomRenderer<?>> postRenderer2) {
		this.postRenderer = postRenderer2;
		
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemStack(this);
	}

	@Override
	public CraftingEntry[] getModernRecipe() {
		return this.modernRecipe;
	}

	@Override
	public void setCraftingRecipe(CraftingEntry[] recipe) {
		this.modernRecipe = recipe;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return true;
	}

	@SuppressWarnings("rawtypes")
	public static ItemAttachment<?> lookupAttachment(int id) {
		Item item = Item.getItemById(id);
		if (item instanceof ItemAttachment) {
			return (ItemAttachment) item;
		}
		return null;
	}
}
