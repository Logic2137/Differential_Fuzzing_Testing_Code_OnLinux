

package compiler.jvmci.common.testcases;

public interface SingleImplementerInterface {
    public static final long initTime = System.currentTimeMillis();

    default void defaultMethod() {
        
    }

    void interfaceMethod();

    void finalize() throws Throwable;
}
