public class TestPrintGCDetailsVerbose {

    public static void main(String[] args) {
        for (int t = 0; t <= 10; t++) {
            byte[][] a = new byte[100000][];
            try {
                for (int i = 0; i < a.length; i++) {
                    a[i] = new byte[100000];
                }
            } catch (OutOfMemoryError oome) {
                a = null;
                System.out.println("OOM!");
                continue;
            }
        }
    }
}
