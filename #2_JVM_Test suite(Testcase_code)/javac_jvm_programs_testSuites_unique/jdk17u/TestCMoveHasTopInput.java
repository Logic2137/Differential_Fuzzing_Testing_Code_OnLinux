public class TestCMoveHasTopInput {

    public static boolean[] arr = new boolean[20];

    public void vMeth(long l) {
        for (int a = 2; a < 155; a++) {
            for (int b = 1; b < 10; ++b) {
                for (int c = 1; c < 2; c++) {
                    l += 3 * l;
                    arr[b - 1] = false;
                    switch(a) {
                        case 14:
                        case 17:
                            l -= b;
                            break;
                    }
                }
            }
        }
    }

    public static void main(String... args) {
        TestCMoveHasTopInput test = new TestCMoveHasTopInput();
        for (int i = 0; i < 10; i++) {
            test.vMeth(i);
        }
    }
}
