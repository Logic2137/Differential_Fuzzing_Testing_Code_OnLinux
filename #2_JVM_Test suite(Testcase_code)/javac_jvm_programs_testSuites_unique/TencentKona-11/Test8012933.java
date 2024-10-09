



import sun.awt.SunToolkit;
import sun.awt.AppContext;

public class Test8012933 {
    private AppContext appContext = null;
    final ThreadGroup threadGroup = new ThreadGroup("test thread group");
    final Object lock = new Object();
    boolean isCreated = false;

    public static void main(String[] args) throws Exception {
        SunToolkit.createNewAppContext();
        new Test8012933().test();
    }

    private void test() throws Exception {
        createAppContext();
        long startTime = System.currentTimeMillis();
        appContext.dispose();
        long endTime = System.currentTimeMillis();

        
        
        
        
        if ((endTime - startTime) > 9000) {
            throw new RuntimeException("Returning from dispose() took too much time, probably a bug");
        }
    }

    private void createAppContext() {
        isCreated = false;
        final Runnable runnable = new Runnable() {
                public void run() {
                    appContext = SunToolkit.createNewAppContext();
                    synchronized (lock) {
                        isCreated = true;
                        lock.notifyAll();
                    }
                }
            };

        final Thread thread = new Thread(threadGroup, runnable, "creates app context");
        synchronized (lock) {
            thread.start();
            while (!isCreated) {
                try {
                    lock.wait();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }

        if (appContext == null) {
            throw new RuntimeException("failed to create app context.");
        } else {
            System.out.println("app context was created.");
        }
    }

}
