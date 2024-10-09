

import java.awt.EventQueue;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;


public class InternalResourceBundle {

    public static void main(final String[] args) throws Exception {
        EventQueue.invokeAndWait(() -> {
            
            
            try {
                UIManager.setLookAndFeel(new NimbusLookAndFeel());
            } catch (final UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            }
            UIDefaults defaults = UIManager.getDefaults();
            
            
            defaults.addResourceBundle("com.sun.swing.internal.plaf.metal.resources.metal");

            Object value = getValue(defaults);
            if (value != null) {
                throw new RuntimeException("value is not null = " + value);
            }

            
            try {
                UIManager.setLookAndFeel(new MetalLookAndFeel());
            } catch (final UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            }
            value = getValue(defaults);
            if (value == null) {
                throw new RuntimeException("value is null");
            }
        });
    }

    private static Object getValue(UIDefaults defaults) {
        return defaults.get("MetalTitlePane.restore.titleAndMnemonic");
    }
}
