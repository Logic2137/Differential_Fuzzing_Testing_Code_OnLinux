



public class T8169345a {
    void test() {
        Object o = new Object();
        class Local1 {
            Object test1() {
                return o;
            }
        }
        class Local2 {
            void test2() {
                Object o = new Object();
                class Local3 extends Local1 {
                    Object test3() {
                        return o;
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Class.forName("T8169345a$1Local1");
        Class.forName("T8169345a$1Local2$1Local3");
        Class.forName("T8169345a$1Local2");
    }
}
