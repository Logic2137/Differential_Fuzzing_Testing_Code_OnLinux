



import java.security.*;
import javax.security.auth.Subject;

public class Generic {
    public static void main(String[] args) throws Exception {
        String foo = Subject.doAs(new Subject(),
                                new A1<String>("foo"));

        Integer one = Subject.doAs(new Subject(),
                                new A2<Integer>(new Integer("1")));

        Boolean troo = Subject.doAsPrivileged(new Subject(),
                                new A1<Boolean>(new Boolean("true")),
                                AccessController.getContext());

        Generic gen = Subject.doAsPrivileged(new Subject(),
                                new A2<Generic>(new Generic()),
                                AccessController.getContext());
    }

    private static class A1<T> implements PrivilegedAction<T> {
        T t;

        public A1(T t) {
            this.t = t;
        }

        public T run() {
            return t;
        }
    }

    private static class A2<T> implements PrivilegedExceptionAction<T> {
        T t;

        public A2(T t) {
            this.t = t;
        }

        public T run() throws PrivilegedActionException {
            return t;
        }
    }
}
