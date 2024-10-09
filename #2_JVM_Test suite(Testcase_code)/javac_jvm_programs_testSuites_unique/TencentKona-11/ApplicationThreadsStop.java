

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.Robot;

import sun.awt.AppContext;
import sun.awt.SunToolkit;


public final class ApplicationThreadsStop implements Runnable {

    private static AppContext contextToDispose;
    private static Thread thread;

    public static void main(final String[] args) throws Exception {
        ThreadGroup tg = new ThreadGroup("TestThreadGroup");
        Thread t = new Thread(tg, new ApplicationThreadsStop());
        t.start();
        t.join();
        contextToDispose.dispose();
        
        Thread.sleep(10000);
        if(thread.isAlive()){
            throw new RuntimeException("Thread is alive");
        }
    }

    @Override
    public void run() {
        contextToDispose = SunToolkit.createNewAppContext();
        Frame f = new Frame();
        f.setSize(300, 300);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        thread = new Thread(() -> {
            while(true);
        });
        thread.start();
        sync();
    }

    private static void sync() {
        try {
            new Robot().waitForIdle();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }
}
