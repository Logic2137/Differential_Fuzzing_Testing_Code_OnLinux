



class A8184989_2 {
    public boolean test() {
        return true;
    }
    class AA {
        public AA(Condition8184989_2<AA> condition) {
            if (condition.check(this) != true) {
                throw new AssertionError("Incorrect output");
            }
        }
    }
}

interface Condition8184989_2<T> {
    boolean check(T t);
}

public class LambdaInSuperCallCapturingOuterThis2 extends A8184989_2 {
    public boolean test() {return false;}
    public void b() {}

    class C extends A8184989_2 {
        public class BA extends AA {
            public BA() {
                super(o -> {b(); return test();});
            }
        }
    }
    public static void main(String[] args) {
        new LambdaInSuperCallCapturingOuterThis2().new C().new BA();
    }
}
