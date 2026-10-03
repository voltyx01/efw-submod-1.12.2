package com.voltyx.mwccf.darkmode;

import java.awt.image.BufferedImage;

/**
 * Procedural dark theme filter with sharp corners and color retention.
 */
public class DarkGuiFilter {

    private static final int[] LUT = new int[256];

    static {
        for (int v = 0; v <= 255; v++) {
            if (v <= 20) {
                LUT[v] = Math.max(0, (int) (v * 0.5));
            } else if (v <= 70) {
                // Shadow borders and slot shadows (vanilla 55 -> #0D0D0D / 13)
                float t = (v - 20.0f) / (70.0f - 20.0f);
                LUT[v] = (int) (10 + t * 4); // 10..14
            } else if (v <= 165) {
                // Slot interiors (vanilla 139 -> #1A1A1A / 26)
                float t = (v - 70.0f) / (165.0f - 70.0f);
                LUT[v] = (int) (15 + t * 14); // 15..29
            } else if (v <= 225) {
                // Container background (vanilla 198 -> #292929 / 41)
                float t = (v - 165.0f) / (225.0f - 165.0f);
                LUT[v] = (int) (32 + t * 13); // 32..45
            } else {
                // Light highlights (vanilla 255 -> #404040 / 64)
                float t = (v - 225.0f) / (255.0f - 225.0f);
                LUT[v] = (int) (48 + t * 16); // 48..64
            }
        }
    }

    public static BufferedImage transform(BufferedImage src) {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        int[] pixels = new int[w * h];
        src.getRGB(0, 0, w, h, pixels, 0, w);
        int[] outPixels = new int[w * h];

        for (int y = 0; y < h; y++) {
            int rowOffset = y * w;
            for (int x = 0; x < w; x++) {
                int p = pixels[rowOffset + x];
                int a = (p >> 24) & 0xFF;

                if (a < 10) {
                    // Sharp corners (fills transparent 1px corner notches even at texture edges x=0 or y=0)
                    boolean hasRight = (x + 1 < w) && (((pixels[rowOffset + x + 1] >> 24) & 0xFF) > 200);
                    boolean hasLeft  = (x - 1 >= 0) && (((pixels[rowOffset + x - 1] >> 24) & 0xFF) > 200);
                    boolean hasDown  = (y + 1 < h) && (((pixels[(y + 1) * w + x] >> 24) & 0xFF) > 200);
                    boolean hasUp    = (y - 1 >= 0) && (((pixels[(y - 1) * w + x] >> 24) & 0xFF) > 200);

                    if ((hasRight && hasDown) || (hasLeft && hasDown) || (hasRight && hasUp) || (hasLeft && hasUp)) {
                        outPixels[rowOffset + x] = 0xFF000000;
                        continue;
                    }

                    outPixels[rowOffset + x] = 0;
                    continue;
                }

                int r = (p >> 16) & 0xFF;
                int g = (p >> 8) & 0xFF;
                int b = p & 0xFF;

                int max = Math.max(r, Math.max(g, b));
                int min = Math.min(r, Math.min(g, b));
                int diff = max - min;

                // If pixel is colored (fire, mana, RF energy, fluid, redstone, colored icons) or bright highlight in sprite sheet, preserve it!
                if (diff > 18 || (x >= 176 && max > 240)) {
                    outPixels[rowOffset + x] = p;
                } else {
                    int v = (r + g + b) / 3;
                    int newV = LUT[v];
                    outPixels[rowOffset + x] = (a << 24) | (newV << 16) | (newV << 8) | newV;
                }
            }
        }

        dst.setRGB(0, 0, w, h, outPixels, 0, w);
        return dst;
    }
}
