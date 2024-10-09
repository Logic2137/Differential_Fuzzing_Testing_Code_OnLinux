public class Test6837011 {

    static boolean var_3 = true;

    public static void main(String[] args) {
        double var_5;
        char var_7 = 1;
        double var_11 = 0;
        do {
            var_11++;
            var_5 = (var_7 /= (var_3 ? ~1L : 3));
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        } while (var_11 < 1);
    }
}
