
package compiler.membars;

public class TestMemBarAcquire {

    private volatile static Object defaultObj = new Object();

    private Object obj;

    public TestMemBarAcquire(Object param) {
        this.obj = defaultObj;
        this.obj = param;
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 100000; ++i) {
            TestMemBarAcquire p = new TestMemBarAcquire(new Object());
        }
    }
}
