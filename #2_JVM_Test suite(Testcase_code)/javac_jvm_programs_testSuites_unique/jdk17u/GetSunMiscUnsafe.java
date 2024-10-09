public class GetSunMiscUnsafe {

    public static void main(String[] args) throws Exception {
        try {
            sun.misc.Unsafe.getUnsafe();
        } catch (SecurityException x) {
            System.err.println("Thrown as expected: " + x);
            return;
        }
        throw new Exception("No exception thrown");
    }
}
