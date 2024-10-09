



class T8169345c {
    void test() {
        final int b;
        b = 10;
        class Local1 {
            public String toString() {
                return "" + b;
            }
        }
        class Local2 {
            void test() {
                final int b;
                b = 20;
                class DeepLocal extends Local1 {
                    public String toString() {
                        return "" + b;
                    }
                }
            }
        }
    }
}
