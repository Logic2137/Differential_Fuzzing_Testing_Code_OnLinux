



import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;

import sun.awt.SunToolkit;

class TestThread extends Thread {

    IIORegistry registry;
    boolean useCache;
    File cacheDirectory;
    boolean cacheSettingsOK = false;
    String threadName;

    boolean gotCrosstalk = false;

    public TestThread(ThreadGroup tg,
                      boolean useCache, File cacheDirectory,
                      String threadName) {
        super(tg, threadName);
        this.useCache = useCache;
        this.cacheDirectory = cacheDirectory;
        this.threadName = threadName;
    }

    public void run() {



        
        SunToolkit.createNewAppContext();

        
        this.registry = IIORegistry.getDefaultInstance();

        for (int i = 0; i < 10; i++) {



            ImageIO.setUseCache(useCache);
            ImageIO.setCacheDirectory(cacheDirectory);

            try {
                sleep(1000L);
            } catch (InterruptedException e) {
            }


            boolean newUseCache = ImageIO.getUseCache();
            File newCacheDirectory = ImageIO.getCacheDirectory();
            if (newUseCache != useCache ||
                newCacheDirectory != cacheDirectory) {




                gotCrosstalk = true;
            }
        }
    }

    public IIORegistry getRegistry() {
        return registry;
    }

    public boolean gotCrosstalk() {
        return gotCrosstalk;
    }
}

public class AppContextTest {

    public AppContextTest() {
        ThreadGroup tg0 = new ThreadGroup("ThreadGroup0");
        ThreadGroup tg1 = new ThreadGroup("ThreadGroup1");

        TestThread t0 =
            new TestThread(tg0, false, null, "TestThread 0");
        TestThread t1 =
            new TestThread(tg1, true, new File("."), "TestThread 1");

        t0.start();
        t1.start();

        try {
            t0.join();
        } catch (InterruptedException ie0) {
        }
        try {
            t1.join();
        } catch (InterruptedException ie1) {
        }

        if (t0.gotCrosstalk() || t1.gotCrosstalk()) {
            throw new RuntimeException("ImageIO methods had crosstalk!");
        }

        if (t0.getRegistry() == t1.getRegistry()) {
            throw new RuntimeException("ThreadGroups had same IIORegistry!");
        }
    }

    public static void main(String[] args) throws IOException {
        new AppContextTest();
    }
}
