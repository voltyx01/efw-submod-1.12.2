package efw.biomeinfo;

import java.util.function.Supplier;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

import static efw.biomeinfo.BiomeInfoRenderer.MARGIN;

public enum PositionPreset {
    NONE(
            res -> MwccfConfig.biomeinfo.posX,
            (res, lineHeight) -> MwccfConfig.biomeinfo.posY,
            () -> MwccfConfig.biomeinfo.textAlignment
    ),
    TOP_LEFT(
            res -> MARGIN,
            (res, lineHeight) -> MARGIN,
            () -> TextAlignment.LEFT
    ),
    TOP_MIDDLE(
            res -> res.getScaledWidth() / 2,
            (res, lineHeight) -> MARGIN,
            () -> TextAlignment.MIDDLE
    ),
    TOP_RIGHT(
            res -> res.getScaledWidth() - MARGIN,
            (res, lineHeight) -> MARGIN,
            () -> TextAlignment.RIGHT
    ),
    MIDDLE_LEFT(
            res -> MARGIN,
            (res, lineHeight) -> (int) (res.getScaledHeight() / 2.0F - lineHeight / 2),
            () -> TextAlignment.LEFT
    ),
    MIDDLE(
            res -> res.getScaledWidth() / 2,
            (res, lineHeight) -> (int) (res.getScaledHeight() / 2.0F - lineHeight / 2),
            () -> TextAlignment.MIDDLE
    ),
    MIDDLE_RIGHT(
            res -> res.getScaledWidth() - MARGIN,
            (res, lineHeight) -> (int) (res.getScaledHeight() / 2.0F - lineHeight / 2),
            () -> TextAlignment.RIGHT
    ),
    BOTTOM_LEFT(
            res -> MARGIN,
            (res, lineHeight) -> (int) (res.getScaledHeight() - MARGIN - lineHeight),
            () -> TextAlignment.LEFT
    ),
    BOTTOM_MIDDLE(
            res -> res.getScaledWidth() / 2,
            (res, lineHeight) -> (int) (res.getScaledHeight() - MARGIN - lineHeight),
            () -> TextAlignment.MIDDLE
    ),
    BOTTOM_RIGHT(
            res -> res.getScaledWidth() - MARGIN,
            (res, lineHeight) -> (int) (res.getScaledHeight() - MARGIN - lineHeight),
            () -> TextAlignment.RIGHT
    ),
    ABOVE_MIDDLE(
            res -> res.getScaledWidth() / 2,
            (res, lineHeight) -> res.getScaledHeight() / 4,
            () -> TextAlignment.MIDDLE
    ),
    ABOVE_HOTBAR(
            res -> res.getScaledWidth() / 2,
            (res, lineHeight) -> res.getScaledHeight() - 68,
            () -> TextAlignment.MIDDLE
    ),
    LEFT_OF_CROSSHAIR(
            res -> res.getScaledWidth() / 2 - MARGIN - 3,
            (res, lineHeight) -> (int) (res.getScaledHeight() / 2.0F - lineHeight / 2),
            () -> TextAlignment.RIGHT
    ),
    RIGHT_OF_CROSSHAIR(
            res -> res.getScaledWidth() / 2 + MARGIN + 3,
            (res, lineHeight) -> (int) (res.getScaledHeight() / 2.0F - lineHeight / 2),
            () -> TextAlignment.LEFT
    ),
    ABOVE_CROSSHAIR(
            res -> res.getScaledWidth() / 2,
            (res, lineHeight) -> (int) (res.getScaledHeight() / 2.0F - MARGIN - 3 - lineHeight),
            () -> TextAlignment.MIDDLE
    ),
    UNDER_CROSSHAIR_WITH_ATTACK_INDICATOR(
            res -> res.getScaledWidth() / 2,
            (res, lineHeight) -> (int) (res.getScaledHeight() / 2.0F + MARGIN + 4 + lineHeight),
            () -> TextAlignment.MIDDLE
    ),
    UNDER_CROSSHAIR(
            res -> res.getScaledWidth() / 2,
            (res, lineHeight) -> res.getScaledHeight() / 2 + MARGIN + 3,
            () -> TextAlignment.MIDDLE
    );

    private final XPosition xPosition;
    private final YPosition yPosition;
    private final Supplier<TextAlignment> textAlignmentGetter;

    PositionPreset(XPosition xPosition, YPosition yPosition, Supplier<TextAlignment> textAlignmentGetter) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.textAlignmentGetter = textAlignmentGetter;
    }

    public int posX(ScaledResolution res) {
        return xPosition.get(res);
    }

    public int posY(ScaledResolution res, FontRenderer font, float scale) {
        return yPosition.get(res, font.FONT_HEIGHT * scale);
    }

    public TextAlignment textAlignment() {
        return textAlignmentGetter.get();
    }

    @FunctionalInterface
    public interface XPosition {
        int get(ScaledResolution res);
    }

    @FunctionalInterface
    public interface YPosition {
        int get(ScaledResolution res, float lineHeight);
    }
}
