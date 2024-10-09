



public class StaticFieldsOnInterface {
    

    public interface A {
        public static final int CONSTANT = 42;
    }

    public interface B extends A {
    }

    public interface C extends A {
    }

    public interface D extends B, C {
    }

    static class X implements A {}
    static class Y extends X implements A {}

    public static void main(String[] args) throws Exception {
        char first = 'C';
        if (args.length > 0) {
            first = args[0].charAt(0);
        }

        assertOneField(A.class);
        
        if (first == 'D') {
            assertOneField(D.class);
            assertOneField(C.class);
        }
        
        else if (first == 'C') {
            assertOneField(C.class);
            assertOneField(D.class);
        }
        else {
            assertOneField(Y.class);
        }
    }

    static void assertOneField(Class<?> c) {
        int nfs = c.getFields().length;
        if (nfs != 1) {
            throw new AssertionError(String.format(
                    "Class %s does not have exactly one field: %d", c.getName(), nfs));
        }
    }
}
