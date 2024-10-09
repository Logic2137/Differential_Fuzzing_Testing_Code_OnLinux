

import java.awt.Toolkit;



public class CheckFontManagerSystemProperty {

    public static void main(String[] args) {
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        if (toolkit == null) {
            throw new RuntimeException("Toolkit not found!");
        }
        String tkProp = System.getProperty("sun.font.fontmanager");
        if (tkProp != null) {
            throw new RuntimeException("tkProp = " + tkProp);
        }
    }
}
