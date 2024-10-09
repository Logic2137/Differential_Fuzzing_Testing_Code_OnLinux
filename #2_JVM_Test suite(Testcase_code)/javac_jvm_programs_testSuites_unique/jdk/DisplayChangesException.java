

import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JFrame;

import sun.awt.DisplayChangedListener;
import sun.awt.SunToolkit;


public final class DisplayChangesException {

    private static boolean fail;
    private static CountDownLatch go = new CountDownLatch(1);

    static final class TestThread extends Thread {

        private JFrame frame;

        private TestThread(ThreadGroup tg, String threadName) {
            super(tg, threadName);
        }

        public void run() {
            try {
                test();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void test() throws Exception {
            SunToolkit.createNewAppContext();
            EventQueue.invokeAndWait(() -> {
                frame = new JFrame();
                final JButton b = new JButton();
                b.addPropertyChangeListener(evt -> {
                    if (!SunToolkit.isDispatchThreadForAppContext(b)) {
                        System.err.println("Wrong thread:" + currentThread());
                        fail = true;
                    }
                });
                frame.add(b);
                frame.setSize(100, 100);
                frame.setLocationRelativeTo(null);
                frame.pack();
            });
            go.await();
            EventQueue.invokeAndWait(() -> {
                frame.dispose();
            });
        }
    }

    public static void main(final String[] args) throws Exception {
        ThreadGroup tg0 = new ThreadGroup("ThreadGroup0");
        ThreadGroup tg1 = new ThreadGroup("ThreadGroup1");

        TestThread t0 = new TestThread(tg0, "TestThread 0");
        TestThread t1 = new TestThread(tg1, "TestThread 1");

        t0.start();
        t1.start();
        Thread.sleep(1500); 
        testToolkit();
        Thread.sleep(1500);
        testGE();
        Thread.sleep(1500);
        go.countDown();

        if (fail) {
            throw new RuntimeException();
        }
    }

    private static void testGE() {
        Object ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        if (!(ge instanceof DisplayChangedListener)) {
            return;
        }
        ((DisplayChangedListener) ge).displayChanged();
    }

    private static void testToolkit() {
        final Class toolkit;
        try {
            toolkit = Class.forName("sun.awt.windows.WToolkit");
        } catch (final ClassNotFoundException ignored) {
            return;
        }
        try {
            final Method displayChanged = toolkit.getMethod("displayChanged");
            displayChanged.invoke(Toolkit.getDefaultToolkit());
        } catch (final Exception e) {
            e.printStackTrace();
            fail = true;
        }
    }
}

