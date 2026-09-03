package com.voltyx.mwccf;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = "mwccf", value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class TorchAnimationHandler {

    private static final Map<EntityPlayer, AnimState> states = new WeakHashMap<>();

    public static class AnimState {
        public boolean initialized = false;

        public float prevRight = 0f, right = 0f;
        public float prevLeft = 0f, left = 0f;

        public float prevRightSwitch = 0f, rightSwitch = 0f;
        public float prevLeftSwitch = 0f, leftSwitch = 0f;

        public Item prevRightItem = null;
        public Item prevLeftItem = null;

        // НОВОЕ: Память для состояния прицеливания/использования
        public boolean prevActionState = false;

        public float rightSnapX = 0f, rightSnapY = 0f, rightSnapZ = 0f;
        public float leftSnapX = 0f, leftSnapY = 0f, leftSnapZ = 0f;

        public float finalRightX = 0f, finalRightY = 0f, finalRightZ = 0f;
        public float finalLeftX = 0f, finalLeftY = 0f, finalLeftZ = 0f;


    }

    public static AnimState getState(EntityPlayer player) {
        return states.get(player);
    }

    public static boolean isTorch(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        Item item = stack.getItem();
        if (item == Item.getItemFromBlock(net.minecraft.init.Blocks.TORCH)
                || item == Item.getItemFromBlock(net.minecraft.init.Blocks.REDSTONE_TORCH)) return true;
        if (item instanceof ItemBlock) {
            net.minecraft.block.Block b = ((ItemBlock) item).getBlock();
            if (b instanceof net.minecraft.block.BlockTorch) return true;
        }
        if (item.getRegistryName() != null) {
            String name = item.getRegistryName().toString().toLowerCase();
            if (name.contains("torch") || name.contains("flashlight") || name.contains("lantern")) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !event.player.world.isRemote) return;

        EntityPlayer player = event.player;
        AnimState state = states.computeIfAbsent(player, k -> new AnimState());

        state.prevRight = state.right;
        state.prevLeft = state.left;
        state.prevRightSwitch = state.rightSwitch;
        state.prevLeftSwitch = state.leftSwitch;

        boolean mainIsRight = player.getPrimaryHand() == EnumHandSide.RIGHT;
        boolean rightIsTorch = isTorch(mainIsRight ? player.getHeldItemMainhand() : player.getHeldItemOffhand());
        boolean leftIsTorch = isTorch(mainIsRight ? player.getHeldItemOffhand() : player.getHeldItemMainhand());

        float torchSpeed = 0.15f;
        if (rightIsTorch) {
            if (state.right < 1.0f) state.right = Math.min(1.0f, state.right + torchSpeed);
        } else {
            if (state.right > 0.0f) state.right = Math.max(0.0f, state.right - torchSpeed);
        }

        if (leftIsTorch) {
            if (state.left < 1.0f) state.left = Math.min(1.0f, state.left + torchSpeed);
        } else {
            if (state.left > 0.0f) state.left = Math.max(0.0f, state.left - torchSpeed);
        }

        // Если хочешь, чтобы прицеливание было чуть быстрее/резче чем смена предметов,
        // можешь увеличить switchSpeed до 0.25f
        float switchSpeed = 0.20f;
        if (state.rightSwitch > 0) state.rightSwitch = Math.max(0, state.rightSwitch - switchSpeed);
        if (state.leftSwitch > 0) state.leftSwitch = Math.max(0, state.leftSwitch - switchSpeed);
    }
}