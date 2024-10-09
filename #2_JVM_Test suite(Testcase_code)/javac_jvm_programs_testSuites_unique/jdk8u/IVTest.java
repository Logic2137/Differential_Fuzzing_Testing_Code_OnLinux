public class IVTest {

    static int paddedSize;

    static void padV15(byte[] padded) {
        int psSize = padded.length;
        int k = 0;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        while (psSize-- > 0) {
            padded[k++] = (byte) 0xff;
        }
    }

    static void padV15_2(int paddedSize) {
        byte[] padded = new byte[paddedSize];
        int psSize = padded.length;
        int k = 0;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        while (psSize-- > 0) {
            padded[k++] = (byte) 0xff;
        }
    }

    static void padV15_3() {
        byte[] padded = new byte[paddedSize];
        int psSize = padded.length;
        int k = 0;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        while (psSize-- > 0) {
            padded[k++] = (byte) 0xff;
        }
    }

    static void padV15_4() {
        byte[] padded = new byte[paddedSize];
        int psSize = padded.length;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int k = 0; psSize > 0; psSize--) {
            int i = padded.length - psSize;
            padded[i] = (byte) 0xff;
        }
    }

    static void padV15_5() {
        byte[] padded = new byte[paddedSize];
        int psSize = padded.length;
        int k = psSize - 1;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < psSize; i++) {
            padded[k--] = (byte) 0xff;
        }
    }

    public static void main(String[] argv) {
        int bounds = 1024;
        int lim = 500000;
        long start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int j = 0; j < lim; j++) {
            paddedSize = j % bounds;
            padV15(new byte[paddedSize]);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int j = 0; j < lim; j++) {
            paddedSize = j % bounds;
            padV15_2(paddedSize);
        }
        end = System.currentTimeMillis();
        System.out.println(end - start);
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int j = 0; j < lim; j++) {
            paddedSize = j % bounds;
            padV15_3();
        }
        end = System.currentTimeMillis();
        System.out.println(end - start);
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int j = 0; j < lim; j++) {
            paddedSize = j % bounds;
            padV15_4();
        }
        end = System.currentTimeMillis();
        System.out.println(end - start);
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int j = 0; j < lim; j++) {
            paddedSize = j % bounds;
            padV15_5();
        }
        end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
