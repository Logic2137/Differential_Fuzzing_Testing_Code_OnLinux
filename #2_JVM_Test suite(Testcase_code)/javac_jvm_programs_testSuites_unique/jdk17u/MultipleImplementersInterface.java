
package compiler.jvmci.common.testcases;

import java.util.HashMap;
import java.util.Map;

public interface MultipleImplementersInterface {

    int INT_CONSTANT = Integer.MAX_VALUE;

    long LONG_CONSTANT = Long.MAX_VALUE;

    float FLOAT_CONSTANT = Float.MAX_VALUE;

    double DOUBLE_CONSTANT = Double.MAX_VALUE;

    String STRING_CONSTANT = "Hello";

    Object OBJECT_CONSTANT = new Object();

    default void defaultMethod() {
    }

    void testMethod();

    default void finalize() throws Throwable {
    }

    default void lambdaUsingMethod() {
        Thread t = new Thread(this::defaultMethod);
        t.start();
    }

    default void printFields() {
        System.out.println(OBJECT_CONSTANT);
        String s = "";
        System.out.println(s);
    }

    static void staticMethod() {
        System.getProperties();
        Map map = new HashMap();
        map.put(OBJECT_CONSTANT, OBJECT_CONSTANT);
        map.remove(OBJECT_CONSTANT);
    }

    default void instanceMethod() {
        toString();
    }

    default void anonClassMethod() {
        new Runnable() {

            @Override
            public void run() {
                System.out.println("Running");
            }
        }.run();
    }
}
