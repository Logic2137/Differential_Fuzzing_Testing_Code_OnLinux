import java.util.*;

public class MissingCast2 {

    public static void main(String[] args) {
        new E();
    }
}

class S<T> {

    T t;
}

class C {

    class I {
    }
}

class E extends S<C> {

    {
        t = new C();
        t.new I() {
        };
    }
}
