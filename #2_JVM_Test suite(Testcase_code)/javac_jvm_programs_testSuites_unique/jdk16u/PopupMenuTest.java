


import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicPopupMenuUI;

public class PopupMenuTest {

    private JPopupMenu jpopup;
    private static volatile boolean isLightWeight;
    private static JFrame frame;
    private static Robot robot;
    private static JPanel panel;

    public static void main(String s[]) throws Exception {
        PopupMenuTest obj = new PopupMenuTest();
        obj.createUI();
        robot = new Robot();
        robot.waitForIdle();
        robot.delay(1000);
        obj.exectuteTest();
        obj.dispose();
        if (isLightWeight) {
            throw new RuntimeException("Test Failed");
        }
    }

    private void createUI() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            frame = new JFrame("Popup Menu");
            jpopup = new JPopupMenu();
            jpopup.setUI(new PopMenuUIExt());
            JMenuItem item = new JMenuItem("Menu Item1");
            jpopup.add(item);
            item = new JMenuItem("Menu Item2");
            jpopup.setLabel("Justification");
            jpopup.add(item);
            jpopup.setLabel("Justification");
            jpopup.addPopupMenuListener(new PopupListener());
            panel = new JPanel();
            panel.addMouseListener(new MousePopupListener());
            frame.setContentPane(panel);
            frame.setSize(300, 300);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

    }

    private void dispose() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Popup popup = PopMenuUIExt.getPopup();
            if (popup != null) {
                popup.hide();
            }
            frame.dispose();
        });
    }

    private void exectuteTest() {
        Point p = frame.getLocationOnScreen();
        Rectangle rect = frame.getBounds();
        robot.mouseMove(p.x + rect.width / 2, p.y + rect.height / 2);
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        robot.delay(1000);
        robot.mouseMove(p.x + rect.width / 2 - 10, p.y + rect.height / 2 - 10);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(1000);
    }

    class MousePopupListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            showPopup(e);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            showPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            showPopup(e);
        }

        private void showPopup(MouseEvent e) {
            jpopup.show(panel, e.getX(), e.getY());
        }
    }

    class PopupListener implements PopupMenuListener {

        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            Popup popup = ((PopMenuUIExt) jpopup.getUI()).getPopup();
            if (popup != null) {
                isLightWeight = !popup.getClass().toString().
                        contains("HeavyWeightPopup");
            }
        }

        public void popupMenuCanceled(PopupMenuEvent e) {
        }
    }
}

class PopMenuUIExt extends BasicPopupMenuUI {

    private static Popup popUp;

    @Override
    public Popup getPopup(JPopupMenu popup, int x, int y) {
        PopupFactory.setSharedInstance(new PopupFactory() {

            @Override
            public Popup getPopup(Component owner, Component contents,
                    int x, int y) {
                return super.getPopup(owner, contents, x, y, true);
            }
        });
        PopupFactory factory = PopupFactory.getSharedInstance();
        popUp = factory.getPopup(popup.getInvoker(), popup, x, y);
        return popUp;
    }

    public static Popup getPopup() {
        return popUp;
    }
}

