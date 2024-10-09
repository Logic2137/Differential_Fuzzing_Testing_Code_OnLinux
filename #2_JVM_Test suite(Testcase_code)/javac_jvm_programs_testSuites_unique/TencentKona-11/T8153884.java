

class T8153884 {
    void test() {
        Runnable r = () -> (foo());
    }

    void foo() { }
}
