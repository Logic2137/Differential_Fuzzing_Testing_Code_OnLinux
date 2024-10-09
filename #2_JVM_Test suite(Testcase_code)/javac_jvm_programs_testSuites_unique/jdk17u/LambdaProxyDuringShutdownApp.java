class Outer {

    static final class Inner {

        static {
            doit(() -> {
                System.out.println("Hello from Inner");
            });
        }

        static void doit(Runnable t) {
            t.run();
        }
    }
}

class MyShutdown extends Thread {

    public void run() {
        Outer.Inner inner = new Outer.Inner();
    }
}

public class LambdaProxyDuringShutdownApp {

    public static void main(String[] args) throws Exception {
        Runtime r = Runtime.getRuntime();
        r.addShutdownHook(new MyShutdown());
        System.exit(0);
    }
}
