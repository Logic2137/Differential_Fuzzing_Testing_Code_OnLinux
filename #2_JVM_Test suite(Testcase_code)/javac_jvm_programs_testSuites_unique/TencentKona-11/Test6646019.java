



package compiler.c2;

public class Test6646019 {
    final static int i = 2076285318;
    long l = 2;
    short s;

    public static void main(String[] args) {
        Test6646019 t = new Test6646019();
        try {
            t.test();
        } catch (Throwable e) {
            if (t.l != 5) {
                System.out.println("Fails: " + t.l + " != 5");
            }
        }
    }

    private void test() {
        l = 5;
        l = (new short[(byte) -2])[(byte) (l = i)];
    }
}
