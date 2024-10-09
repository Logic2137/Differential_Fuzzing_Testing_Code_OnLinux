

package compiler.runtime;



public class TestFloatsOnStackDeopt {

    private static final int ARRLEN = 97;
    private static final int ITERS1 = 100;
    private static final int ITERS2 = 40000;
    private static final float VALUE = 15.f;
    public static String dummyString = "long long string";
    static volatile boolean pleaseStop = false;

    static void run_loop_with_safepoint(float[] a0, float b) {
        
        for (long l = 0; l < ITERS2; l++) {
            
            for (int i = 0; i < a0.length; i += 1) {
                a0[i] += b;
            }
        }
    }

    static int test() {
        
        Thread th = new Thread() {
            public void run() {
                while (!pleaseStop) {
                    synchronized(this) { try { wait(1); } catch (Exception ex) {} }
                    dummyString = new StringBuilder(dummyString).append(dummyString).toString();
                    if (dummyString.length() > 1024*1024) { dummyString = "long long string"; }
                }
            }
        };
        th.start();

        int errn = 0;
        for (int j = 0; j < ITERS1; j++) {
            float[] x0 = new float[ARRLEN];
            run_loop_with_safepoint(x0, VALUE);
            for (int i = 0; i < ARRLEN; i++) {
                if (x0[i] != VALUE * ITERS2) {
                    System.err.println("(" + j + "): " + "x0[" + i + "] = " + x0[i] + " != " + VALUE * ITERS2);
                    errn++;
                }
                x0[i] = 0.f; 
            }
            if (errn > 0) break;
        }

        pleaseStop = true;
        try {
            th.join();
        } catch (InterruptedException e) {
            throw new Error("InterruptedException in main thread ", e);
        }
        return errn;
    }

    public static void main(String args[]) {
        int errn = test();
        System.err.println((errn > 0) ? "FAILED" : "PASSED");
    }
}

