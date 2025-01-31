



import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class JDK8048887 {

    static volatile boolean passed = true;

    public static void main(String[] args) {
        JDK8048887 app = new JDK8048887();
        app.start();
    }

    public void start() {
        final CountDownLatch latch = new CountDownLatch(1);

        SwingUtilities.invokeLater(() -> {
                
                
                Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                        public void uncaughtException(Thread t, Throwable e) {
                            e.printStackTrace();
                            if (e instanceof IllegalArgumentException) {
                                passed = false;
                                latch.countDown();
                            }
                        }
                    });

                TestDialog d = new TestDialog();
                
                
                d.addWindowFocusListener(new WindowAdapter() {
                        public void windowGainedFocus(WindowEvent e) {
                            latch.countDown();
                        }
                });
                d.setVisible(true);
        });

        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (passed)
            System.out.println("Test passed.");
        else
            throw new RuntimeException("Test failed!");
    }
}

class TestDialog extends JFrame {

    
    

    private static int[] Xs = new int[] {71, 23, 62, 4, 79, 39, 34, 9, 84, 58, 30, 34, 38, 15, 69, 10, 44, 95, 70, 54,
    44, 62, 77, 64, 70, 83, 31, 48, 96, 54, 40, 3, 60, 58, 3, 20, 94, 54, 26, 19, 48, 47, 12, 70, 86, 43, 71, 97, 19,
    69, 90, 22, 43, 76, 10, 60, 29, 49, 9, 9, 15, 73, 85, 80, 81, 35, 87, 43, 17, 57, 38, 44, 29, 86, 96, 15, 57, 26,
    27, 78, 26, 87, 43, 6, 4, 16, 57, 99, 32, 86, 96, 5, 50, 69, 12, 4, 36, 84, 71, 60, 22, 46, 11, 44, 87, 3, 23, 14,
    43, 25, 32, 44, 11, 18, 77, 2, 51, 87, 88, 53, 69, 37, 14, 10, 25, 73, 39, 33, 91, 51, 96, 9, 74, 66, 70, 42, 72,
    7, 82, 40, 91, 33, 83, 54, 33, 50, 83, 1, 81, 32, 66, 11, 75, 56, 53, 45, 1, 69, 46, 31, 79, 58, 12, 20, 92, 49,
    50, 90, 33, 8, 43, 93, 72, 78, 9, 56, 84, 60, 30, 39, 33, 88, 84, 56, 49, 47, 4, 90, 57, 6, 23, 96, 37, 88, 22, 79,
    35, 80, 45, 55};

    public TestDialog() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        for (int i=0; i < Xs.length; i++) {
            gbc.gridx = Xs[i];
            gbc.gridy = 100 - gbc.gridx;
            panel.add(new MyComponent(), gbc);
        }
        getRootPane().getContentPane().add(panel);
        pack();
    }

    public static class MyComponent extends JPanel {
        private final static Dimension SIZE = new Dimension(1,1);

        public MyComponent() {
            setBackground(Color.BLACK);
            setOpaque(true);
        }

        @Override
        public Dimension getPreferredSize() {
            return SIZE;
        }
    }
}
