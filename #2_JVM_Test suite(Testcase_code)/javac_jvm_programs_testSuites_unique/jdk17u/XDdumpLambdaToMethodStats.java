public class XDdumpLambdaToMethodStats {

    public static void main(String... a) {
        new XDdumpLambdaToMethodStats().run();
    }

    public void run() {
        printHash(this::m);
        printHash(XDdumpLambdaToMethodStats::sm);
        printHash(() -> {
            o = new Object();
        });
        printHash(() -> {
            s = new Object();
        });
    }

    private void printHash(Runnable function) {
        System.out.println(function + "; hash=" + function.hashCode());
    }

    private static void sm() {
    }

    private void m() {
    }

    private Object o;

    private static Object s;
}
