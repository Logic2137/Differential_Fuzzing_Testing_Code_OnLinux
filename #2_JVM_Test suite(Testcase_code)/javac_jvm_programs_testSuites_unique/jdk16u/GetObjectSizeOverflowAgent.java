
import java.lang.instrument.*;

public class GetObjectSizeOverflowAgent {

    static Instrumentation instrumentation;

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        GetObjectSizeOverflowAgent.instrumentation = instrumentation;
    }

    public static void main(String[] args) throws Exception {
        int[] a = new int[600_000_000];
        long size = instrumentation.getObjectSize(a);

        if (size < 2_400_000_000L) {
            throw new RuntimeException("Invalid size of array, expected >= 2400000000, got " + size);
        }

        System.out.println("GetObjectSizeOverflow passed");
    }
}
