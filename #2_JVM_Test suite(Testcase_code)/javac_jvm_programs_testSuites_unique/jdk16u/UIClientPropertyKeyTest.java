

import java.awt.EventQueue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIClientPropertyKey;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import static javax.swing.UIManager.getInstalledLookAndFeels;


public final class UIClientPropertyKeyTest {

    private static Object key = new UIClientPropertyKey() {
    };

    public static void main(final String[] args) throws Exception {
        EventQueue.invokeAndWait(UIClientPropertyKeyTest::testSetUI);
        EventQueue.invokeAndWait(UIClientPropertyKeyTest::testSerialization);
    }

    
    private static void testSerialization() {
        JComponent comp = new JButton();
        comp.putClientProperty("key1", "value1");
        comp.putClientProperty(key, "value2");

        comp = serializeDeserialize(comp);

        validate(comp);
    }

    
    private static void testSetUI() {
        JComponent comp = new JButton();
        comp.putClientProperty("key1", "value1");
        for (final UIManager.LookAndFeelInfo laf : getInstalledLookAndFeels()) {
            comp.putClientProperty(key, "value2");
            setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(comp);
            validate(comp);
        }
    }

    private static void validate(JComponent comp) {
        Object value = comp.getClientProperty("key1");
        if (!value.equals("value1")) {
            throw new RuntimeException("Incorrect value: " + value);
        }
        value = comp.getClientProperty(key);
        if (value != null) {
            throw new RuntimeException("Incorrect value: " + value);
        }
    }

    private static void setLookAndFeel(final UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
            System.out.println("LookAndFeel: " + laf.getClassName());
        } catch (final UnsupportedLookAndFeelException ignored){
            System.out.println("Unsupported LookAndFeel: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static JComponent serializeDeserialize(JComponent comp) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(comp);
            out.close();
            return (JComponent) new ObjectInputStream(new ByteArrayInputStream(
                    byteOut.toByteArray())).readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
