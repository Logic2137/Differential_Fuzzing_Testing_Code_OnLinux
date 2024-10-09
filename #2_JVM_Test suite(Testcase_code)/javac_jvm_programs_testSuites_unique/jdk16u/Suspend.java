



public class Suspend implements Runnable {

    private static volatile int count = 0;
    private static final ThreadGroup group = new ThreadGroup("");
    private static final Thread first = new Thread(group, new Suspend());
    private static final Thread second = new Thread(group, new Suspend());

    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
                if (Thread.currentThread() == first) {
                    if (second.isAlive()) {
                        group.suspend();
                    }
                } else {
                    count++;
                }
            } catch (InterruptedException e) {
            }
        }
    }

    public static void main(String[] args) throws Exception {
        
        first.start();
        second.start();

        
        while (!first.isAlive() || !second.isAlive()) {
            Thread.sleep(100);
        }
        Thread.sleep(1000);
        

        count = 0;
        Thread.sleep(1000);

        
        boolean failed = (count > 1);
        first.stop();
        second.stop();
        if (failed) {
            throw new RuntimeException("Failure.");
        }
    }
}
