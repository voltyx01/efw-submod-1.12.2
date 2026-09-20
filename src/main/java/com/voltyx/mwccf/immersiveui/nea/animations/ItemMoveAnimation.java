package com.voltyx.mwccf.immersiveui.nea.animations;

import com.voltyx.mwccf.immersiveui.ImmersiveUIConfig;
import com.voltyx.mwccf.immersiveui.nea.NEAHelper;
import com.voltyx.mwccf.immersiveui.nea.api.IItemLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

@SideOnly(Side.CLIENT)
public class ItemMoveAnimation {

    private static final Map<Integer, List<ItemMovePacket>> movingItemsBySource = new HashMap<>();
    private static GuiContainer lastGui = null;
    private static final List<ItemStack> virtualStacks = new ArrayList<>(256);
    private static final List<Integer> virtualStacksUser = new ArrayList<>(256);
    private static long lastAnimation = 0;

    public static void onGuiOpen(GuiOpenEvent event) {
        if (ImmersiveUIConfig.moveAnimationTime <= 0) return;
        if (!(event.getGui() instanceof GuiContainer)) {
            if (lastGui != null) {
                lastGui = null;
                movingItemsBySource.clear();
            }
            return;
        }
        if (!ImmersiveUIConfig.isBlacklisted(event.getGui())) {
            lastGui = (GuiContainer) event.getGui();
            movingItemsBySource.clear();
            virtualStacks.clear();
            virtualStacksUser.clear();
        }
    }

    public static ItemStack getVirtualStack(GuiContainer container, Slot slot) {
        return getVirtualStack(container, IItemLocation.of(slot));
    }

    public static ItemStack getVirtualStack(GuiContainer container, IItemLocation slot) {
        int idx = slot.nea$getSlotNumber() + 1;
        if (container == lastGui && !ImmersiveUIConfig.isBlacklisted(container)
                && virtualStacks.size() > idx && virtualStacksUser.size() > idx
                && virtualStacksUser.get(idx) > 0) {
            return virtualStacks.get(idx);
        }
        return null;
    }

    public static void updateVirtualStack(int target, ItemStack stack, int op) {
        if (ImmersiveUIConfig.moveAnimationTime <= 0) return;
        int targetIdx = target + 1;
        while (targetIdx >= virtualStacks.size()) {
            virtualStacks.add(null);
            virtualStacksUser.add(0);
        }
        int users = virtualStacksUser.get(targetIdx) + op;
        if (users <= 0) {
            stack = null;
            users = 0;
        }
        virtualStacks.set(targetIdx, stack);
        virtualStacksUser.set(targetIdx, users);
    }

    public static void queueAnimation(int slot, ItemMovePacket packet) {
        int key = slot + 1;
        List<ItemMovePacket> packets = movingItemsBySource.get(key);
        if (packets == null) {
            packets = new ArrayList<>();
            movingItemsBySource.put(key, packets);
        }
        packets.add(packet);
    }

    public static void queueAnimation(int slot, List<ItemMovePacket> packetList) {
        int key = slot + 1;
        List<ItemMovePacket> packets = movingItemsBySource.get(key);
        if (packets == null) {
            packets = new ArrayList<>();
            movingItemsBySource.put(key, packets);
        }
        packets.addAll(packetList);
    }

    public static Pair<List<Slot>, List<ItemStack>> getCandidates(Slot in, List<Slot> allSlots) {
        if (ImmersiveUIConfig.moveAnimationTime <= 0 || ImmersiveUIConfig.isBlacklisted(
                Minecraft.getMinecraft().currentScreen) || NEAHelper.time() - lastAnimation <= 10) {
            return null;
        }
        List<Slot> slots = new ArrayList<>(allSlots.size());
        List<ItemStack> stacks = new ArrayList<>(allSlots.size());
        ItemStack item = IItemLocation.of(in).nea$getStack();
        for (Slot slot : allSlots) {
            if (in == slot) continue;
            ItemStack other = slot.getStack();
            if (other.isEmpty()) {
                slots.add(slot);
                stacks.add(ItemStack.EMPTY);
            } else if (ItemHandlerHelper.canItemStacksStack(item, other)) {
                slots.add(slot);
                stacks.add(other.copy());
            }
        }
        return Pair.of(slots, stacks);
    }

