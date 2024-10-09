



public class TestCMovSplitThruPhi {
    static int f;

    static int test(boolean flag1, boolean flag2, boolean flag3, boolean flag4) {
        int v3 = 0;
        if (flag4) {
            for (int i = 0; i < 10; i++) {
                int v1 = 0;
                if (flag1) {
                    v1 = not_inlined();
                }
                
                int v2 = v1;
                if (flag2) {
                    v2 = f + v1;
                }
                
                if (flag3) {
                    v3 = v2 * 2;
                    break;
                }
            }
        }
        return v3;
    }

    private static int not_inlined() {
        return 0;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20000; i++) {
            test((i % 2) == 0, (i % 2) == 0, (i % 100) == 1, (i % 1000) == 1);
        }
    }
}
