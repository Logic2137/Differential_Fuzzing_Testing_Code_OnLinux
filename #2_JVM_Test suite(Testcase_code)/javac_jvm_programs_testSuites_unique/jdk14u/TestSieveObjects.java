







import java.util.concurrent.ThreadLocalRandom;

public class TestSieveObjects {

    static final int COUNT = 100_000_000;
    static final int WINDOW = 1_000_000;
    static final int PAYLOAD = 100;

    static final MyObject[] arr = new MyObject[WINDOW];

    public static void main(String[] args) throws Exception {
        int rIdx = 0;
        for (int c = 0; c < COUNT; c++) {
            MyObject v = arr[rIdx];
            if (v != null) {
                if (v.x != rIdx) {
                    throw new IllegalStateException("Illegal value at index " + rIdx + ": " + v.x);
                }
                if (ThreadLocalRandom.current().nextInt(1000) > 100) {
                    arr[rIdx] = null;
                }
            } else {
                if (ThreadLocalRandom.current().nextInt(1000) > 500) {
                    arr[rIdx] = new MyObject(rIdx);
                }
            }
            rIdx++;
            if (rIdx >= WINDOW) {
                rIdx = 0;
            }
        }
    }

    public static class MyObject {
        public int x;
        public byte[] payload;

        public MyObject(int x) {
            this.x = x;
            this.payload = new byte[PAYLOAD];
        }
    }

}
