



import java.awt.Font;
import java.io.File;



public class AppleFontNameTest {

    static String file = "/System/Library/Fonts/Menlo.ttc";

    public static void main(String[] args) throws Exception {
        String os = System.getProperty("os.name");
        if (!(os.startsWith("Mac"))) {
            return;
        }
        File fontFile = new File(file);
        if (!fontFile.exists()) {
            return;
        }
        Font[] fonts = Font.createFonts(new File(file));
        System.out.println("createFont from file returned " + fonts);

        if (fonts == null || fonts.length == 0) {
            throw new RuntimeException("No fonts");
        }
        for (Font f : fonts) {
            System.out.println(f);
            if (!f.getFamily().equals("Menlo"))
               throw new RuntimeException("Expected Menlo, got " + f.getFamily());
        }
    }
}
