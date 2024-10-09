import java.lang.instrument.*;

public class GetObjectSizeClassAgent {

    static Instrumentation instrumentation;

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        GetObjectSizeClassAgent.instrumentation = instrumentation;
    }

    public static void main(String[] args) throws Exception {
        long sizeA = instrumentation.getObjectSize(A.class);
        long sizeB = instrumentation.getObjectSize(B.class);
        if (sizeA != sizeB) {
            throw new RuntimeException("java.lang.Class sizes disagree: " + sizeA + " vs. " + sizeB);
        }
        System.out.println("GetObjectSizeClass passed");
    }

    static class A {
    }

    static class B {

        void m() {
        }
    }
}
