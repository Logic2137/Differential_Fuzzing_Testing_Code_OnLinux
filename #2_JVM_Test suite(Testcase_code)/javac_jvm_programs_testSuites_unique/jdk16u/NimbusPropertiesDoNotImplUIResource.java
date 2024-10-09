

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.Color;



public class NimbusPropertiesDoNotImplUIResource {
    private static final String[] defPropertyKeys = new String[] {
            "Tree.leafIcon", "Tree.closedIcon",
            "Tree.openIcon", "Tree.selectionForeground",
            "Tree.textForeground", "Tree.selectionBackground",
            "Tree.textBackground", "Tree.selectionBorderColor"};
    private static String failedKeys = null;

    public static void main(String[] args) throws Exception {
        UIManager.LookAndFeelInfo[] installedLookAndFeels;
        installedLookAndFeels = UIManager.getInstalledLookAndFeels();

        for (UIManager.LookAndFeelInfo LF : installedLookAndFeels) {
            try {
                UIManager.setLookAndFeel(LF.getClassName());
                failedKeys = null;
                for (String propertyKey : defPropertyKeys) {
                    verifyProperty(propertyKey);
                }
                if(failedKeys != null) {
                    throw new RuntimeException("JTree renderer Properties " +
                            failedKeys + " are not instance of UIResource for "
                            + LF.getClassName());
                }
            } catch(UnsupportedLookAndFeelException e) {
                System.out.println("Note: LookAndFeel " + LF.getClassName()
                        + " is not supported on this configuration");
            }
        }

        
        
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        Color color1 = ((NimbusLookAndFeel)UIManager.getLookAndFeel())
                .getDerivedColor("text", 0, 0, 0, 0, false);
        if(color1 instanceof UIResource) {
            throw new RuntimeException("color1 should not be instance of " +
                    "UIResource");
        }

        Color color2 = ((NimbusLookAndFeel)UIManager.getLookAndFeel())
                .getDerivedColor("text", 0, 0, 0, 0, true);
        if(!(color2 instanceof UIResource)) {
            throw new RuntimeException("color2 should be instance of " +
                    "UIResource");
        }

    }

    private static void verifyProperty(String propertyKey) {
        Object property = UIManager.get(propertyKey);
        if (property == null) {
            return;
        }
        if (!(property instanceof UIResource)) {
            if(failedKeys == null) {
                failedKeys = ":" + propertyKey;
            } else {
                failedKeys += "," + propertyKey;
            }
        }
    }
}
