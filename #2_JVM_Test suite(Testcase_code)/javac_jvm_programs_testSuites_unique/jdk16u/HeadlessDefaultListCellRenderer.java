

import javax.swing.*;
import java.awt.*;
import java.util.Locale;



public class HeadlessDefaultListCellRenderer {
    public static void main(String args[]) {
        DefaultListCellRenderer dcr = new DefaultListCellRenderer();
        Component c1 = dcr.add(new Component(){});
        Component c2 = dcr.add(new Component(){});
        Component c3 = dcr.add(new Component(){});
        dcr.setLayout(new FlowLayout());
        dcr.invalidate();
        dcr.validate();
        dcr.getAccessibleContext();
        dcr.requestFocus();
        dcr.requestFocusInWindow();
        dcr.getPreferredSize();
        dcr.getMaximumSize();
        dcr.getMinimumSize();
        dcr.contains(1, 2);
        Insets ins = dcr.getInsets();
        dcr.getAlignmentY();
        dcr.getAlignmentX();
        dcr.getGraphics();
        dcr.setVisible(false);
        dcr.setVisible(true);
        dcr.setEnabled(false);
        dcr.setEnabled(true);
        dcr.setForeground(Color.red);
        dcr.setBackground(Color.red);
        for (String font : Toolkit.getDefaultToolkit().getFontList()) {
            for (int j = 8; j < 17; j++) {
                Font f1 = new Font(font, Font.PLAIN, j);
                Font f2 = new Font(font, Font.BOLD, j);
                Font f3 = new Font(font, Font.ITALIC, j);
                Font f4 = new Font(font, Font.BOLD | Font.ITALIC, j);

                dcr.setFont(f1);
                dcr.setFont(f2);
                dcr.setFont(f3);
                dcr.setFont(f4);

                dcr.getFontMetrics(f1);
                dcr.getFontMetrics(f2);
                dcr.getFontMetrics(f3);
                dcr.getFontMetrics(f4);
            }
        }
        dcr.enable();
        dcr.disable();
        dcr.reshape(10, 10, 10, 10);
        dcr.getBounds(new Rectangle(1, 1, 1, 1));
        dcr.getSize(new Dimension(1, 2));
        dcr.getLocation(new Point(1, 2));
        dcr.getX();
        dcr.getY();
        dcr.getWidth();
        dcr.getHeight();
        dcr.isOpaque();
        dcr.isValidateRoot();
        dcr.isOptimizedDrawingEnabled();
        dcr.isDoubleBuffered();
        dcr.getComponentCount();
        dcr.countComponents();
        dcr.getComponent(1);
        dcr.getComponent(2);
        Component[] cs = dcr.getComponents();
        ins = dcr.insets();
        dcr.remove(0);
        dcr.remove((java.awt.Component) c2);
        dcr.removeAll();
        dcr.getLayout();
        dcr.setLayout(new FlowLayout());
        dcr.doLayout();
        dcr.layout();
        dcr.invalidate();
        dcr.validate();
        dcr.revalidate();
        dcr.preferredSize();
        dcr.minimumSize();
        dcr.getComponentAt(1, 2);
        dcr.locate(1, 2);
        dcr.getComponentAt(new Point(1, 2));
        dcr.isFocusCycleRoot(new Container());
        dcr.transferFocusBackward();
        dcr.setName("goober");
        dcr.getName();
        dcr.getParent();
        dcr.getGraphicsConfiguration();
        dcr.getTreeLock();
        dcr.getToolkit();
        dcr.isValid();
        dcr.isDisplayable();
        dcr.isVisible();
        dcr.isShowing();
        dcr.isEnabled();
        dcr.enable(false);
        dcr.enable(true);
        dcr.enableInputMethods(false);
        dcr.enableInputMethods(true);
        dcr.show();
        dcr.show(false);
        dcr.show(true);
        dcr.hide();
        dcr.getForeground();
        dcr.isForegroundSet();
        dcr.getBackground();
        dcr.isBackgroundSet();
        dcr.getFont();
        dcr.isFontSet();

        Container c = new Container();
        c.add(dcr);
        dcr.getLocale();

        for (Locale locale : Locale.getAvailableLocales())
            dcr.setLocale(locale);

        dcr.getColorModel();
        dcr.getLocation();

        boolean exceptions = false;
        try {
            dcr.getLocationOnScreen();
        } catch (IllegalComponentStateException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("IllegalComponentStateException did not occur when expected");

        dcr.location();
        dcr.setLocation(1, 2);
        dcr.move(1, 2);
        dcr.setLocation(new Point(1, 2));
        dcr.getSize();
        dcr.size();
        dcr.setSize(1, 32);
        dcr.resize(1, 32);
        dcr.setSize(new Dimension(1, 32));
        dcr.resize(new Dimension(1, 32));
        dcr.getBounds();
        dcr.bounds();
        dcr.setBounds(10, 10, 10, 10);
        dcr.setBounds(new Rectangle(10, 10, 10, 10));
        dcr.isLightweight();
        dcr.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        dcr.getCursor();
        dcr.isCursorSet();
        dcr.inside(1, 2);
        dcr.contains(new Point(1, 2));
        dcr.isFocusTraversable();
        dcr.isFocusable();
        dcr.setFocusable(true);
        dcr.setFocusable(false);
        dcr.transferFocus();
        dcr.getFocusCycleRootAncestor();
        dcr.nextFocus();
        dcr.transferFocusUpCycle();
        dcr.hasFocus();
        dcr.isFocusOwner();
        dcr.toString();
        dcr.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        dcr.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        dcr.setComponentOrientation(ComponentOrientation.UNKNOWN);
        dcr.getComponentOrientation();
    }
}
