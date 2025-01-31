

import javax.swing.*;
import java.awt.*;
import java.util.Locale;



public class HeadlessJDesktopPane {
    public static void main(String args[]) {
        JComponent ch = new JComponent(){};
        ch.getAccessibleContext();
        ch.isFocusTraversable();
        ch.setEnabled(false);
        ch.setEnabled(true);
        ch.requestFocus();
        ch.requestFocusInWindow();
        ch.getPreferredSize();
        ch.getMaximumSize();
        ch.getMinimumSize();
        ch.contains(1, 2);
        Component c1 = ch.add(new Component(){});
        Component c2 = ch.add(new Component(){});
        Component c3 = ch.add(new Component(){});
        Insets ins = ch.getInsets();
        ch.getAlignmentY();
        ch.getAlignmentX();
        ch.getGraphics();
        ch.setVisible(false);
        ch.setVisible(true);
        ch.setForeground(Color.red);
        ch.setBackground(Color.red);
        for (String font : Toolkit.getDefaultToolkit().getFontList()) {
            for (int j = 8; j < 17; j++) {
                Font f1 = new Font(font, Font.PLAIN, j);
                Font f2 = new Font(font, Font.BOLD, j);
                Font f3 = new Font(font, Font.ITALIC, j);
                Font f4 = new Font(font, Font.BOLD | Font.ITALIC, j);

                ch.setFont(f1);
                ch.setFont(f2);
                ch.setFont(f3);
                ch.setFont(f4);

                ch.getFontMetrics(f1);
                ch.getFontMetrics(f2);
                ch.getFontMetrics(f3);
                ch.getFontMetrics(f4);
            }
        }
        ch.enable();
        ch.disable();
        ch.reshape(10, 10, 10, 10);
        ch.getBounds(new Rectangle(1, 1, 1, 1));
        ch.getSize(new Dimension(1, 2));
        ch.getLocation(new Point(1, 2));
        ch.getX();
        ch.getY();
        ch.getWidth();
        ch.getHeight();
        ch.isOpaque();
        ch.isValidateRoot();
        ch.isOptimizedDrawingEnabled();
        ch.isDoubleBuffered();
        ch.getComponentCount();
        ch.countComponents();
        ch.getComponent(1);
        ch.getComponent(2);
        Component[] cs = ch.getComponents();
        ch.getLayout();
        ch.setLayout(new FlowLayout());
        ch.doLayout();
        ch.layout();
        ch.invalidate();
        ch.validate();
        ch.remove(0);
        ch.remove(c2);
        ch.removeAll();
        ch.preferredSize();
        ch.minimumSize();
        ch.getComponentAt(1, 2);
        ch.locate(1, 2);
        ch.getComponentAt(new Point(1, 2));
        ch.isFocusCycleRoot(new Container());
        ch.transferFocusBackward();
        ch.setName("goober");
        ch.getName();
        ch.getParent();
        ch.getGraphicsConfiguration();
        ch.getTreeLock();
        ch.getToolkit();
        ch.isValid();
        ch.isDisplayable();
        ch.isVisible();
        ch.isShowing();
        ch.isEnabled();
        ch.enable(false);
        ch.enable(true);
        ch.enableInputMethods(false);
        ch.enableInputMethods(true);
        ch.show();
        ch.show(false);
        ch.show(true);
        ch.hide();
        ch.getForeground();
        ch.isForegroundSet();
        ch.getBackground();
        ch.isBackgroundSet();
        ch.getFont();
        ch.isFontSet();
        Container c = new Container();
        c.add(ch);
        ch.getLocale();
        for (Locale locale : Locale.getAvailableLocales())
            ch.setLocale(locale);

        ch.getColorModel();
        ch.getLocation();

        boolean exceptions = false;
        try {
            ch.getLocationOnScreen();
        } catch (IllegalComponentStateException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("IllegalComponentStateException did not occur when expected");

        ch.location();
        ch.setLocation(1, 2);
        ch.move(1, 2);
        ch.setLocation(new Point(1, 2));
        ch.getSize();
        ch.size();
        ch.setSize(1, 32);
        ch.resize(1, 32);
        ch.setSize(new Dimension(1, 32));
        ch.resize(new Dimension(1, 32));
        ch.getBounds();
        ch.bounds();
        ch.setBounds(10, 10, 10, 10);
        ch.setBounds(new Rectangle(10, 10, 10, 10));
        ch.isLightweight();
        ch.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        ch.getCursor();
        ch.isCursorSet();
        ch.inside(1, 2);
        ch.contains(new Point(1, 2));
        ch.isFocusable();
        ch.setFocusable(true);
        ch.setFocusable(false);
        ch.transferFocus();
        ch.getFocusCycleRootAncestor();
        ch.nextFocus();
        ch.transferFocusUpCycle();
        ch.hasFocus();
        ch.isFocusOwner();
        ch.toString();
        ch.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        ch.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        ch.setComponentOrientation(ComponentOrientation.UNKNOWN);
        ch.getComponentOrientation();
    }
}
