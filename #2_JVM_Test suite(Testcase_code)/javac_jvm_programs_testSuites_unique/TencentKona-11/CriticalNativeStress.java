

import java.util.Random;


public class CriticalNativeStress {
    private static Random rand = new Random();

    static {
        System.loadLibrary("CriticalNative");
    }

    static final int CYCLES = 50;
    static final int THREAD_PER_CASE = 1;

    static native long sum1(long[] a);

    
    static native long sum2(long a1, int[] a2, int[] a3, long[] a4, int[] a5);

    static long sum(long[] a) {
        long sum = 0;
        for (int index = 0; index < a.length; index++) {
            sum += a[index];
        }
        return sum;
    }

    static long sum(int[] a) {
        long sum = 0;
        for (int index = 0; index < a.length; index++) {
            sum += a[index];
        }
        return sum;
    }

    private static volatile String garbage_array[];

    static void create_garbage(int len) {
        len = Math.max(len, 1024);
        String array[] = new String[len];
        for (int index = 0; index < len; index++) {
            array[index] = "String " + index;
        }
        garbage_array = array;
    }

    static void run_test_case1() {
        int length = rand.nextInt(50) + 1;
        long[] arr = new long[length];
        for (int index = 0; index < length; index++) {
            arr[index] = rand.nextLong() % 10002;
        }

        for (int index = 0; index < length; index++) {
            create_garbage(index);
        }

        long native_sum = sum1(arr);
        long java_sum = sum(arr);
        if (native_sum != java_sum) {
            StringBuffer sb = new StringBuffer("Sums do not match: native = ")
                    .append(native_sum).append(" java = ").append(java_sum);

            throw new RuntimeException(sb.toString());
        }
    }

    static void run_test_case2() {
        int index;
        long a1 = rand.nextLong() % 10245;

        int a2_length = rand.nextInt(50) + 1;
        int[] a2 = new int[a2_length];
        for (index = 0; index < a2_length; index++) {
            a2[index] = rand.nextInt(106);
        }

        int a3_length = rand.nextInt(150) + 1;
        int[] a3 = new int[a3_length];
        for (index = 0; index < a3_length; index++) {
            a3[index] = rand.nextInt(3333);
        }

        int a4_length = rand.nextInt(200) + 1;
        long[] a4 = new long[a4_length];
        for (index = 0; index < a4_length; index++) {
            a4[index] = rand.nextLong() % 12322;
        }

        int a5_length = rand.nextInt(350) + 1;
        int[] a5 = new int[a5_length];
        for (index = 0; index < a5_length; index++) {
            a5[index] = rand.nextInt(3333);
        }

        for (index = 0; index < a1; index++) {
            create_garbage(index);
        }

        long native_sum = sum2(a1, a2, a3, a4, a5);
        long java_sum = a1 + sum(a2) + sum(a3) + sum(a4) + sum(a5);
        if (native_sum != java_sum) {
            StringBuffer sb = new StringBuffer("Sums do not match: native = ")
                    .append(native_sum).append(" java = ").append(java_sum);

            throw new RuntimeException(sb.toString());
        }
    }

    static class Case1Runner extends Thread {
        public Case1Runner() {
            start();
        }

        public void run() {
            for (int index = 0; index < CYCLES; index++) {
                run_test_case1();
            }
        }
    }

    static class Case2Runner extends Thread {
        public Case2Runner() {
            start();
        }

        public void run() {
            for (int index = 0; index < CYCLES; index++) {
                run_test_case2();
            }
        }
    }

    public static void main(String[] args) {
        Thread[] thrs = new Thread[THREAD_PER_CASE * 2];
        for (int index = 0; index < thrs.length; index = index + 2) {
            thrs[index] = new Case1Runner();
            thrs[index + 1] = new Case2Runner();
        }

        for (int index = 0; index < thrs.length; index++) {
            try {
                thrs[index].join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
