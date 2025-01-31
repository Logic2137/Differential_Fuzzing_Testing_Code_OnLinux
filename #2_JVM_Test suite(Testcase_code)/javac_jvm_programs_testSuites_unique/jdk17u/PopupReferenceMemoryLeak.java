import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import static javax.swing.UIManager.getInstalledLookAndFeels;

public class PopupReferenceMemoryLeak {

    static volatile WeakReference referenceToFrame1;

    static JFrame frame1, frame2;

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        robot.setAutoDelay(200);
        for (UIManager.LookAndFeelInfo laf : getInstalledLookAndFeels()) {
            String lafName = laf.getName();
            System.out.println("Testing LaF: " + lafName);
            if (lafName == null || lafName.startsWith("Mac OS X")) {
                continue;
            }
            setLookAndFeel(laf);
            PopupReferenceMemoryLeak newTest = new PopupReferenceMemoryLeak();
            SwingUtilities.invokeAndWait(newTest::createUI);
            try {
                boolean passed = false;
                robot.waitForIdle();
                Thread.sleep(2000);
                robot.mouseMove(200, 200);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                robot.keyPress(KeyEvent.VK_F10);
                robot.keyRelease(KeyEvent.VK_F10);
                robot.keyPress(KeyEvent.VK_F);
                robot.keyRelease(KeyEvent.VK_F);
                robot.keyPress(KeyEvent.VK_C);
                robot.keyRelease(KeyEvent.VK_C);
                robot.waitForIdle();
                Thread.sleep(2000);
                robot.mouseMove(600, 200);
                robot.waitForIdle();
                Thread.sleep(2000);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                JFrame frame3 = new JFrame("Workaround");
                frame3.setSize(100, 100);
                frame3.setLocation(0, 0);
                frame3.setVisible(true);
                Thread.sleep(1000);
                frame3.setVisible(false);
                frame3.dispose();
                for (int i = 0; i < 3; i++) {
                    try {
                        ArrayList gc = new ArrayList();
                        while (true) {
                            gc.add(new int[100000]);
                        }
                    } catch (Throwable ignore) {
                    }
                    robot.waitForIdle();
                    Thread.sleep(1000);
                    if (referenceToFrame1.get() == null) {
                        passed = true;
                        break;
                    }
                }
                if (!passed) {
                    robot.waitForIdle();
                    robot.keyPress(KeyEvent.VK_F10);
                    robot.keyRelease(KeyEvent.VK_F10);
                    robot.keyPress(KeyEvent.VK_T);
                    robot.keyRelease(KeyEvent.VK_T);
                    robot.keyPress(KeyEvent.VK_M);
                    robot.keyRelease(KeyEvent.VK_M);
                    robot.waitForIdle();
                    Thread.sleep(2000);
                    for (int i = 0; i < 5; i++) {
                        try {
                            ArrayList gc = new ArrayList();
                            while (true) {
                                gc.add(new int[100000]);
                            }
                        } catch (Throwable ignore) {
                        }
                        robot.waitForIdle();
                        Thread.sleep(1000);
                        if (referenceToFrame1.get() == null) {
                            throw new RuntimeException("Frame cleared only after menu activated on frame2");
                        }
                    }
                    throw new RuntimeException("Test finished but menu has not cleared the reference!");
                }
            } catch (Exception re) {
                throw new RuntimeException(re.getLocalizedMessage());
            } finally {
                if (frame2 != null) {
                    frame2.setVisible(false);
                    frame2.dispose();
                }
            }
        }
    }

    private static void setLookAndFeel(UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
        } catch (UnsupportedLookAndFeelException ignored) {
            System.out.println("Unsupported LookAndFeel: " + laf.getClassName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createUI() {
        frame1 = new JFrame("Main test window");
        JMenuBar menuBar1 = new JMenuBar();
        JMenu file1 = new JMenu("File");
        file1.setMnemonic('f');
        JMenuItem close1 = new JMenuItem("Close");
        close1.setMnemonic('c');
        close1.addActionListener(new FrameCloser(frame1));
        file1.add(close1);
        menuBar1.add(file1);
        frame1.setJMenuBar(menuBar1);
        frame1.setSize(200, 200);
        frame1.setLocation(100, 100);
        frame1.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame1.setVisible(true);
        referenceToFrame1 = new WeakReference(frame1);
        frame1 = null;
        frame2 = new JFrame("Secondary");
        JMenuBar menuBar2 = new JMenuBar();
        JMenu test = new JMenu("Test");
        test.setMnemonic('T');
        JMenuItem memoryTest = new JMenuItem("Memory");
        memoryTest.setMnemonic('M');
        test.add(memoryTest);
        menuBar2.add(test);
        frame2.setJMenuBar(menuBar2);
        frame2.setLayout(new BorderLayout());
        frame2.setSize(200, 200);
        frame2.setLocation(500, 100);
        frame2.setVisible(true);
    }

    class FrameCloser extends AbstractAction {

        JFrame frame;

        public FrameCloser(JFrame f) {
            this.frame = f;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (frame != null) {
                frame.setVisible(false);
                frame.dispose();
                this.frame = null;
            }
        }
    }
}
