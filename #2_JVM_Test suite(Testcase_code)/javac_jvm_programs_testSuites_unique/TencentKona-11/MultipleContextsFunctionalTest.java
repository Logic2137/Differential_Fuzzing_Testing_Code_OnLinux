



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public final class MultipleContextsFunctionalTest {

    private static final long serialVersionUID = 1L;

    private static final int NUM_WINDOW = 2;
    private static final int INTERVAL = 50;
    private static final int MAX_TIME = 10000; 
    private static final int TOLERANCE = 10000;
    private static final int CHECK_LAPSE = 100;
    private static final int MAX_COUNT = MAX_TIME / INTERVAL;
    private static final int EXPECTED = MAX_COUNT * NUM_WINDOW;
    private static final List<TestWindow> WINDOWS = new ArrayList<TestWindow>();

    public static void main(String[] args) {
        for (int i = 0; i < NUM_WINDOW; i++) {
            createWin(i);
        }

        int total = 0;
        int waitingTime = MAX_TIME + TOLERANCE;
        while (waitingTime > 0 && total != EXPECTED) {
            try {
                Thread.sleep(CHECK_LAPSE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            waitingTime -= CHECK_LAPSE;

            total = 0;
            for (TestWindow window : WINDOWS) {
                total += window.getCounter();
            }
        }

        
        System.out.println("Total [" + total + "] - Expected [" + EXPECTED + "]");
        if (total == EXPECTED) {
            System.out.println("Test PASSED");
            return;
        }
        System.out.println("Test FAILED");
        Runtime.getRuntime().halt(-1);
    }

    private static void createWin(int tgNum) {
        new Thread(new ThreadGroup("TG " + tgNum),
                new Runnable() {
            @Override
            public void run() {
                sun.awt.SunToolkit.createNewAppContext();

                final AtomicReference<TestWindow> ref =
                        new AtomicReference<TestWindow>();

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        final TestWindow window = new TestWindow(tgNum);
                        window.setVisible(true);
                        ref.set(window);
                        WINDOWS.add(window);
                    }
                });

                
                TestWindow window = ref.get();
                while (window == null) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    window = ref.get();
                }
                window.enableTimer(true);
            }
        }).start();
    }

    private static final class TestWindow extends JFrame implements ActionListener {

        private final JButton btn;
        private int counter = 0;
        private final Timer t;

        TestWindow(final int num) {
            super("Test Window [" + num + "]");
            setMinimumSize(new Dimension(300, 200));
            setLocation(100 + 400 * (num - 1), 100);

            setLayout(new BorderLayout());
            JLabel textBlock = new JLabel("Lorem ipsum dolor sit amet...");
            add(textBlock);

            btn = new JButton("TEST");
            add(btn, BorderLayout.SOUTH);

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            pack();

            t = new Timer(INTERVAL, this);
            t.setRepeats(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            this.toFront();
            btn.setText("TEST " + (++counter));
            this.toBack();
            if (counter < MAX_COUNT) {
                enableTimer(true);
            } else {
                dispose();
            }
        }

        void enableTimer(boolean enable) {
            if (enable) {
                t.start();
            } else {
                t.stop();
            }
        }

        int getCounter() {
            return counter;
        }
    }
}
