
package compiler.arraycopy;

public class TestArrayCopyMemoryChain {

    private String mySweetEscape1 = null;

    private String getString(int i) {
        return "A" + i + "B";
    }

    public void test1(int i) {
        mySweetEscape1 = getString(i) + "CD";
    }

    private byte[] mySweetEscape2;

    class Wrapper {

        public final byte[] array;

        public Wrapper(byte[] array) {
            this.array = array;
        }
    }

    public void test2(int idx, int size) {
        byte[] dst = new byte[size];
        mySweetEscape2 = dst;
        byte[] src1 = { 43, 44 };
        byte[] array = { 42 };
        Wrapper wrapper = new Wrapper(array);
        byte[] src2 = wrapper.array;
        System.arraycopy(src1, 0, dst, idx, src1.length);
        System.arraycopy(src2, 0, dst, 0, src2.length);
    }

    public static void main(String[] args) {
        TestArrayCopyMemoryChain t = new TestArrayCopyMemoryChain();
        for (int i = 0; i < 100_000; ++i) {
            t.test1(0);
            if (!t.mySweetEscape1.equals("A0BCD")) {
                throw new RuntimeException("Test1 failed");
            }
            t.test2(1, 3);
            if (t.mySweetEscape2[0] != 42 || t.mySweetEscape2[1] != 43 || t.mySweetEscape2[2] != 44) {
                throw new RuntimeException("Test2 failed");
            }
        }
    }
}
