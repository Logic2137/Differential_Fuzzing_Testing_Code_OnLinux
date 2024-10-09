public class Test6897150 {

    public static void main(String[] args) {
        loopAndPrint(Integer.MAX_VALUE - 1);
        loopAndPrint(Integer.MAX_VALUE);
    }

    static void verify(int max, int a) {
        if (a != (max - 1)) {
            System.out.println("Expected: " + (max - 1));
            System.out.println("Actual  : " + a);
            System.exit(97);
        }
    }

    static void loopAndPrint(int max) {
        int a = -1;
        int i = 1;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (; i < max; i++) {
            a = i;
        }
        verify(max, a);
    }
}
