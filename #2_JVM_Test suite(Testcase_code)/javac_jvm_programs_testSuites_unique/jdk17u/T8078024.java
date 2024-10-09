import java.util.Arrays;
import java.util.List;

class T8078024 {

    public static <A> List<List<A>> cartesianProduct(List<? extends A>... lists) {
        return cartesianProduct(Arrays.asList(lists));
    }

    public static <B> List<List<B>> cartesianProduct(List<? extends List<? extends B>> lists) {
        return null;
    }
}
