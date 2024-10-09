class T8157149b {

    void test() {
        computeException1(this::computeThrowable);
        computeException2(this::computeThrowable);
        computeException1(() -> {
            Integer integer = computeThrowable();
            return integer;
        });
        computeException2(() -> {
            Integer integer = computeThrowable();
            return integer;
        });
    }

    <T, E extends Exception> void computeException1(ThrowableComputable1<T, E> c) throws E {
    }

    <T, E extends Exception> void computeException2(ThrowableComputable2<T, E> c) throws E {
    }

    <E1 extends Throwable> Integer computeThrowable() throws E1 {
        return 0;
    }

    interface ThrowableComputable1<T, E extends Throwable> {

        T compute() throws E;
    }

    interface ThrowableComputable2<T, E extends Throwable> {

        Integer compute() throws E;
    }
}
