public class TestDeadBackbranchArrayAccess {

    static char[] pattern0 = { 0 };

    static char[] pattern1 = { 1 };

    static void test(char[] array) {
        if (pattern1 == null)
            return;
        int i = 0;
        int pos = 0;
        char c = array[pos];
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        while (i >= 0 && (c == pattern0[i] || c == pattern1[i])) {
            i--;
            pos--;
            if (pos != -1) {
                c = array[pos];
            }
        }
    }

    public static void main(String[] args) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 1000000; i++) {
            test(new char[1]);
        }
    }
}
