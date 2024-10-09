
package nsk.jvmti.scenarios.bcinstr.BI04;

public class bi04t002a {

    public final static int TOTAL_INSTRUMENTED_METHODS = 4;

    public final static int INSTR_EQUALS = 0;
    public final static int INSTR_TOSTRING = 1;
    public final static int INSTR_WAIT_JI = 2;
    public final static int INSTR_WAIT = 3;

    static int invocationsCount[] = new int[TOTAL_INSTRUMENTED_METHODS];

    public static void instrInvoke(int idx) {
        invocationsCount[idx]++;
    }

}
