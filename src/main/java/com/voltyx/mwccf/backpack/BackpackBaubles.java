/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  baubles.api.BaubleType
 *  baubles.api.IBauble
 *  baubles.api.cap.IBaublesItemHandler
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumActionResult
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.common.capabilities.Capability
 *  net.minecraftforge.common.capabilities.CapabilityInject
 *  net.minecraftforge.common.capabilities.ICapabilityProvider
 *  net.minecraftforge.event.AttachCapabilitiesEvent
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$RightClickItem
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.registry.GameRegistry$ObjectHolder
 *  net.minecraftforge.items.IItemHandler
 *  net.minecraftforge.items.wrapper.EmptyHandler
 */
package com.voltyx.mwccf.backpack;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;

@Mod.EventBusSubscriber(modid="mwccf")
public final class BackpackBaubles {
    public static final String ID = "backpackbaubles";
    private static final int BAUBLE_BODY_SLOT = 5;

    @CapabilityInject(IBauble.class)
    private static Capability<IBauble> baubleCapability;

    @CapabilityInject(IBaublesItemHandler.class)
    private static Capability<IBaublesItemHandler> baubleHandlerCapability;

    @CapabilityInject(IItemHandler.class)
    private static Capability<IItemHandler> itemHandlerCapability;

    private static Item backpackItem;

    private static final ICapabilityProvider BAUBLE_PROVIDER = new ICapabilityProvider() {
        private final IBauble bauble = new IBauble() {
            @Override
            public BaubleType getBaubleType(ItemStack stack) {
                return BaubleType.BODY;
            }

            @Override
            public boolean canEquip(ItemStack stack, EntityLivingBase entity) {
                return getChestplateBackpack(entity).isEmpty();
            }

            @Override
            public boolean canUnequip(ItemStack stack, EntityLivingBase entity) {
                IItemHandler handler = getItemHandler(stack);
                for (int slot = 0; slot < handler.getSlots(); ++slot) {
                    if (!handler.getStackInSlot(slot).isEmpty()) {
                        return false;
                    }
                }
                return true;
            }
        };

        @Override
        public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
            return baubleCapability != null && baubleCapability == capability;
        }

        @Nullable
        @Override
        public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
            return hasCapability(capability, side) ? (T) this.bauble : null;
        }
    };

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (baubleCapability != null && isBackpack(event.getObject())) {
            event.addCapability(new ResourceLocation(ID, "capability"), BAUBLE_PROVIDER);
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void equipBackpackAsBauble(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();
        if (!isBackpack(stack)) {
            return;
        }
        EntityPlayer player = event.getEntityPlayer();
        EnumActionResult result = EnumActionResult.FAIL;
        
        if (getChestplateBackpack(player).isEmpty()) {
            IItemHandler handler = getBaubleHandler(player);
            ItemStack remainder = handler.insertItem(BAUBLE_BODY_SLOT, stack.copy(), true);
            if (remainder.getCount() < stack.getCount()) {
                ItemArmor armor = (ItemArmor)stack.getItem();
                ItemArmor.ArmorMaterial material = armor.getArmorMaterial();
                player.playSound(material.getSoundEvent(), 1.0f, 1.0f);
                handler.insertItem(BAUBLE_BODY_SLOT, stack.copy(), false);
                stack.setCount(remainder.getCount());
                result = EnumActionResult.SUCCESS;
            }
        }
        event.setCancellationResult(result);
        event.setCanceled(true);
    }

    public static boolean hasNoBaubleBackpack(Entity entity) {
        return !(entity instanceof EntityPlayer) || getBaubleBackpack((EntityPlayer)entity).isEmpty();
    }

    public static ItemStack getBackpackStack(ItemStack chestplate, EntityLivingBase entity) {
        if (!isBackpack(chestplate) && entity instanceof EntityPlayer) {
            ItemStack bauble = getBaubleBackpack((EntityPlayer)entity);
            if (!bauble.isEmpty()) {
                return bauble;
            }
        }
        return chestplate;
    }

    public static ItemStack getBaubleBackpack(EntityPlayer player) {
        ItemStack bauble = getBaubleHandler(player).getStackInSlot(BAUBLE_BODY_SLOT);
        return isBackpack(bauble) ? bauble : ItemStack.EMPTY;
    }

    private static ItemStack getChestplateBackpack(EntityLivingBase entity) {
        ItemStack chestplate = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        return isBackpack(chestplate) ? chestplate : ItemStack.EMPTY;
    }

    private static IItemHandler getItemHandler(ItemStack stack) {
        if (itemHandlerCapability == null) return EmptyHandler.INSTANCE;
        IItemHandler handler = stack.getCapability(itemHandlerCapability, null);
        return handler != null ? handler : EmptyHandler.INSTANCE;
    }

    private static IItemHandler getBaubleHandler(EntityPlayer player) {
        if (baubleHandlerCapability == null) return EmptyHandler.INSTANCE;
        IBaublesItemHandler handler = player.getCapability(baubleHandlerCapability, null);
        if (handler == null) {
            return EmptyHandler.INSTANCE;
        }
        handler.setPlayer(player);
        return handler;
    }

    private static boolean isBackpack(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        if (backpackItem == null) {
            backpackItem = net.minecraftforge.fml.common.registry.ForgeRegistries.ITEMS.getValue(new ResourceLocation("quark", "backpack"));
        }
        return backpackItem != null && stack.getItem() == backpackItem;
    }
}
