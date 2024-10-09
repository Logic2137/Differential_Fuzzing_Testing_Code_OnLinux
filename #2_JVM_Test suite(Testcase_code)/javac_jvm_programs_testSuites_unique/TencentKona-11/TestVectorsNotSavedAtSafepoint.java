



import java.util.Arrays;

public class TestVectorsNotSavedAtSafepoint {

    static void test1(byte[] barray1, byte[] barray2, byte[] barray3, long[] larray, long v) {
        
        
        for (int i = 0; i < larray.length; i++) {
            larray[i] = v;
        }
        
        
        for (int i = 0; i < barray3.length; i++) {
            barray3[i] = (byte)(barray1[i] + barray2[i]);
        }
    }

    public static void test2(int[] iArr, long[] lArr) {
        
        for (int i = 0; i < lArr.length; i++) {
            iArr[i] = 1;
            lArr[i] = 1;
        }
    }

    static class GarbageProducerThread extends Thread {
        public void run() {
            for(;;) {
                
                
                Object[] arrays = new Object[1024];
                for (int i = 0; i < arrays.length; i++) {
                    arrays[i] = new int[1024];
                }
                System.gc();
            }
        }
    }

    public static void main(String[] args) {
        Thread garbage_producer = new GarbageProducerThread();
        garbage_producer.setDaemon(true);
        garbage_producer.start();

        if (args[0].equals("test1")) {
            byte[] bArr = new byte[10];
            long[] lArr = new long[1000];
            for (int i = 0; i < 10_000; ++i) {
                test1(bArr, bArr, bArr, lArr, -1);
                for (int j = 0; j < lArr.length; ++j) {
                    if (bArr[j % 10] != 0 || lArr[j] != -1) {
                        throw new RuntimeException("Test1 failed at iteration " + i + ": bArr[" + (j % 10) + "] = " + bArr[j % 10] + ", lArr[" + j + "] = " + lArr[j]);
                    }
                }
            }
        } else {
            int iArr[] = new int[100];
            long lArr[] = new long[100];
            for (int i = 0; i < 10_000; ++i) {
                test2(iArr, lArr);
                for (int j = 0; j < lArr.length; ++j) {
                    if (iArr[j] != 1 || lArr[j] != 1) {
                        throw new RuntimeException("Test2 failed at iteration " + i + ": iArr[" + j + "] = " + iArr[j] + ", lArr[" + j + "] = " + lArr[j]);
                    }
                }
            }
        }
    }
}

