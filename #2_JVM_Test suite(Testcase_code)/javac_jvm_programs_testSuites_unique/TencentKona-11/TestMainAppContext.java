
import java.util.logging.Logger;
import sun.awt.AppContext;
import sun.awt.SunToolkit;



public class TestMainAppContext {

    static volatile Throwable thrown = null;

    public static void main(String... args) throws Exception {
        ThreadGroup rootTG = Thread.currentThread().getThreadGroup();
        while (rootTG.getParent() != null) {
            rootTG = rootTG.getParent();
        }

        ThreadGroup tg = new ThreadGroup(rootTG, "FakeApplet");
        final Thread t1 = new Thread(tg, "createNewAppContext") {
            @Override
            public void run() {
                try {
                    AppContext context = SunToolkit.createNewAppContext();
                } catch(Throwable t) {
                    thrown = t;
                }
            }
        };
        t1.start();
        t1.join();
        if (thrown != null) {
            throw new RuntimeException("Unexpected exception: " + thrown, thrown);
        }
        Thread t2 = new Thread(tg, "BugDetector") {

            @Override
            public void run() {
                try {
                    Logger.getLogger("foo").info("Done");
                } catch (Throwable x) {
                    thrown = x;
                }
            }

        };

        System.setSecurityManager(new SecurityManager());
        t2.start();
        t2.join();
        if (thrown != null) {
            throw new RuntimeException("Test failed: " + thrown, thrown);
        }

    }

}
