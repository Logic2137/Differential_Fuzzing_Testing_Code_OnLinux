



package compiler.arraycopy;

public class TestArrayCopyStoppedAfterGuards {

    static void test() {
        Object src = new Object();
        int[] dst = new int[10];
        System.arraycopy(src, 0, dst, 0, 10);
    }

    static public void main(String[] args) {
        
        Object o = new Object();
        int[] src = new int[10];
        int[] dst = new int[10];
        System.arraycopy(src, 0, dst, 0, 10);

        try {
            test();
        } catch(ArrayStoreException ase) {}
    }
}
