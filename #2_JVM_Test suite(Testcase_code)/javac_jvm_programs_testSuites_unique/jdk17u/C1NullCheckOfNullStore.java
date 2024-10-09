
package compiler.codegen;

public class C1NullCheckOfNullStore {

    private static class Foo {

        Object bar;
    }

    static private void test(Foo x) {
        x.bar = null;
    }

    static public void main(String[] args) {
        Foo x = new Foo();
        for (int i = 0; i < 10000; i++) {
            test(x);
        }
        boolean gotNPE = false;
        try {
            for (int i = 0; i < 10000; i++) {
                test(null);
            }
        } catch (NullPointerException e) {
            gotNPE = true;
        }
        if (!gotNPE) {
            throw new Error("Expecting a NullPointerException");
        }
    }
}
