
package compiler.c2;

public class TestNPEHeapBased {

    TestNPEHeapBased instance = null;

    int i = 0;

    public void set_i(int value) {
        instance.i = value;
    }

    static final int loop_cnt = 200000;

    public static void main(String[] args) {
        TestNPEHeapBased xyz = new TestNPEHeapBased();
        xyz.instance = xyz;
        for (int x = 0; x < loop_cnt; x++) xyz.set_i(x);
        xyz.instance = null;
        try {
            xyz.set_i(0);
        } catch (NullPointerException npe) {
            System.out.println("Got expected NullPointerException:");
            npe.printStackTrace();
            return;
        }
        throw new InternalError("NullPointerException is missing!");
    }
}
