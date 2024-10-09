class T8170410 {

    interface CheckedSupplier<T extends Throwable, R> {

        R get() throws T;
    }

    static <T extends Throwable, R> CheckedSupplier<T, R> checked(CheckedSupplier<T, R> checkedSupplier) {
        return checkedSupplier;
    }

    static void test() {
        checked(() -> null).get();
        checked(T8170410::m).get();
    }

    static String m() {
        return "";
    }
}
