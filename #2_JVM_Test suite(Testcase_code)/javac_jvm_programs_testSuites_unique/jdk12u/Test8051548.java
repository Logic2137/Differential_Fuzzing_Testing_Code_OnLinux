

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.function.Predicate;
import javax.swing.JColorChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.colorchooser.AbstractColorChooserPanel;



public class Test8051548 {

    private static final String[][] TABS = {
        {"HSV", "0"},
        {"HSL", "0"},
        {"RGB", "255"},
        {"CMYK", "255"}
    };

    private static JColorChooser colorChooser;
    private static boolean propertyChangeListenerInvoked;
    private static volatile Color color;

    public static void main(String[] args) throws Exception {
        testColorPanels();
        testShowDialog(true);
        testShowDialog(false);
    }

    private static void testColorPanels() throws Exception {
        SwingUtilities.invokeAndWait(() -> createAndShowGUI());

        Robot robot = new Robot();
        robot.setAutoDelay(50);
        robot.waitForIdle();

        for (String[] tabs : TABS) {
            final String tab = tabs[0];
            final String initialValue = tabs[1];

            SwingUtilities.invokeAndWait(() -> {

                colorChooser.setColor(new Color(50, 100, 85));
                JTabbedPane tabbedPane =
                        (JTabbedPane) findComponent(colorChooser, "JTabbedPane");
                int index = tabbedPane.indexOfTab(tab);
                tabbedPane.setSelectedIndex(index);

                AbstractColorChooserPanel colorChooserPanel
                        = (AbstractColorChooserPanel) findComponent(
                                tabbedPane.getComponent(index), "ColorChooserPanel");

                propertyChangeListenerInvoked = false;
                colorChooserPanel.addPropertyChangeListener((e) -> {
                    if (AbstractColorChooserPanel.TRANSPARENCY_ENABLED_PROPERTY.
                            equals(e.getPropertyName())) {
                        propertyChangeListenerInvoked = true;
                        if(!(Boolean)e.getOldValue()){
                            throw new RuntimeException("Old color transparency"
                                    + " selection property should be true!");
                        }
                        if((Boolean)e.getNewValue()){
                            throw new RuntimeException("New color transparency"
                                    + " selection property should be false!");
                        }
                    }
                });

                if (!colorChooserPanel.isColorTransparencySelectionEnabled()) {
                    throw new RuntimeException("Color transparency selection"
                            + " should be enabled by default");
                }

                JFormattedTextField transparencyTextField = (JFormattedTextField)
                        findTextField(colorChooserPanel, initialValue);

                if (!transparencyTextField.isEnabled()) {
                    throw new RuntimeException("Transparency controls are"
                            + " disabled by default!");
                }

                transparencyTextField.setValue(50);

                if(!colorHasAlpha()){
                    throw new RuntimeException("Transparency selection should"
                            + " be enabled!");
                }

                colorChooserPanel.setColorTransparencySelectionEnabled(false);

                if (colorChooserPanel.isColorTransparencySelectionEnabled()) {
                    throw new RuntimeException("Color transparency selection"
                            + " should be disabled!");
                }

                if(!propertyChangeListenerInvoked){
                    throw new RuntimeException("Property change listener is not"
                            + " invoked!");
                }

                if(colorHasAlpha()){
                    throw new RuntimeException("Transparency selection should"
                            + " be disabled!");
                }
            });

            robot.waitForIdle();
        }

    }

    static void testShowDialog(boolean colorTransparencySelectionEnabled) throws Exception {
        int alphaValue = 123;
        Robot robot = new Robot();
        robot.setAutoDelay(50);

        SwingUtilities.invokeLater(() -> {
            color = JColorChooser.showDialog(null, "Change Color",
                    new Color(10, 20, 30, alphaValue),
                    colorTransparencySelectionEnabled);
        });

        SwingUtilities.invokeAndWait(() -> {
            
        });

        robot.waitForIdle();

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.waitForIdle();

        if (colorTransparencySelectionEnabled) {
            if (color.getAlpha() != alphaValue) {
                throw new RuntimeException("Color alpha has not bee reseted!");
            }
        } else {
            if (color.getAlpha() != 255) {
                throw new RuntimeException("Color alpha has not bee reseted!");
            }
        }
    }

    private static boolean colorHasAlpha(){
        return colorChooser.getColor().getAlpha() != 255;
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame();
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        colorChooser = new JColorChooser();
        frame.getContentPane().add(colorChooser);
        frame.setVisible(true);
    }

    private static Component findComponent(Component component, String name) {
        return findComponent(component,
                (comp) -> comp.getClass().getName().contains(name));
    }

    private static Component findTextField(Component component, String value) {
        return findComponent(component, (comp) -> {

            if (comp instanceof JFormattedTextField) {
                JFormattedTextField textField = (JFormattedTextField) comp;
                return value.equals(textField.getText());
            }
            return false;
        });
    }

    private static Component findComponent(Component component,
            Predicate<Component> predicate) {

        if (predicate.test(component)) {
            return component;
        }

        if (component instanceof Container) {
            Container container = (Container) component;
            for (int i = 0; i < container.getComponentCount(); i++) {
                Component child = findComponent(container.getComponent(i),
                        predicate);
                if (child != null) {
                    return child;
                }
            }
        }

        return null;
    }
}
