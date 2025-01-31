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

public class ContextMenuScrollTest extends JPopupMenu {

    private JMenuItem undo;

    private JMenuItem redo;

    private JMenuItem cut;

    private JMenuItem copy;

    private JMenuItem paste;

    private JMenuItem delete;

    private JMenuItem selectAll;

    private final Robot robot;

    private JFrame frame;

    private JMenuBar menuBar;

    private JMenu menu;

    private volatile Point p = null;

    private volatile Dimension d = null;

    public static void main(String[] args) throws Exception {
        new ContextMenuScrollTest();
    }

    void blockTillDisplayed(JComponent comp) throws Exception {
        while (p == null) {
            try {
                SwingUtilities.invokeAndWait(() -> {
                    p = comp.getLocationOnScreen();
                    d = menu.getSize();
                });
            } catch (IllegalStateException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    public ContextMenuScrollTest() throws Exception {
        robot = new Robot();
        robot.setAutoDelay(200);
        try {
            SwingUtilities.invokeAndWait(() -> createGUI());
            blockTillDisplayed(menu);
            robot.waitForIdle();
            robot.mouseMove(p.x + d.width / 2, p.y + d.height / 2);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            robot.waitForIdle();
            System.out.println("popmenu visible " + menu.isPopupMenuVisible());
            robot.mouseWheel(1);
            robot.waitForIdle();
            System.out.println("popmenu visible " + menu.isPopupMenuVisible());
            if (!menu.isPopupMenuVisible()) {
                throw new RuntimeException("Popup closes on mouse scroll");
            }
        } finally {
            SwingUtilities.invokeAndWait(() -> frame.dispose());
        }
    }

    public void createGUI() {
        frame = new JFrame();
        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        menuBar.add(menu);
        undo = new JMenuItem("Undo");
        undo.setEnabled(false);
        undo.setAccelerator(KeyStroke.getKeyStroke("control Z"));
        undo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
            }
        });
        menu.add(undo);
        redo = new JMenuItem("Redo");
        redo.setEnabled(false);
        redo.setAccelerator(KeyStroke.getKeyStroke("control Y"));
        redo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
            }
        });
        menu.add(redo);
        menu.add(new JSeparator());
        cut = new JMenuItem("Cut");
        cut.setEnabled(false);
        cut.setAccelerator(KeyStroke.getKeyStroke("control X"));
        cut.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
            }
        });
        menu.add(cut);
        copy = new JMenuItem("Copy");
        copy.setEnabled(false);
        copy.setAccelerator(KeyStroke.getKeyStroke("control C"));
        copy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
            }
        });
        menu.add(copy);
        paste = new JMenuItem("Paste");
        paste.setEnabled(false);
        paste.setAccelerator(KeyStroke.getKeyStroke("control V"));
        paste.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
            }
        });
        menu.add(paste);
        delete = new JMenuItem("Delete");
        delete.setEnabled(false);
        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        delete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
            }
        });
        menu.add(delete);
        menu.add(new JSeparator());
        selectAll = new JMenuItem("Select All");
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
        frame.setVisible(true);
    }
}
