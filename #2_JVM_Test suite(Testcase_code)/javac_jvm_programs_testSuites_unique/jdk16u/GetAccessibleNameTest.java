

import javax.accessibility.AccessibleContext;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;



public class GetAccessibleNameTest {

    public static void main(String[] args) throws Exception {
        testConstructor();
        testSetText();
        testAccessibleProperty();
    }

    private static void testConstructor() {
        Class[] testClass = new Class[] {
            JLabel.class, JButton.class, JMenuItem.class,
            JMenu.class, JCheckBoxMenuItem.class, JRadioButtonMenuItem.class,
            JToggleButton.class, JRadioButton.class, JCheckBox.class };

        Class[] ctorArg = new Class[1];
        ctorArg[0] = String.class;
        String expectedText = "bold italic em mark del small big sup sub ins strong code strike";
        String inputText = "<html><style>{color:#FF0000;}</style><body>" +
                   "<b>bold</b> <i>italic</i> <em>em</em> <mark>mark</mark> <del>del</del> " +
                   "<small>small</small> <big>big</big> <sup>sup</sup> <sub>sub</sub> <ins>ins</ins> " +
                   "<strong>strong</strong> <code>code</code> <strike>strike</strike>" +
                   "</body></html>";

        for (Class aClass : testClass) {
            try {
                Constructor constructor = aClass.getDeclaredConstructor(ctorArg);
                JComponent comp = (JComponent) constructor.newInstance(inputText);
                if (!expectedText.equals(comp.getAccessibleContext().getAccessibleName())) {
                    throw new RuntimeException("AccessibleName of " + aClass.getName() + " is incorrect." +
                                       " Expected: " + expectedText +
                                       " Actual: " + comp.getAccessibleContext().getAccessibleName());
                }
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(aClass.getName() + " does not have a constructor accepting" +
                    "String parameter.", e.getCause());
            } catch (InstantiationException e) {
                throw new RuntimeException(aClass.getName() + " could not be instantiated.",
                    e.getCause());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(aClass.getName() + " constructor cannot be accessed.",
                    e.getCause());
            } catch (InvocationTargetException e) {
                throw new RuntimeException(aClass.getName() + " constructor cannot be invoked.",
                    e.getCause());
            }
        }
    }

    private static void testSetText() {
        String text = "html text";
        JLabel testLabel = new JLabel("<html>" + text + "</html>");
        if (!text.equals(testLabel.getAccessibleContext().getAccessibleName())) {
            throw new RuntimeException("Incorrect AccessibleName," +
                " Expected: " + text +
                " Actual: " + testLabel.getAccessibleContext().getAccessibleName());
        }

        text = "Non html text";
        testLabel.setText(text);
        if (!text.equals(testLabel.getAccessibleContext().getAccessibleName())) {
            throw new RuntimeException("Incorrect AccessibleName," +
                " Expected: " + text +
                " Actual: " + testLabel.getAccessibleContext().getAccessibleName());
        }
    }

    private static void testAccessibleProperty() {
        String text = "html text";
        JLabel testLabel = new JLabel("<html>" + text + "</html>");
        if (!text.equals(testLabel.getClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY))) {
            throw new RuntimeException("Incorrect ACCESSIBLE_NAME_PROPERTY," +
                " Expected: " + text +
                " Actual: " + testLabel.getClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY));
        }

        String namePropertyText = "name property";
        testLabel.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, namePropertyText);
        if (!namePropertyText.equals(testLabel.getClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY))) {
            throw new RuntimeException("Incorrect ACCESSIBLE_NAME_PROPERTY," +
                " Expected: " + namePropertyText +
                " Actual: " + testLabel.getClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY));
        }

        text = "different html text";
        testLabel.setText("<html>" + text + "</html>");
        if (!namePropertyText.equals(testLabel.getClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY))) {
            throw new RuntimeException("Incorrect ACCESSIBLE_NAME_PROPERTY," +
                " Expected: " + namePropertyText +
                " Actual: " + testLabel.getClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY));
        }
    }
}
