package com.voltyx.mwccf;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class TextureCreator {
    public static void main(String[] args) throws Exception {
        File outDir = new File("src/main/resources/assets/weaponlib/com/paneedah/weaponlib/resources");
        outDir.mkdirs();

        BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(15, 15, 15, 255));
        g.fillRect(0, 0, 32, 32);
        g.dispose();

        File targetFile = new File(outDir, "dark-screen.png");
        ImageIO.write(img, "png", targetFile);

        // Also create static.png if needed by WirelessCameraPerspective
        File staticFile = new File(outDir, "static.png");
        if (!staticFile.exists()) {
            ImageIO.write(img, "png", staticFile);
        }

        System.out.println("Created dark-screen.png and static.png in " + outDir.getAbsolutePath());
    }
}
