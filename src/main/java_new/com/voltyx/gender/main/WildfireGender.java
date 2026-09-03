package com.voltyx.gender.main;

import com.voltyx.gender.main.networking.PacketSync;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WildfireGender {
	public static final String VERSION = "2.8";
	public static final String MODID = "wildfire_gender";

	public static boolean modEnabled = true;

	// Ссылка на сеть (инициализируется в MwccfMod)
	public static SimpleNetworkWrapper NETWORK;

	// Хранилище данных загруженных игроков
	public static Map<UUID, GenderPlayer> CLOTHING_PLAYERS = new HashMap<>();

	@Nullable
	public static GenderPlayer getPlayerById(UUID id) {
		return CLOTHING_PLAYERS.get(id);
	}

	public static GenderPlayer getOrAddPlayerById(UUID id) {
		if (!CLOTHING_PLAYERS.containsKey(id)) {
			CLOTHING_PLAYERS.put(id, new GenderPlayer(id));
		}
		return CLOTHING_PLAYERS.get(id);
	}

	@SubscribeEvent
	public static void onPlayerLoginEvent(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		if (!player.world.isRemote && player instanceof EntityPlayerMP) {
			// Отправляем данные всех игроков тому, кто только что зашел
			PacketSync.sendTo((EntityPlayerMP) player);
		}
	}

	public static void loadGenderInfoAsync(UUID uuid, boolean markForSync) {
		Thread thread = new Thread(() -> WildfireGender.loadGenderInfo(uuid, markForSync));
		thread.setName("WFGM_GetPlayer-" + uuid);
		thread.start();
	}

	public static void refreshAllGenders() {
		// Оставлено пустым
	}

	public static GenderPlayer loadGenderInfo(UUID uuid, boolean markForSync) {
		return GenderPlayer.loadCachedPlayer(uuid, markForSync);
	}

	// --- Утилиты для отрисовки интерфейсов (GUI) ---

	@SideOnly(Side.CLIENT)
	public static void drawTextLabel(String txt, int x, int y) {
		GlStateManager.disableBlend();
		int textWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(txt);
		Gui.drawRect(x, y, x + textWidth + 3, y + 11, 0x60000000);
		Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(txt, x + 2, y + 2, 0xFFFFFF);
	}

	@SideOnly(Side.CLIENT)
	public static void drawRightTextLabel(String txt, int x, int y) {
		GlStateManager.disableBlend();
		int w = Minecraft.getMinecraft().fontRenderer.getStringWidth(txt) + 3;
		Gui.drawRect(x - w, y, x, y + 11, 0x60000000);
		Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(txt, x - w + 2, y + 2, 0xFFFFFF);
	}

	@SideOnly(Side.CLIENT)
	public static void drawCenterTextLabel(String txt, int x, int y) {
		GlStateManager.disableBlend();
		int w = Minecraft.getMinecraft().fontRenderer.getStringWidth(txt) + 3;
		Gui.drawRect(x - w / 2, y, x + w / 2 + 1, y + 11, 0x60000000);
		Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(txt, x - w / 2 + 2, y + 2, 0xFFFFFF);
	}

	public interface WildfireCB {
		void onExecute(boolean success, Object data);
	}
}