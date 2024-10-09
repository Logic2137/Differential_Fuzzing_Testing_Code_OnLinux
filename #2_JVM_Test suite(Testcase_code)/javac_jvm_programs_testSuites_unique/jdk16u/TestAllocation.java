



package compiler.allocation;

public class TestAllocation {

    public static volatile int size = 128;
    public static volatile int neg_size = -128;

    public int testUnknownPositiveArrayLength() {
        Payload w = new Payload(17, size);
        return w.i + w.ba.length;
    }

    public int testStoreCapture() {
        byte[] bs = new byte[1];
        bs[0] = 1;
        return bs.length;
    }

    public int testUnknownNegativeArrayLength() {
        boolean success = false;
        try {
            Payload w = new Payload(17, neg_size);
            return w.i + w.ba.length;
        } catch (NegativeArraySizeException e) {
            success = true;
        }
        if (!success) {
            throw new RuntimeException("Should have thrown NegativeArraySizeException");
        }
        return 0;

    }

    public int testConstantPositiveArrayLength() {
        Payload w = new Payload(173, 10);
        return w.i + w.ba.length;
    }

    public int testConstantPositiveArrayLength2() {
        Payload w = new Payload(173, 10);
        w.i = 17;
        w.ba = new byte[10];
        return w.i + w.ba.length;
    }

    public int testConstantNegativeArrayLength() {
        boolean success = false;
        try {
            Payload w = new Payload(173, -10);
            return w.i + w.ba.length;
        } catch (NegativeArraySizeException e) {
            success = true;
        }
        if (!success) {
            throw new RuntimeException("Should have thrown NegativeArraySizeException");
        }
        return 0;
    }

    public static class Payload {
        public int i;
        public byte ba[];

        public Payload(int i, int size) {
            this.i = i;
            this.ba = new byte[size];
        }
    }

    public static void main(String[] strArr) {
        TestAllocation test = new TestAllocation();
        for (int i = 0; i < 10_000; i++ ) {
            test.testUnknownPositiveArrayLength();
            test.testUnknownNegativeArrayLength();
            test.testConstantPositiveArrayLength();
            test.testConstantPositiveArrayLength2();
            test.testConstantNegativeArrayLength();
            test.testStoreCapture();
        }
    }
}

class Wrapper {
    int[] arr;
    void setArr(int... many) { arr = many; }
}
