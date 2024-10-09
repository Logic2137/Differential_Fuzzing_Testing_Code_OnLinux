

public class NestApp {

    public static void main(String[] args) throws Exception {
        Class<NestApp.InnerA.InnerInnerA> clazzInnerInnerA = NestApp.InnerA.InnerInnerA.class;
        NestApp.InnerA.InnerInnerA iia = (NestApp.InnerA.InnerInnerA)clazzInnerInnerA.newInstance();
    }

    public NestApp(String type, int weight) {
    }

    static class InnerA {

        static class InnerInnerA {
            static {
                doit(() -> {
                    System.out.println("Hello from InnerInnerA");
                });
            }
        }
    }

    public class InnerB {
    }

    static void doit(Runnable t) {
        t.run();
    }
}
