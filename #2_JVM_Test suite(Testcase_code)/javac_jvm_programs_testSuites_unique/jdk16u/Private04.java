




public class Private04 {
    @FunctionalInterface
    interface SubRunnable extends Runnable {
        private void run(int x) {
            SubRunnable s = () -> {};
        }
    }

    public static void main(String [] args) {
        SubRunnable s = () -> {};
    }
}
