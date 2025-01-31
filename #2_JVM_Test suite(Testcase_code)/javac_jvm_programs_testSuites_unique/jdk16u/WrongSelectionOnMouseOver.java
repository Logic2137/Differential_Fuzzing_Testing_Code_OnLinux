



import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static javax.swing.UIManager.getInstalledLookAndFeels;

public class WrongSelectionOnMouseOver implements Runnable {

    CountDownLatch firstMenuSelected = new CountDownLatch(1);
    CountDownLatch secondMenuMouseEntered = new CountDownLatch(1);
    CountDownLatch secondMenuSelected = new CountDownLatch(1);

    JMenu m1, m2;

    private UIManager.LookAndFeelInfo laf;
    JFrame frame1;
    JFrame frame2;
    private Point menu1location;
    private Point menu2location;

    public WrongSelectionOnMouseOver(UIManager.LookAndFeelInfo laf) throws Exception {
        this.laf = laf;
    }

    private void createUI() throws Exception {
        System.out.println("Testing UI: " + laf);
        UIManager.setLookAndFeel(laf.getClassName());

        {
            frame1 = new JFrame("Frame1");
            JMenuBar mb = new JMenuBar();
            m1 = new JMenu("File");
            JMenuItem i1 = new JMenuItem("Save");
            JMenuItem i2 = new JMenuItem("Load");

            m1.addMenuListener(new MenuListener() {
                @Override
                public void menuSelected(MenuEvent e) {
                    firstMenuSelected.countDown();
                    System.out.println("Menu1: menuSelected");
                }

                @Override
                public void menuDeselected(MenuEvent e) {
                    System.out.println("Menu1: menuDeselected");
                }

                @Override
                public void menuCanceled(MenuEvent e) {
                    System.out.println("Menu1: menuCanceled");
                }
            });

            frame1.setJMenuBar(mb);
            mb.add(m1);
            m1.add(i1);
            m1.add(i2);

            frame1.setLayout(new FlowLayout());
            frame1.setBounds(200, 200, 200, 200);

            frame1.setVisible(true);
        }

        {
            frame2 = new JFrame("Frame2");
            JMenuBar mb = new JMenuBar();
            m2 = new JMenu("File");
            JMenuItem i1 = new JMenuItem("Save");
            JMenuItem i2 = new JMenuItem("Load");

            m2.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    secondMenuMouseEntered.countDown();
                    System.out.println("WrongSelectionOnMouseOver.mouseEntered");
                }
            });

            m2.addMenuListener(new MenuListener() {
                @Override
                public void menuSelected(MenuEvent e) {
                    secondMenuSelected.countDown();
                    System.out.println("Menu2: menuSelected");
                }

                @Override
                public void menuDeselected(MenuEvent e) {
                    System.out.println("Menu2: menuDeselected");
                }

                @Override
                public void menuCanceled(MenuEvent e) {
                    System.out.println("Menu2: menuCanceled");
                }
            });

            frame2.setJMenuBar(mb);
            mb.add(m2);
            m2.add(i1);
            m2.add(i2);

            frame2.setLayout(new FlowLayout());
            frame2.setBounds(450, 200, 200, 200);

            frame2.setVisible(true);
        }
    }

    public void disposeUI() {
        frame1.dispose();
        frame2.dispose();
    }

    @Override
    public void run() {
        try {
            if (frame1 == null) {
                createUI();
            } else {
                disposeUI();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void test() throws Exception {
        Robot robot = new Robot();
        robot.setAutoDelay(100);

        robot.waitForIdle();

        SwingUtilities.invokeAndWait(() -> {
            menu1location = m1.getLocationOnScreen();
            menu2location = m2.getLocationOnScreen();
        });

        robot.mouseMove((int) menu1location.getX() + 5,
                (int) menu1location.getY() + 5);
        robot.mousePress(MouseEvent.BUTTON1_MASK);
        robot.mouseRelease(MouseEvent.BUTTON1_MASK);

        if (!firstMenuSelected.await(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("Menu has not been selected.");
        };

        robot.mouseMove((int) menu2location.getX() + 5,
                (int) menu2location.getY() + 5);

        if (!secondMenuMouseEntered.await(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("MouseEntered event missed for the second menu");
        };

        if (secondMenuSelected.await(1, TimeUnit.SECONDS)) {
            throw new RuntimeException("The second menu has been selected");
        };
    }

    public static void main(final String[] args) throws Exception {
        for (final UIManager.LookAndFeelInfo laf : getInstalledLookAndFeels()) {
            WrongSelectionOnMouseOver test = new WrongSelectionOnMouseOver(laf);
            SwingUtilities.invokeAndWait(test);
            test.test();
            SwingUtilities.invokeAndWait(test);
        }
        System.out.println("Test passed");
    }
}
