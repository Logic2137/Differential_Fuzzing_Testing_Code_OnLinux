

import java.awt.Color;
import java.awt.Font;

import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class Test7022041 {

    public static void main(String[] args) throws Exception {
        UIManager.LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
        
        for (UIManager.LookAndFeelInfo lookAndFeel : installedLookAndFeels) {
            String name = lookAndFeel.getName();
            System.out.println("Testing " + name);
            
            
            try {
                UIManager.setLookAndFeel(lookAndFeel.getClassName());
                checkTitleColor();
                System.out.println("    titleColor test ok");
                checkTitleFont();
                System.out.println("    titleFont test ok");
            }
            catch (UnsupportedLookAndFeelException e) {
                System.out.println("    Note: LookAndFeel " + name
                                 + " is not supported on this configuration");
            }
        }
    }

    
    private static void checkTitleColor() {
        TitledBorder titledBorder = new TitledBorder(new EmptyBorder(1, 1, 1, 1));
        Color defaultColor = UIManager.getLookAndFeelDefaults().getColor("TitledBorder.titleColor");
        Color titledBorderColor = titledBorder.getTitleColor();

        
        if (defaultColor == null) {
            if (titledBorderColor == null) {
                return;
            }
            else {
                throw new RuntimeException("TitledBorder default color should be null");
            }
        }
        if (!defaultColor.equals(titledBorderColor)) {
            throw new RuntimeException("L&F default color " + defaultColor.toString()
                                     + " differs from TitledBorder color " + titledBorderColor.toString());
        }

        
        Color color = Color.green;
        titledBorder.setTitleColor(color);
        if (!color.equals(titledBorder.getTitleColor())) {
            throw new RuntimeException("TitledBorder color should be " + color.toString());
        }

        
        titledBorder.setTitleColor(null);
        if (!defaultColor.equals(titledBorder.getTitleColor())) {
            throw new RuntimeException("L&F default color " + defaultColor.toString()
                                     + " differs from TitledBorder color " + titledBorderColor.toString());
        }
    }

    
    private static void checkTitleFont() {
        TitledBorder titledBorder = new TitledBorder(new EmptyBorder(1, 1, 1, 1));
        Font defaultFont = UIManager.getLookAndFeelDefaults().getFont("TitledBorder.font");
        Font titledBorderFont = titledBorder.getTitleFont();

        
        if (defaultFont == null) {
            if (titledBorderFont == null) {
                return;
            }
            else {
                throw new RuntimeException("TitledBorder default font should be null");
            }
        }
        if (!defaultFont.equals(titledBorderFont)) {
            throw new RuntimeException("L&F default font " + defaultFont.toString()
                                     + " differs from TitledBorder font " + titledBorderFont.toString());
        }

        
        Font font = new Font("Dialog", Font.PLAIN, 10);
        titledBorder.setTitleFont(font);
        if (!font.equals(titledBorder.getTitleFont())) {
            throw new RuntimeException("TitledBorder font should be " + font.toString());
        }

        
        titledBorder.setTitleFont(null);
        if (!defaultFont.equals(titledBorder.getTitleFont())) {
            throw new RuntimeException("L&F default font " + defaultFont.toString()
                                     + " differs from TitledBorder font " + titledBorderFont.toString());
        }
    }
}

