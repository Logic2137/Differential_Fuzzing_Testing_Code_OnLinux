public class AttachWithStalePidFileTarget {

    static final String READY_MSG = "*ready*";

    public static void main(String... args) throws Exception {
        System.out.println(READY_MSG);
        System.out.flush();
        System.in.read();
    }
}
