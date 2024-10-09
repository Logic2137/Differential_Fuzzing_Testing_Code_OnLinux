

class HelloStaticInner {

    static class InnerHello {
        public static void m1() {
            doit(() -> {
                System.out.println("Hello from Lambda");
            });
        }
        static void doit(Runnable t) {
            t.run();
        }
    }
}

public class StaticInnerApp {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("dump")) {
            Object o = new HelloStaticInner();
        }
        HelloStaticInner.InnerHello.m1();
    }
}
