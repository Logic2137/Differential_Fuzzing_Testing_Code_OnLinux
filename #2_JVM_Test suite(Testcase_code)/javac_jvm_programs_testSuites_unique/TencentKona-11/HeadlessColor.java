

import java.awt.*;
import java.awt.color.ColorSpace;



public class HeadlessColor {
    public static void main(String args[]) {
        Color c;

        
        c = new Color(1, 2, 3);
        c = new Color(1, 2, 3, 4);
        c = new Color((1 << 16) | (2 << 8) | (3));
        c = new Color((1 << 24) | (1 << 16) | (2 << 8) | (3));
        c = new Color((1 << 24) | (2 << 16) | (3 << 8) | (4), true);
        c = new Color((2 << 16) | (3 << 8) | (4), false);
        c = new Color(0.8f, 0.8f, 0.3f);
        c = new Color(0.999f, 0.8f, 0.8f, 0.3f);
        c = new Color(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new float[]{0.8f, 0.8f, 0.3f}, 1f);

        
        boolean exceptions = false;
        try {
            c = new Color(409, 400, 400);
        } catch (IllegalArgumentException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("Constructor did not throw IllegalArgumentException " +
                    "when expected in headless mode");

        exceptions = false;
        try {
            c = new Color(400, 3003, 400, 400);
        } catch (IllegalArgumentException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("Constructor did not throw IllegalArgumentException " +
                    "when expected in headless mode");

        exceptions = false;
        try {
            c = new Color(8f, 8f, 3f);
        } catch (IllegalArgumentException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("Constructor did not throw IllegalArgumentException " +
                    "when expected in headless mode");

        exceptions = false;
        try {
            c = new Color(-8f, -8f, -3f);
        } catch (IllegalArgumentException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("Constructor did not throw IllegalArgumentException " +
                    "when expected in headless mode");

        exceptions = false;
        try {
            c = new Color(0.999f, 8f, 8f, 3f);
        } catch (IllegalArgumentException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("Constructor did not throw IllegalArgumentException " +
                    "when expected in headless mode");

        exceptions = false;
        try {
            c = new Color(20f, 8f, 8f, 3f);
        } catch (IllegalArgumentException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("Constructor did not throw IllegalArgumentException " +
                    "when expected in headless mode");

        exceptions = false;
        try {
            c = new Color(-20f, -8f, -8f, -3f);
        } catch (IllegalArgumentException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("Constructor did not throw IllegalArgumentException " +
                    "when expected in headless mode");

        exceptions = false;
        try {
            c = new Color(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                    new float[]{-8f, -8f, -3f}, 1f);
        } catch (IllegalArgumentException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("Constructor did not throw IllegalArgumentException " +
                    "when expected in headless mode");

        exceptions = false;
        try {
            c = new Color(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                    new float[]{-8f, -8f, -3f}, -1f);
        } catch (IllegalArgumentException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("Constructor did not throw IllegalArgumentException " +
                    "when expected in headless mode");


        c = new Color(1, 2, 3);
        c.hashCode();
        c.toString();
        if (c.getRed() != 1)
            throw new RuntimeException("Incorrect red value");
        if (c.getGreen() != 2)
            throw new RuntimeException("Incorrect green value");
        if (c.getBlue() != 3)
            throw new RuntimeException("Incorrect bluevalue");
        if (c.getAlpha() != 255)
            throw new RuntimeException("Incorrect alpha value");
        if (c.getRGB() != ((255 << 24) | (1 << 16) | (2 << 8) | (3)))
            throw new RuntimeException("Incorrect rgb value");

        int rgb = c.getRGB();
        c.brighter();
        if (rgb != c.getRGB())
            throw new RuntimeException("Color object changed RGB value after brighter() called");

        rgb = c.getRGB();
        c.darker();
        if (rgb != c.getRGB())
            throw new RuntimeException("Color object changed RGB value after brighter() called");

        c = new Color(1, 2, 3, 4);
        c.hashCode();
        c.toString();
        if (c.getRed() != 1)
            throw new RuntimeException("Incorrect red value");
        if (c.getGreen() != 2)
            throw new RuntimeException("Incorrect green value");
        if (c.getBlue() != 3)
            throw new RuntimeException("Incorrect bluevalue");
        if (c.getAlpha() != 4)
            throw new RuntimeException("Incorrect alpha value");
        if (c.getRGB() != ((4 << 24) | (1 << 16) | (2 << 8) | (3)))
            throw new RuntimeException("Incorrect rgb value");

        rgb = c.getRGB();
        c.brighter();
        if (rgb != c.getRGB())
            throw new RuntimeException("Color object changed RGB value after brighter() called");

        rgb = c.getRGB();
        c.darker();
        if (rgb != c.getRGB())
            throw new RuntimeException("Color object changed RGB value after brighter() called");


        if (!(new Color(1, 2, 3).equals(new Color(1, 2, 3))))
            throw new RuntimeException("Inequality in colors when equality expected");
        if (new Color(1, 2, 3).equals(new Color(3, 2, 1)))
            throw new RuntimeException("Equality in colors when NO equality expected");

        if (!(new Color(1, 2, 3, 4).equals(new Color(1, 2, 3, 4))))
            throw new RuntimeException("Inequality in colors when equality expected");
        if (new Color(1, 2, 3, 4).equals(new Color(4, 3, 2, 1)))
            throw new RuntimeException("Equality in colors when NO equality expected");

        c = Color.decode("0xffffff");
        c = Color.getColor("65535");
        c = Color.getColor("65535", Color.black);
        c = Color.getColor("65535", 0xffffff);

        int hsb_value = Color.HSBtoRGB(0.1f, 0.2f, 0.3f);
        float[] rgb_value = Color.RGBtoHSB(1, 2, 3, null);

        c = Color.getHSBColor(0.3f, 0.4f, 0.6f);
        c = Color.getHSBColor(-0.3f, -0.4f, -0.6f);
        c = Color.getHSBColor(30, 40, 60);

        float[] comps;
        comps = Color.black.getRGBComponents(null);
        comps = Color.black.getRGBColorComponents(null);
        comps = Color.black.getComponents(null);
        comps = Color.black.getColorComponents(null);
        comps = Color.black.getComponents(ColorSpace.getInstance(ColorSpace.CS_sRGB), null);
        comps = Color.black.getColorComponents(ColorSpace.getInstance(ColorSpace.CS_sRGB), null);

        Color.black.getColorSpace();
        Color.black.getTransparency();
    }
}
