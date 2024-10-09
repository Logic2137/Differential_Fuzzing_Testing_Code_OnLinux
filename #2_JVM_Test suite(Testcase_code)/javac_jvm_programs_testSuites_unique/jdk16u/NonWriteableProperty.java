



public class NonWriteableProperty {
    public static void main(String[] args) {
        if (System.getProperty(args[0]).equals(args[1])) {
            throw new RuntimeException("Non-writeable system property " +
                                       args[0] + " was rewritten");
        }
    }
}
