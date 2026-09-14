package efw.mixin;

import efw.item.CDiaryItem;
import efw.item.NoteItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Container.class)
public abstract class ContainerDiaryMixin {

    @Inject(method = "slotClick", at = @At("HEAD"), cancellable = true)
    private void onSlotClick(int slotId, int dragType, ClickType clickTypeIn,
                             EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        // Только ПКМ (Правая кнопка мыши: dragType == 1, ClickType.PICKUP)
        if (clickTypeIn != ClickType.PICKUP || dragType != 1) return;
        if (slotId < 0) return;

        Container container = (Container) (Object) this;
        if (slotId >= container.inventorySlots.size()) return;

        Slot slot = container.inventorySlots.get(slotId);
        if (slot == null) return;
        ItemStack slotStack = slot.getStack();
        ItemStack cursor = player.inventory.getItemStack();

        // 1. Дневник в курсоре, кликаем ПКМ по записке в слоте
        boolean diaryInCursor = !cursor.isEmpty() && cursor.getItem() instanceof CDiaryItem
                && !slotStack.isEmpty() && slotStack.getItem() instanceof NoteItem;

        // 2. Записка в курсоре, кликаем ПКМ по дневнику в слоте
        boolean noteInCursor = !cursor.isEmpty() && cursor.getItem() instanceof NoteItem
                && !slotStack.isEmpty() && slotStack.getItem() instanceof CDiaryItem;

        // 3. Курсор пуст, кликаем ПКМ по записке в слоте, а дневник лежит в инвентаре игрока
        boolean emptyCursorOnNote = cursor.isEmpty()
                && !slotStack.isEmpty() && slotStack.getItem() instanceof NoteItem;

        if (diaryInCursor) {
            ItemStack returnStack = slotStack.copy();
            boolean absorbed = CDiaryItem.tryAbsorbNote(cursor, slotStack, player);
            if (absorbed) {
                if (slotStack.isEmpty()) {
                    slot.putStack(ItemStack.EMPTY);
                }
                slot.onSlotChanged();

                if (!player.world.isRemote) {
                    player.openContainer.detectAndSendChanges();
                    if (player instanceof EntityPlayerMP) {
                        EntityPlayerMP mp = (EntityPlayerMP) player;
                        mp.connection.sendPacket(new SPacketSetSlot(-1, -1, player.inventory.getItemStack()));
                        mp.connection.sendPacket(new SPacketSetSlot(container.windowId, slotId, slot.getStack()));
                    }
                }
                cir.setReturnValue(returnStack);
            }
        } else if (noteInCursor) {
            ItemStack returnStack = slotStack.copy();
            boolean absorbed = CDiaryItem.tryAbsorbNote(slotStack, cursor, player);
            if (absorbed) {
                if (cursor.isEmpty()) {
                    player.inventory.setItemStack(ItemStack.EMPTY);
                }
                slot.onSlotChanged();

                if (!player.world.isRemote) {
                    player.openContainer.detectAndSendChanges();
                    if (player instanceof EntityPlayerMP) {
                        EntityPlayerMP mp = (EntityPlayerMP) player;
                        mp.connection.sendPacket(new SPacketSetSlot(-1, -1, player.inventory.getItemStack()));
                        mp.connection.sendPacket(new SPacketSetSlot(container.windowId, slotId, slot.getStack()));
                    }
                }
                cir.setReturnValue(returnStack);
            }
        } else if (emptyCursorOnNote && slot.canTakeStack(player)) {
            ItemStack diaryInInv = mwccf$findDiary(container, player);
            if (!diaryInInv.isEmpty()) {
                ItemStack returnStack = slotStack.copy();
                boolean absorbed = CDiaryItem.tryAbsorbNote(diaryInInv, slotStack, player);
                if (absorbed) {
                    if (slotStack.isEmpty()) {
                        slot.putStack(ItemStack.EMPTY);
                    }
                    slot.onSlotChanged();

                    if (!player.world.isRemote) {
                        player.openContainer.detectAndSendChanges();
                        if (player instanceof EntityPlayerMP) {
                            EntityPlayerMP mp = (EntityPlayerMP) player;
                            mp.connection.sendPacket(new SPacketSetSlot(container.windowId, slotId, slot.getStack()));
                        }
                    }
                    cir.setReturnValue(returnStack);
                }
            }
        }
    }

    private static ItemStack mwccf$findDiary(Container container, EntityPlayer player) {
        // 1. Основной инвентарь игрока (хотбар + слоты инвентаря)
        for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
            ItemStack stack = player.inventory.mainInventory.get(i);
            if (!stack.isEmpty() && stack.getItem() instanceof CDiaryItem) {
                return stack;
            }
        }
        // 2. Вторая рука (Offhand)
        for (int i = 0; i < player.inventory.offHandInventory.size(); i++) {
            ItemStack stack = player.inventory.offHandInventory.get(i);
            if (!stack.isEmpty() && stack.getItem() instanceof CDiaryItem) {
                return stack;
            }
        }
        // 3. Слоты открытого контейнера (например, рюкзак или сундук)
        for (int i = 0; i < container.inventorySlots.size(); i++) {
            Slot s = container.inventorySlots.get(i);
            if (s != null && s.getHasStack()) {
                ItemStack stack = s.getStack();
                if (!stack.isEmpty() && stack.getItem() instanceof CDiaryItem) {
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }
}