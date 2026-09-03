package efw.biomeinfo;

import net.minecraft.client.gui.FontRenderer;

public enum TextAlignment {
    LEFT((font, text, scale) -> 0),
    MIDDLE((font, text, scale) -> (int) (font.getStringWidth(text) * scale / 2.0F)),
    RIGHT((font, text, scale) -> (int) (font.getStringWidth(text) * scale));

    private final NegativeOffset negativeOffset;

    TextAlignment(NegativeOffset offset) {
        this.negativeOffset = offset;
    }

    public int getNegativeOffset(FontRenderer font, String text, float scale) {
        return negativeOffset.get(font, text, scale);
    }

    @FunctionalInterface
    public interface NegativeOffset {
        int get(FontRenderer font, String text, float scale);
    }
}
