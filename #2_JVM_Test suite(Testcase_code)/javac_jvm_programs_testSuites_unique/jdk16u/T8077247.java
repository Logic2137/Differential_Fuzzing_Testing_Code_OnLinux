


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class T8077247 {
    public static void test() {
        int x = add(add(add(add(add(add(add(add(add(add(1, 2), 3), 4), 5), 6), 7), 8), 9), 10), 11);
    }

    public static int add(int x, int y) {
        long rslt = (long)x + (long)y;
        if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
            return (int)rslt;
        }

        String msg = String.format("Integer overflow: %d + %d.", x, y);
        throw new IllegalArgumentException(msg);
    }

    public static double add(double x, double y) {
        double rslt = x + y;
        if (Double.isInfinite(rslt)) {
            String msg = String.format("Real overflow: %s + %s.", x, y);
            throw new IllegalArgumentException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    public static <T> List<T> add(List<T> x, List<T> y) {
        List<T> rslt = new ArrayList<>(x.size() + y.size());
        rslt.addAll(x);
        rslt.addAll(y);
        return rslt;
    }

    public static String add(String x, String y) {
        return x + y;
    }

    public static <K, V> Map<K, V> add(Map<K, V> x, Map<K, V> y) {
        Map<K, V> rslt = new HashMap<>(x);
        rslt.putAll(y);
        return rslt;
    }
}
