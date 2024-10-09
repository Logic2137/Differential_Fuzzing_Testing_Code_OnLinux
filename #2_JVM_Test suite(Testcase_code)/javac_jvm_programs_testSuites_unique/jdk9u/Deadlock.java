



public class Deadlock {
    public double e;
    private volatile int i;

    public static void main(String[] args) {
        new Deadlock().test();
    }
    private void test() {
        final Object a = new Object();
        final Object b = new Object();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (a) {
                    do {
                        i |= 1;
                    } while (i != 3);

                    synchronized (b) {
                        e = 1;
                    }
                }
            }}).start();

        synchronized (b) {
            do {
                i |= 2;
            } while (i != 3);
            synchronized (a) {
                e = 2;
            }
        }
    }
}
