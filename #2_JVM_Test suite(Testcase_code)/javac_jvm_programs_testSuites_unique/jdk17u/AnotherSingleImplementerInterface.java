
package compiler.jvmci.common.testcases;

public interface AnotherSingleImplementerInterface {

    public static final long initTime = System.currentTimeMillis();

    default void defaultMethod() {
    }

    void interfaceMethod();

    void finalize() throws Throwable;
}
