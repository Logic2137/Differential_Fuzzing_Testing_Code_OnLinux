class Neg16 {

    interface Predicate<T> {

        default boolean test(T t) {
            System.out.println("Default method");
            return false;
        }
    }

    static void someMethod(Predicate<? extends Number> p) {
        if (!p.test(null))
            throw new Error("Blew it");
    }

    public static void main(String[] args) {
        someMethod(new Predicate<Integer>() {

            public boolean test(Integer n) {
                System.out.println("Override");
                return true;
            }
        });
        someMethod(new Predicate<Number>() {

            public boolean test(Number n) {
                System.out.println("Override");
                return true;
            }
        });
    }
}
