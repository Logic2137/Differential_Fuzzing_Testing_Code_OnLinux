public class Test8211698 {

    public static void main(String[] args) {
        Test8211698 issue = new Test8211698();
        for (int i = 0; i < 10000; i++) {
            issue.test();
        }
    }

    public void test() {
        int[] iarr1 = new int[888];
        for (int i = 5; i > 0; i--) {
            for (int j = 0; j <= i - 1; j++) {
                int istep = 2 * j - i;
                int iadj = 0;
                if (istep < 0) {
                    iadj = iarr1[-istep];
                } else {
                    iadj = iarr1[istep];
                }
            }
        }
    }
}
