public class CallMultipleCatchProjs {

    private static A field = new A();

    public static void main(String[] args) throws Exception {
        Exception3 exception3 = new Exception3();
        test(new Exception2());
    }

    static int test(Exception exception) throws Exception {
        try {
            throw exception;
        } catch (Exception1 e1) {
            return 1;
        } catch (Exception2 e2) {
            return field.i + 2;
        } catch (Exception3 e3) {
            return field.i + 3;
        }
    }

    private static class Exception1 extends Exception {
    }

    private static class Exception2 extends Exception {
    }

    private static class Exception3 extends Exception {
    }

    private static class A {

        public int i;
    }
}
