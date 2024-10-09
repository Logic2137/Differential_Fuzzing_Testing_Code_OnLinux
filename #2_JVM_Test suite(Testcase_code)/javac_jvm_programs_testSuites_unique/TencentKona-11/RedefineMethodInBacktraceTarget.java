


public class RedefineMethodInBacktraceTarget {
    public static void methodToRedefine() {
        throw new RuntimeException("Test exception");
    }

    public static void callMethodToDelete() {
        methodToDelete();
    }

    private static void methodToDelete() {
        throw new RuntimeException("Test exception in methodToDelete");
    }

}
