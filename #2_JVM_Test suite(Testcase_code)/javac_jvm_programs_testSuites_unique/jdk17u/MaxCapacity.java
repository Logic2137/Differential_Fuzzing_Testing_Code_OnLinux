import java.io.ByteArrayOutputStream;

public class MaxCapacity {

    public static void main(String[] args) {
        long maxHeap = Runtime.getRuntime().maxMemory();
        if (maxHeap < 3L * Integer.MAX_VALUE) {
            System.out.printf("Skipping test; max memory %sM too small%n", maxHeap / (1024 * 1024));
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (long n = 0; ; n++) {
            try {
                baos.write((byte) 'x');
            } catch (Throwable t) {
                byte[] bytes = baos.toByteArray();
                if (bytes.length != n)
                    throw new AssertionError("wrong length");
                if (bytes[0] != 'x' || bytes[bytes.length - 1] != 'x')
                    throw new AssertionError("wrong contents");
                long gap = Integer.MAX_VALUE - n;
                System.out.printf("gap=%dM %d%n", gap / (1024 * 1024), gap);
                if (gap > 1024)
                    throw t;
                break;
            }
        }
    }
}
