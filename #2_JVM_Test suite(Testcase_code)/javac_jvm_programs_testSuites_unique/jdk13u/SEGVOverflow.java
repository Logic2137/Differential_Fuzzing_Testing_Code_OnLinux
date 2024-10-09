public class SEGVOverflow {

    static {
        System.loadLibrary("overflow");
    }

    native static String nativesegv();

    public static void main(String[] args) {
        String str = nativesegv();
        if (str == null) {
            System.out.println("FAILED: malloc returned null");
        } else {
            System.out.println(str);
        }
    }
}
