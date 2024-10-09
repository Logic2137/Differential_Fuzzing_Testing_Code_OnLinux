



package compiler.c2;

public class Test6741738 {
    private String[] values;
    private int count;

    String foo() {
        int i = Integer.MAX_VALUE - 1;
        String s;
        try {
            s = values[i];
        } catch (Throwable e) {
            s = "";
        }
        return s;
    }

    public static void main(String[] args) {
        Test6741738 t = new Test6741738();
        String s = t.foo();
    }
}
