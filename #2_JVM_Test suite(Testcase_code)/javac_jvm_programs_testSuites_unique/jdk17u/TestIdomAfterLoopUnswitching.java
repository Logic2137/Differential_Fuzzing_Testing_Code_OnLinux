public class TestIdomAfterLoopUnswitching {

    public static void main(String[] k) {
        test1();
        test2();
    }

    public static void test1() {
        float h = 0;
        for (int j = 0; j < 3; ++j) {
            float k = 9;
            float[] fla = new float[2];
            for (int n = 0; n < 5; ++n) {
                if (j >= 1) {
                    if (n <= 1) {
                        h += k;
                    }
                }
            }
            for (int l12 = 0; l12 < 9; ++l12) {
                for (int o = 0; o < 1; ++o) {
                    fla[0] += 1.0f;
                }
            }
        }
    }

    public static void test2() {
        float[] fla = new float[1000];
        for (int i = 0; i < 1000; i++) {
            for (float fl2 : fla) {
                fla[100] = 1.0f;
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 14; j++) {
                fla[2] = fla[j];
            }
        }
    }
}
