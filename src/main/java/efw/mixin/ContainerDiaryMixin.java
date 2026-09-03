package efw.mixin;

import efw.item.CDiaryItem;
import efw.item.NoteItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketSetSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Container.class)
public class ContainerDiaryMixin {
    static {
        System.out.println("[EFW-MIXIN-LOAD] ContainerDiaryMixin class loaded!");
    }

    @Inject(method = "slotClick", at = @At("HEAD"), cancellable = true)
    private void onSlotClick(int slotId, int dragType, ClickType clickTypeIn,
                             EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        // РўРѕР»СЊРєРѕ РџРљРњ (РџСЂР°РІР°СЏ РљРЅРѕРїРєР° РњС‹С€Рё = dragType 1)
        if (clickTypeIn != ClickType.PICKUP || dragType != 1) return;
        if (slotId < 0) return;

        Container container = (Container)(Object) this;
        if (slotId >= container.inventorySlots.size()) return;

        net.minecraft.inventory.Slot slot = container.inventorySlots.get(slotId);
        ItemStack slotStack = slot.getStack();
        ItemStack cursor = player.inventory.getItemStack();

        // РџСЂРѕРІРµСЂСЏРµРј, С‡С‚Рѕ РѕРґРёРЅ РїСЂРµРґРјРµС‚ вЂ” РґРЅРµРІРЅРёРє, Р° РґСЂСѓРіРѕР№ вЂ” Р·Р°РїРёСЃРєР°
        boolean diaryInCursor = !cursor.isEmpty() && cursor.getItem() instanceof CDiaryItem
                && !slotStack.isEmpty() && slotStack.getItem() instanceof NoteItem;
        boolean noteInCursor = !cursor.isEmpty() && cursor.getItem() instanceof NoteItem
                && !slotStack.isEmpty() && slotStack.getItem() instanceof CDiaryItem;

        if (diaryInCursor || noteInCursor) {

            // РРЎРџР РђР’Р›Р•РќРР•: Р—РІСѓРє РїСЂРѕРёРіСЂС‹РІР°РµС‚СЃСЏ Р·РґРµСЃСЊ (СЃСЂР°Р±Р°С‚С‹РІР°РµС‚ Рё РЅР° РєР»РёРµРЅС‚Рµ, Рё РЅР° СЃРµСЂРІРµСЂРµ)
            // РќР° РєР»РёРµРЅС‚Рµ С‚С‹ СѓСЃР»С‹С€РёС€СЊ Р·РІСѓРє РјРѕРјРµРЅС‚Р°Р»СЊРЅРѕ, Р° СЃРµСЂРІРµСЂ СЂР°Р·РѕС€Р»РµС‚ РµРіРѕ СЃРѕСЃРµРґСЏРј.
            // Р—Р°РјРµРЅРё NOTES РЅР° РЅР°Р·РІР°РЅРёРµ Р·РІСѓРєР° С‚РІРѕРµРіРѕ РґРЅРµРІРЅРёРєР°
            player.playSound(efw.init.EfwModSounds.DIARYOPEN, 1.0f, 1.0f);

            // РњР°РЅРёРїСѓР»СЏС†РёРё СЃ NBT Рё РёРЅРІРµРЅС‚Р°СЂРµРј РїСЂРѕРёСЃС…РѕРґСЏС‚ РўРћР›Р¬РљРћ РЅР° СЃРµСЂРІРµСЂРµ
            if (!player.world.isRemote) {
                ItemStack diaryStack = diaryInCursor ? cursor : slotStack;
                ItemStack noteStack = diaryInCursor ? slotStack : cursor;

                int noteId = NoteItem.getNoteId(noteStack);
                int variant = NoteItem.getVariant(noteStack);

                NBTTagCompound noteNbt = new NBTTagCompound();
                noteNbt.setInteger("noteId", noteId);
                noteNbt.setInteger("variant", variant);

                if (!diaryStack.hasTagCompound()) diaryStack.setTagCompound(new NBTTagCompound());
                NBTTagCompound diaryRoot = diaryStack.getTagCompound();
                if (!diaryRoot.hasKey("efw_diary")) diaryRoot.setTag("efw_diary", new NBTTagCompound());
                NBTTagCompound diaryTag = diaryRoot.getCompoundTag("efw_diary");

                NBTTagList notesList = diaryTag.getTagList("StoredNotes", 10);
                notesList.appendTag(noteNbt);
                diaryTag.setTag("StoredNotes", notesList);

                noteStack.shrink(1);
                if (noteStack.isEmpty()) {
                    if (diaryInCursor) {
                        slot.putStack(ItemStack.EMPTY);
                    } else {
                        player.inventory.setItemStack(ItemStack.EMPTY);
                    }
                }

                // РћР±РЅРѕРІР»СЏРµРј СЃР»РѕС‚С‹
                slot.onSlotChanged();
                player.openContainer.detectAndSendChanges();

                // Р–РµСЃС‚РєР°СЏ СЃРёРЅС…СЂРѕРЅРёР·Р°С†РёСЏ РєСѓСЂСЃРѕСЂР°
                if (player instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) player).connection.sendPacket(new SPacketSetSlot(-1, -1, player.inventory.getItemStack()));
                }
            }

            cir.setReturnValue(ItemStack.EMPTY);
        }
    }
}