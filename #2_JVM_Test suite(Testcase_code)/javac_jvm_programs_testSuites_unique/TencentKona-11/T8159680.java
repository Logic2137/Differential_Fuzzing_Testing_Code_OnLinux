



class T8159680 {

    static class Condition<T> {}

    @SafeVarargs
    static <T> Condition<T> allOf(Condition<? super T>... conditions) {
        return null;
    }

    @SafeVarargs
    static void test(Condition<? super Number>... conditions) {
        allOf(conditions);
    }
}