    public static void handleMove(Slot source, ItemStack oldSource, Pair<List<Slot>, List<ItemStack>> candidates) {
        IItemLocation sourceLoc = IItemLocation.of(source);
        List<Slot> slots = candidates.getLeft();
        List<ItemStack> stacks = candidates.getRight();
        int total = oldSource.getCount() - source.getStack().getCount();
        if (total <= 0) return;

        List<ItemMovePacket> packets = new ArrayList<>();
        Map<Integer, ItemStack> stagedVirtualStacks = new HashMap<>();
        boolean error = false;
        long time = NEAHelper.time();

        for (int i = 0; i < slots.size(); i++) {
            IItemLocation slot = IItemLocation.of(slots.get(i));
            if (slot == sourceLoc) continue;
            ItemStack oldStack = stacks.get(i);
            ItemStack newStack = slot.nea$getStack();
            if (oldStack.isEmpty()) {
                if (!newStack.isEmpty()) {
                    ItemMovePacket packet = new ItemMovePacket(time, sourceLoc, slot, newStack.copy());
                    packets.add(packet);
                    total -= newStack.getCount();
                    stagedVirtualStacks.put(slot.nea$getSlotNumber(), oldStack);
                }
            } else if (ItemHandlerHelper.canItemStacksStack(newStack, oldStack)) {
                if (oldStack.getCount() < newStack.getCount()) {
                    ItemStack movingStack = newStack.copy();
                    movingStack.shrink(oldStack.getCount());
                    ItemMovePacket packet = new ItemMovePacket(time, sourceLoc, slot, movingStack);
                    packets.add(packet);
                    total -= movingStack.getCount();
                    stagedVirtualStacks.put(slot.nea$getSlotNumber(), oldStack);
                } else if (oldStack.getCount() > newStack.getCount()) {
                    error = true;
                }
            } else {
                error = true;
            }
            if (total <= 0) break;
        }

        if (error || packets.isEmpty()) return;
        queueAnimation(sourceLoc.nea$getSlotNumber(), packets);
        for (Map.Entry<Integer, ItemStack> e : stagedVirtualStacks.entrySet()) {
            updateVirtualStack(e.getKey(), e.getValue(), 1);
        }
        lastAnimation = NEAHelper.time();
    }

    public static void drawAnimations(RenderItem itemRender, FontRenderer fontRenderer) {
        Iterator<Map.Entry<Integer, List<ItemMovePacket>>> iter = movingItemsBySource.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, List<ItemMovePacket>> entry = iter.next();
            List<ItemMovePacket> packets = entry.getValue();
            Iterator<ItemMovePacket> iterator = packets.iterator();
            while (iterator.hasNext()) {
                ItemMovePacket packet = iterator.next();
                boolean end = false;
                float val = packet.value();
                if (val >= 1.0F) {
                    val = 1.0F;
                    end = true;
                }
                int x = packet.getDrawX(val);
                int y = packet.getDrawY(val);
                GlStateManager.translate(0.0F, 0.0F, 32.0F);
                FontRenderer font = packet.getMovingStack().getItem().getFontRenderer(packet.getMovingStack());
                if (font == null) font = fontRenderer;
                itemRender.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().player, packet.getMovingStack(), x, y);
                itemRender.renderItemOverlayIntoGUI(font, packet.getMovingStack(), x, y, null);
                if (end) {
                    ItemMoveAnimation.updateVirtualStack(packet.getTarget().nea$getSlotNumber(), packet.getTargetStack(), -1);
                    if (packets.size() == 1) {
                        iter.remove();
                        break;
                    }
                    iterator.remove();
                }
            }
            if (packets.isEmpty()) {
                iter.remove();
            }
        }
    }
}
