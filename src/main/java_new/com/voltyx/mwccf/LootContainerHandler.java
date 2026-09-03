package com.voltyx.mwccf;

import com.voltyx.mwccf.network.PacketStartLooting;

import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;

public class LootContainerHandler {

    // 1. КОГДА ИГРОК СТАВИТ БЛОК -> Помечаем его как "Свой"
    @SubscribeEvent
    public void onBlockPlace(BlockEvent.PlaceEvent event) {
        if (event.getWorld().isRemote)
            return;

        TileEntity tile = event.getWorld().getTileEntity(event.getPos());
        if (isContainer(tile)) {
            // Пишем в личную память этого блока, что его поставил игрок
            tile.getTileData().setBoolean("PlayerPlaced", true);
            tile.markDirty();
        }
    }

    // 2. КОГДА ИГРОК КЛИКАЕТ ПКМ ПО БЛОКУ -> Проверяем, можно ли открыть
    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isRemote)
            return; // Проверку делаем только на сервере

        TileEntity tile = event.getWorld().getTileEntity(event.getPos());

        if (isContainer(tile)) {
            NBTTagCompound data = tile.getTileData();
            boolean isPlacedByPlayer = data.getBoolean("PlayerPlaced");
            boolean isUnlocked = data.getBoolean("LootUnlocked");

            // Если это дикий блок из генерации и мы его еще не вскрывали
            if (!isPlacedByPlayer && !isUnlocked) {

                // ОТМЕНЯЕМ ОТКРЫТИЕ GUI!
                event.setCanceled(true);

                // Отправляем пакет клиенту: "Начинай мини-игру с полоской прогресса!"
                MwccfMod.PACKET_HANDLER.sendTo(
                        new PacketStartLooting(event.getPos()),
                        (net.minecraft.entity.player.EntityPlayerMP) event.getEntityPlayer());
            }
        }
    }

    // Универсальный метод проверки: является ли блок контейнером
    private boolean isContainer(TileEntity tile) {
        if (tile == null)
            return false;

        // 1. Получаем регистрационное имя блока (например, "cfm:fridge")
        ResourceLocation regName = tile.getBlockType().getRegistryName();

        // 2. Превращаем в строку (будет формат "cfm:fridge") и проверяем домен
        if (regName != null && regName.toString().startsWith("cfm:")) {

            // 3. Если это блок из Furniture Mod, убеждаемся, что в нем можно хранить вещи
            // (чтобы система не пыталась "лутать" стулья или столы без инвентаря)
            return tile instanceof IInventory
                    || tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        }
        return false; // Это либо стул из cfm, либо сундук/блок из другого мода
    }
}