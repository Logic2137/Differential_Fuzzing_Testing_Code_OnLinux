import java.util.*;

public class MethodReference01 {

    interface Getter<U, T> {

        public U get(T t);
    }

    static class Foo {

        private Integer a;

        private String b;

        Foo(Integer a, String b) {
            this.a = a;
            this.b = b;
        }

        static Integer getA(Foo f) {
            return f.a;
        }

        static String getB(Foo f) {
            return f.b;
        }
    }

    public static <T, U extends Comparable<? super U>> void sortBy(List<T> s, final Getter<U, T> getter) {
        Collections.sort(s, new Comparator<T>() {

            public int compare(T t1, T t2) {
                return getter.get(t1).compareTo(getter.get(t2));
            }
        });
    }

    public static void main(String[] args) {
        List<Foo> c = new ArrayList<Foo>();
        c.add(new Foo(2, "Hello3!"));
        c.add(new Foo(3, "Hello1!"));
        c.add(new Foo(1, "Hello2!"));
        checkSortByA(c);
        checkSortByB(c);
    }

    static void checkSortByA(List<Foo> l) {
        sortBy(l, Foo::getA);
        int oldA = -1;
        for (Foo foo : l) {
            if (foo.a.compareTo(oldA) < 1) {
                throw new AssertionError();
            }
        }
    }

    static void checkSortByB(List<Foo> l) {
        sortBy(l, Foo::getB);
        String oldB = "";
        for (Foo foo : l) {
            if (foo.b.compareTo(oldB) < 1) {
                throw new AssertionError();
            }
        }
    }
}
