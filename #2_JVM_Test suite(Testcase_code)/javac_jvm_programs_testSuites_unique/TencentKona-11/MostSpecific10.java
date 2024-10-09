



public class MostSpecific10 {

    public static void main(String[] args) {
        new MostSpecific10().test(true);
    }

    interface GetInt {
        int get();
    }

    interface GetInteger {
        Integer get();
    }

    void m(GetInt getter) {}
    void m(GetInteger getter) {
        throw new AssertionError("Less-specific method invocation: " + getter.getClass());
    }

    void test(boolean cond) {
        m(() -> 23);
        m("abc"::length);
        m(( () -> 23 ));
        m(( "abc"::length ));
        m(cond ? () -> 23 : "abc"::length);
        m(( cond ? () -> 23 : "abc"::length ));
        m(cond ? (() -> 23) : ("abc"::length) );
        m(( cond ? () -> 23 : cond ? ("abc"::length) : (() -> 23) ));
    }

}
