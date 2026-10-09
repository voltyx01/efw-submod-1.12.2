package com.voltyx.mwccf.speech.client;

import com.voltyx.mwccf.speech.SpeechConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.ChatType;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class SpeechClientEvents {

    @SubscribeEvent
    public void onPotionShift(net.minecraftforge.client.event.GuiScreenEvent.PotionShiftEvent event) {
        event.setCanceled(true);
    }

    private static final java.lang.reflect.Field GUI_CHAT_DEFAULT_TEXT;
    static {
        java.lang.reflect.Field field = null;
        try {
            field = net.minecraft.client.gui.GuiChat.class.getDeclaredField("defaultInputFieldText");
        } catch (NoSuchFieldException e) {
            try {
                field = net.minecraft.client.gui.GuiChat.class.getDeclaredField("field_146409_v");
            } catch (NoSuchFieldException ignored) {}
        }
        if (field != null) {
            field.setAccessible(true);
        }
        GUI_CHAT_DEFAULT_TEXT = field;
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.getGui() instanceof net.minecraft.client.gui.GuiChat) {
            String initialText = "";
            if (GUI_CHAT_DEFAULT_TEXT != null) {
                try {
                    Object val = GUI_CHAT_DEFAULT_TEXT.get(event.getGui());
                    if (val instanceof String) initialText = (String) val;
                } catch (Exception ignored) {}
            }
            SpeechPanelController.setInitialText(initialText);
            SpeechPanelController.focusInput();
            event.setGui(new SpeechPanelScreen());
        }
    }

    @SubscribeEvent
    public void onMouseInput(net.minecraftforge.client.event.GuiScreenEvent.MouseInputEvent.Pre event) {
        if (!(event.getGui() instanceof SpeechPanelHost) || !SpeechPanelController.isInventoryPanelOpen()) return;
        int dWheel = org.lwjgl.input.Mouse.getEventDWheel();
        if (dWheel == 0) return;
        Minecraft mc = Minecraft.getMinecraft();
        int width = event.getGui().width;
        int height = event.getGui().height;
        int mouseX = org.lwjgl.input.Mouse.getEventX() * width / mc.displayWidth;
        int mouseY = height - org.lwjgl.input.Mouse.getEventY() * height / mc.displayHeight - 1;
        SpeechPanelHost host = (SpeechPanelHost) event.getGui();
        if (SpeechPanelController.handleMouseWheel(mouseX, mouseY, dWheel, width,
                host.speech$getGuiLeft(), host.speech$getGuiTop(), host.speech$getGuiHeight())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null) {
            SpeechClientManager.clearWorldState();
            return;
        }
        if (!mc.isGamePaused()) SpeechClientManager.update();
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (event.getType() == ChatType.CHAT) event.setCanceled(true);
    }

    @SubscribeEvent
    public void onRenderHotbar(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null && (com.voltyx.mwccf.doll.SayaDollManager.isActivating(mc.player)
                || com.voltyx.mwccf.render.doll.DollRenderer.isDollActive()
                || com.voltyx.mwccf.doll.SayaDollManager.getDarknessAlpha() > 0.01f)) return;
        renderPersonalReplica(new ScaledResolution(mc), mc);
    }

    public static void renderPersonalReplica(ScaledResolution resolution, Minecraft mc) {
        float replicaAlpha = SpeechClientManager.getPersonalAlpha();
        if (replicaAlpha < 0.04F) return;

        boolean isProcedural = SpeechClientManager.isPersonalProcedural();

        // Gradient is ONLY displayed for procedural calls (like Saya's doll replica), never for normal chat
        float gradientAlpha = isProcedural ? replicaAlpha : 0.0F;

        if (gradientAlpha > 0.04F && SpeechConfig.personalBottomGradient) {
            renderBottomGradient(resolution, gradientAlpha);
        }

        SpeechClientManager.TypedLine line = SpeechClientManager.getPersonalLine();
        if (line == null || line.getVisibleText().isEmpty() || replicaAlpha < 0.04F) return;

        String visible = line.getVisibleText();
        String toRender = SpeechConfig.personalShowBrackets ? "[ " + visible + " ]" : visible;
        int textWidth = mc.fontRenderer.getStringWidth(toRender);
        int centerX = resolution.getScaledWidth() / 2 + Math.round(SpeechConfig.personalXOffset);
        int y = resolution.getScaledHeight() - 50 + Math.round(SpeechConfig.personalYOffset);

        int a = Math.max(8, Math.min(255, (int) (replicaAlpha * 255.0F)));
        int rgb = SpeechConfig.getPersonalTextColorRgb() & 0x00FFFFFF;
        int color = (a << 24) | rgb;
        int textX = centerX - textWidth / 2;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA,
                GL11.GL_ONE, GL11.GL_ZERO);

        float scale = Math.max(0.1F, SpeechConfig.personalScale);
        if (Math.abs(scale - 1.0F) > 0.001F) {
            GlStateManager.translate((float) centerX, (float) (y + 4), 0.0F);
            GlStateManager.scale(scale, scale, 1.0F);
            GlStateManager.translate((float) -centerX, (float) -(y + 4), 0.0F);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.fontRenderer.drawStringWithShadow(toRender, textX, y, color);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void renderBottomGradient(ScaledResolution resolution, float alpha) {
        if (alpha <= 0.04F) return;
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();
        float topY = height * (2.0F / 3.0F); // 1/3 высоты экрана от нижнего края
        float bottomY = (float) height;
        float maxBottomAlpha = Math.min(1.0F, alpha * 0.88F);

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
        );
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buf.pos(width, topY,    -90).color(0.0F, 0.0F, 0.0F, 0.0F).endVertex();
        buf.pos(0,     topY,    -90).color(0.0F, 0.0F, 0.0F, 0.0F).endVertex();
        buf.pos(0,     bottomY, -90).color(0.0F, 0.0F, 0.0F, maxBottomAlpha).endVertex();
        buf.pos(width, bottomY, -90).color(0.0F, 0.0F, 0.0F, maxBottomAlpha).endVertex();
        tess.draw();

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}
