

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class ContextMenuScrollTest extends JPopupMenu
{
    private static Robot robot;
    private static JFrame frame;
    private static JMenu menu;
    private static volatile Point p = null;
    private static volatile Dimension d = null;
    private static volatile boolean popupVisible = false;

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        robot.setAutoDelay(100);
        try {
            SwingUtilities.invokeAndWait(()->createGUI());
            robot.waitForIdle();
            robot.delay(1000);

            SwingUtilities.invokeAndWait(() -> {
                p = menu.getLocationOnScreen();
                d = menu.getSize();
            });
            System.out.println("p " + p + " d " + d);
            robot.mouseMove(p.x + d.width/2, p.y + d.height/2);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.waitForIdle();
            robot.delay(1000);

            robot.mouseWheel(1);
            robot.waitForIdle();

            SwingUtilities.invokeAndWait(() -> {
                popupVisible = menu.isPopupMenuVisible();
            });
            if (!popupVisible) {
                throw new RuntimeException("Popup closes on mouse scroll");
            }
        } finally {
            SwingUtilities.invokeAndWait(()->frame.dispose());
        }
    }


    public static void createGUI() {
        frame = new JFrame();
        JMenuBar menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        menuBar.add(menu);

        JMenuItem undo = new JMenuItem("Undo");
        undo.setEnabled(false);
        undo.setAccelerator(KeyStroke.getKeyStroke("control Z"));
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            }
        });

        menu.add(undo);

        JMenuItem redo = new JMenuItem("Redo");
        redo.setEnabled(false);
        redo.setAccelerator(KeyStroke.getKeyStroke("control Y"));
        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            }
        });
        menu.add(redo);

        menu.add(new JSeparator());

        JMenuItem cut = new JMenuItem("Cut");
        cut.setEnabled(false);
        cut.setAccelerator(KeyStroke.getKeyStroke("control X"));
        cut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            }
        });

        menu.add(cut);

        JMenuItem copy = new JMenuItem("Copy");
        copy.setEnabled(false);
        copy.setAccelerator(KeyStroke.getKeyStroke("control C"));
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            }
        });

        menu.add(copy);

        JMenuItem paste = new JMenuItem("Paste");
        paste.setEnabled(false);
        paste.setAccelerator(KeyStroke.getKeyStroke("control V"));
        paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            }
        });

        menu.add(paste);

        JMenuItem delete = new JMenuItem("Delete");
        delete.setEnabled(false);
        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            }
        });

        menu.add(delete);

        menu.add(new JSeparator());

        JMenuItem selectAll = new JMenuItem("Select All");
        selectAll.setEnabled(false);
        selectAll.setAccelerator(KeyStroke.getKeyStroke("control A"));
        selectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            }
        });
        menu.add(selectAll);
        frame.setJMenuBar(menuBar);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
