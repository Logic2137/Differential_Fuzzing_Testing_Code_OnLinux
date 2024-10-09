public class Test6859338 {

    static Object[] o = new Object[] { new Object(), null };

    public static void main(String[] args) {
        int total = 0;
        try {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < 40000; i++) {
                int limit = o.length;
                if (i < 20000)
                    limit = 1;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
                for (int j = 0; j < limit; j++) {
                    total += o[j].hashCode();
                }
            }
        } catch (NullPointerException e) {
        }
    }
}
