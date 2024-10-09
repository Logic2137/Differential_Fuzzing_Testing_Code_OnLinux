public class Test6885584 {

    static private int i1;

    static private int i2;

    static private int i3;

    static int limit = Integer.MAX_VALUE - 8;

    public static void main(String[] args) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int j = 200000; j != 0; j--) {
        }
        i1 = i2;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int k = Integer.MAX_VALUE - 1; k != 0; k--) {
            if (i2 > i3)
                i1 = k;
            if (k <= limit)
                break;
        }
    }
}
