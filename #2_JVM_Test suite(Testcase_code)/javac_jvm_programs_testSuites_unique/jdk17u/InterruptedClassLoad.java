public class InterruptedClassLoad {

    public static void main(String[] args) {
        class Empty {
        }
        Thread.currentThread().interrupt();
        new Empty();
    }
}
