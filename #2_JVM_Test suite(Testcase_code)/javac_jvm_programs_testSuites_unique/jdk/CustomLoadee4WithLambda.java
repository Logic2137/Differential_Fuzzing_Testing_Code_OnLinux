

public class CustomLoadee4WithLambda {
    public static void test() {
        doit(() -> {
                System.out.println("Hello inside a Lambda expression");
            });
    }

    static void doit(Runnable r) {
        r.run();
    }
}
