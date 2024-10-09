class T8255968_8_Outer {

    void m() {
    }

    void m(String s) {
    }

    class T8255968_8_Inner extends T8255968_8_Sup {

        void test() {
            m();
        }
    }
}

class T8255968_8_Sup {

    private void m(String s) {
    }

    private void m() {
    }
}
