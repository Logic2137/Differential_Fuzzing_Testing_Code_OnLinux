public class StackOverflowBug {

    public static int run() {
        try {
            try {
                return run();
            } catch (Throwable e) {
                return 42;
            }
        } finally {
        }
    }

    public static void main(String[] argv) {
        run();
    }
}
