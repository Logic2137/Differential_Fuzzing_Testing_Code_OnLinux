

public class LambdaWithMethod {
    public static void run(Runnable r) {
        run(new Runnable() {
            public void run() {
                put(get());
            }
        });
    }
    private static String get() { return null; }
    private static void put(String i) {}
}
