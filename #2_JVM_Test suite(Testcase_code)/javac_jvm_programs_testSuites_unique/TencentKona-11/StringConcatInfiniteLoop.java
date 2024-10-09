



public class StringConcatInfiniteLoop {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        test(sb, "foo", "bar", true);
    }

    private static void test(Object v, String s1, String s2, boolean flag) {
        if (flag) {
            return;
        }
        int i = 0;
        for (; i < 10; i++);
        if (i == 10) {
            v = null;
        }
        StringBuilder sb = new StringBuilder(s1);
        sb.append(s2);
        while (v == null);
    }

    private static class A {
    }
}
