




public class TestStripMinedBackToBackIfs {
    public static void main(String[] args) {
        for (int i = 0; i < 20_000; i++) {
            test(100);
        }
    }

    private static double test(int limit) {
        double v = 1;
        for (int i = 0; i < limit; i++) {
            v = v * 4;
            
            
            if (i+1 < limit) {
                v = v * 2;
            }
        }
        return v;
    }
}
