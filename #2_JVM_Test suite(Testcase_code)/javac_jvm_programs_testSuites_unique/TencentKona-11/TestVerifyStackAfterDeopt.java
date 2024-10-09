




package compiler.interpreter;

public class TestVerifyStackAfterDeopt {

    private void method(Object[] a) {

    }

    private void test() {
        
        
        
        
        
        method(new Object[0]);
    }

    public static void main(String[] args) {
        TestVerifyStackAfterDeopt t = new TestVerifyStackAfterDeopt();
        
        for (int i = 0; i < 100_000; ++i) {
            t.test();
        }
    }
}
