import java.awt.*;

public class EventQueuePushAutoshutdown implements Runnable {

    private volatile int status = 2;

    public EventQueuePushAutoshutdown() throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(this));
        Thread thread = new Thread() {

            @Override
            public void run() {
                status = 0;
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    status = 1;
                    System.exit(status);
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        System.setProperty("java.awt.headless", "true");
        final EventQueue systemQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
        systemQueue.push(new EventQueue());
        EventQueue.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                System.out.println("Activated EDT");
            }
        });
        System.out.println("After EDT activation");
    }

    public static void main(String[] args) throws Exception {
        new EventQueuePushAutoshutdown();
    }

    @Override
    public void run() {
        Runtime.getRuntime().halt(status);
    }
}
