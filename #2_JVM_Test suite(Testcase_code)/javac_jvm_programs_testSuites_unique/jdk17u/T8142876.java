class T8142876 {

    interface I<R extends Runnable, T> {

        void m();
    }

    void test() {
        I<? extends O, String> succeed = this::ff;
        I<? extends Comparable<String>, String> failed = this::ff;
    }

    interface O {
    }

    private void ff() {
    }
}
