



package test.java.lang.invoke;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;


public class LoopCombinatorLongSignatureTest {

    static final MethodHandle INIT = MethodHandles.constant(int.class, 0);
    static final MethodHandle STEP = MethodHandles.identity(int.class);
    static final MethodHandle PRED_F = MethodHandles.constant(boolean.class, false);
    static final MethodHandle PRED_T = MethodHandles.constant(boolean.class, true);
    static final MethodHandle FINI = MethodHandles.identity(int.class);

    static final int ARG_LIMIT = 254; 

    public static void main(String[] args) {
        boolean run = Boolean.parseBoolean(
                System.getProperty("java.lang.invoke.LoopCombinatorLongSignatureTest.RUN", "false"));
        for (int loopArgs = 0; loopArgs < 2; ++loopArgs) {
            testLongSignature(loopArgs, false, run);
            testLongSignature(loopArgs, true, run);
        }
    }

    static void testLongSignature(int loopArgs, boolean excessive, boolean run) {
        int nClauses = ARG_LIMIT - loopArgs + (excessive ? 1 : 0);

        System.out.print((excessive ? "(EXCESSIVE)" : "(LONG     )") + " arguments: " + loopArgs + ", clauses: " + nClauses + " -> ");

        
        Class<?>[] argTypes = new Class<?>[loopArgs];
        Arrays.fill(argTypes, int.class);
        MethodHandle init = MethodHandles.dropArguments(INIT, 0, argTypes);

        
        MethodHandle[][] clauses = new MethodHandle[nClauses][];
        MethodHandle[] clause = {init, STEP, PRED_T, FINI};
        MethodHandle[] fclause = {init, STEP, PRED_F, FINI};
        Arrays.fill(clauses, clause);
        clauses[nClauses - 1] = fclause; 

        try {
            MethodHandle loop = MethodHandles.loop(clauses);
            if (excessive) {
                throw new AssertionError("loop construction should have failed");
            } else if (run) {
                int r;
                if (loopArgs == 0) {
                    r = (int) loop.invoke();
                } else {
                    Object[] args = new Object[loopArgs];
                    Arrays.fill(args, 0);
                    r = (int) loop.invokeWithArguments(args);
                }
                System.out.println("SUCCEEDED (OK) -> " + r);
            } else {
                System.out.println("SUCCEEDED (OK)");
            }
        } catch (IllegalArgumentException iae) {
            if (excessive) {
                System.out.println("FAILED    (OK)");
            } else {
                iae.printStackTrace(System.out);
                throw new AssertionError("loop construction should not have failed (see above)");
            }
        } catch (Throwable t) {
            t.printStackTrace(System.out);
            throw new AssertionError("unexpected failure (see above)");
        }
    }

}
