



package compiler.c1;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MutableCallSite;

public class Test8172751 {
    private static final MethodHandle CONSTANT_TRUE = MethodHandles.constant(boolean.class, true);
    private static final MethodHandle CONSTANT_FALSE = MethodHandles.constant(boolean.class, false);
    private static final MutableCallSite CALL_SITE = new MutableCallSite(CONSTANT_FALSE);
    private static final int LIMIT = 1_000_000;
    private static volatile int counter;

    private static boolean doSomething() {
        return counter++ < LIMIT;
    }

    private static void executeLoop() {
        
        if (CALL_SITE.getTarget() == CONSTANT_FALSE) {
            int count = 0;
            while (doSomething()) {
                if (count++ == 1) {
                    flipSwitch();
                }
            }
        } else {
            while (doSomething()) {
            }
        }
    }

    private static void flipSwitch() {
        CALL_SITE.setTarget(CONSTANT_TRUE);
    }

    public static void main(String[] args) {
        executeLoop();
    }
}
