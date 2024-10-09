public class TestShortArraycopy {

    static short[] a1 = new short[8];

    static short[] a2 = new short[8];

    static short[] a3 = new short[8];

    static volatile boolean keepRunning = true;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < a1.length; i++) {
            a1[i] = (short) 0xffff;
            a2[i] = (short) 0xffff;
            a3[i] = (short) 0x0000;
        }
        Thread reader = new Thread() {

            public void run() {
                while (keepRunning) {
                    for (int j = 0; j < a1.length; j++) {
                        short s = a1[j];
                        if (s != (short) 0xffff && s != (short) 0x0000) {
                            System.out.println("Error: s = " + s);
                            throw new RuntimeException("wrong result");
                        }
                    }
                }
            }
        };
        Thread writer = new Thread() {

            public void run() {
                for (int i = 0; i < 1000000; i++) {
                    System.arraycopy(a2, 5, a1, 3, 3);
                    System.arraycopy(a3, 5, a1, 3, 3);
                }
            }
        };
        keepRunning = true;
        reader.start();
        writer.start();
        writer.join();
        keepRunning = false;
        reader.join();
    }
}
