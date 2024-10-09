



import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;

import sun.font.Font2D;
import sun.font.SunFontManager;

public class DefaultFontTest {
    public static void main(String[] args) throws Exception {
        
        String[] fontFamilyNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        if (fontFamilyNames != null) {
            System.out.println("FontFamilies:");
            Arrays.asList(fontFamilyNames).stream().forEach((fontname)->System.out.println(fontname));
        }
        SunFontManager sfm = SunFontManager.getInstance();
        Font[] createdFonts = sfm.getCreatedFonts();
        if (createdFonts != null) {
            System.out.println("\nCreated Fonts:");
            Arrays.asList(createdFonts).stream().forEach((font)->System.out.println(font));
        }
        Font2D[] registeredFonts = sfm.getRegisteredFonts();
        if (registeredFonts != null) {
            System.out.println("\nRegistered Fonts:");
            Arrays.asList(registeredFonts).stream().forEach((font)->System.out.println(font));
        }
        System.out.println("\nDefault physical font: " + sfm.getDefaultPhysicalFont());
    }
}
