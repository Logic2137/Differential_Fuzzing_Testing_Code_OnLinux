import java.util.ArrayList;
import java.util.Collection;

class T8174249a {

    static <T> T foo(Class<T> c, Collection<? super T> baz) {
        return null;
    }

    static void bar(String c) {
    }

    void test() {
        bar(foo(String.class, new ArrayList<String>()));
        String s = foo(String.class, new ArrayList());
        bar(s);
        bar(foo(String.class, new ArrayList()));
    }
}
