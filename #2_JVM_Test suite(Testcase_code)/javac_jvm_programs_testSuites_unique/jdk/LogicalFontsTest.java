



import java.awt.Font;

public class LogicalFontsTest {

    public static void main(String[] args) {
        test(Font.SANS_SERIF);
        test(Font.SERIF);
        test(Font.MONOSPACED);
        test(Font.DIALOG);
        test(Font.DIALOG_INPUT);
     }

     static void test(String fontName) {
         System.out.println("name="+fontName);
         Font font = new Font(fontName, Font.PLAIN, 12);
         System.out.println("font = " + font);
         if (!fontName.equalsIgnoreCase(font.getFamily())) {
             throw new RuntimeException("Requested " + fontName + " but got " + font);
         }
     }
}
