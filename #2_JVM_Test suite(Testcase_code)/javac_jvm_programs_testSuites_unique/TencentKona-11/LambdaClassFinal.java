



import java.lang.reflect.Modifier;
import java.io.Serializable;

public class LambdaClassFinal {

    interface I {
        void m();
    }

    interface Iser extends Serializable {
        void m();
    }

    static void assertTrue(boolean cond) {
        if (!cond)
            throw new AssertionError();
    }

    public static void main(String[] args) throws Exception {
        new LambdaClassFinal().test();
    }

     void test() throws Exception {
        I lam = () -> { };
        assertTrue((lam.getClass().getModifiers() & Modifier.FINAL) != 0);
        Iser slam = () -> { };
        assertTrue((slam.getClass().getModifiers() & Modifier.FINAL) != 0);
    }
}
