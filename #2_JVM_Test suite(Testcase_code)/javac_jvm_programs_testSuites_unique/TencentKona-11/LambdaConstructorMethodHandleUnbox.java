



public class LambdaConstructorMethodHandleUnbox {
    interface IntFunction<X> {
        int m(X x);
    }

    public static void main(String[] args) {
       IntFunction<String> s = Integer::new;
       if (s.m("2000") + s.m("13") != 2013) {
           throw new RuntimeException("Lambda conversion failure");
       }
    }
}
