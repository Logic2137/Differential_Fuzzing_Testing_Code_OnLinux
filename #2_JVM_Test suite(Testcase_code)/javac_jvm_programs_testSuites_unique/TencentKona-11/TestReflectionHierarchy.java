









public class TestReflectionHierarchy {

    static class NestedA extends ExternalSuper {
        static final String ID =  "NestedA::priv_invoke";
        private String priv_invoke() {
            return ID;
        }
        static void checkA(NestedA a) throws Throwable {
            verifyEquals((String)NestedA.class.
                         getDeclaredMethod("priv_invoke",
                                           new Class<?>[0]).
                         invoke(a, new Object[0]),
                         NestedA.ID);
        }
    }

    static class NestedB extends NestedA {
        static final String ID =  "NestedB::priv_invoke";
        private String priv_invoke() {
            return ID;
        }
        static void checkA(NestedA a) throws Throwable {
            verifyEquals((String)NestedA.class.
                         getDeclaredMethod("priv_invoke",
                                           new Class<?>[0]).
                         invoke(a, new Object[0]),
                         NestedA.ID);
        }
    }

    static class NestedC extends NestedB {
        static final String ID =  "NestedC::priv_invoke";
        private String priv_invoke() {
            return ID;
        }
        static void checkA(NestedA a) throws Throwable {
            verifyEquals((String)NestedA.class.
                         getDeclaredMethod("priv_invoke",
                                           new Class<?>[0]).
                         invoke(a, new Object[0]),
                         NestedA.ID);
        }
    }

    static void checkA(NestedA a) throws Throwable {
            verifyEquals((String)NestedA.class.
                         getDeclaredMethod("priv_invoke",
                                           new Class<?>[0]).
                         invoke(a, new Object[0]),
                         NestedA.ID);
    }

    static void checkB(NestedB b) throws Throwable {
            verifyEquals((String)NestedB.class.
                         getDeclaredMethod("priv_invoke",
                                           new Class<?>[0]).
                         invoke(b, new Object[0]),
                         NestedB.ID);
    }

    static void checkC(NestedC c) throws Throwable {
            verifyEquals((String)NestedC.class.
                         getDeclaredMethod("priv_invoke",
                                           new Class<?>[0]).
                         invoke(c, new Object[0]),
                         NestedC.ID);
    }


    
    
    

    static void checkExternalSuper(ExternalSuper s) throws Throwable {
        try {
            ExternalSuper.class.
                getDeclaredMethod("priv_invoke", new Class<?>[0]).
                invoke(s, new Object[0]);
            throw new Error("Unexpected access to ExternalSuper.priv_invoke");
        }
        catch (IllegalAccessException iae) {
            System.out.println("Got expected exception accessing ExternalSuper.priv_invoke:" + iae);
        }
    }

    static void checkExternalSub(ExternalSub s) throws Throwable {
        try {
            ExternalSub.class.
                getDeclaredMethod("priv_invoke", new Class<?>[0]).
                invoke(s, new Object[0]);
            throw new Error("Unexpected access to ExternalSub.priv_invoke");
        }
        catch (IllegalAccessException iae) {
            System.out.println("Got expected exception accessing ExternalSub.priv_invoke:" + iae);
        }
    }

    static void verifyEquals(String actual, String expected) {
        if (!actual.equals(expected)) {
            throw new Error("Expected " + expected + " but got " + actual);
        }
        System.out.println("Check passed for " + expected);
    }

    public static void main(String[] args) throws Throwable {
        NestedA a = new NestedA();
        NestedB b = new NestedB();
        NestedC c = new NestedC();
        ExternalSub sub = new ExternalSub();
        ExternalSuper sup = new ExternalSuper();

        checkExternalSuper(sup);
        checkExternalSuper(a);
        checkExternalSuper(b);
        checkExternalSuper(c);
        checkExternalSuper(sub);

        checkA(a);
        checkA(b);
        checkA(c);
        checkA(sub);

        NestedA.checkA(a);
        NestedA.checkA(b);
        NestedA.checkA(c);
        NestedA.checkA(sub);

        NestedB.checkA(a);
        NestedB.checkA(b);
        NestedB.checkA(c);
        NestedB.checkA(sub);

        NestedC.checkA(a);
        NestedC.checkA(b);
        NestedC.checkA(c);
        NestedC.checkA(sub);

        checkB(b);
        checkB(c);
        checkB(sub);

        checkC(c);
        checkC(sub);

        checkExternalSub(sub);
    }
}




class ExternalSuper {
    static final String ID =  "ExternalSuper::priv_invoke";
    private String priv_invoke() {
        return ID;
    }
}


class ExternalSub extends TestReflectionHierarchy.NestedC {
    static final String ID =  "ExternalSub::priv_invoke";
    private String priv_invoke() {
        return ID;
    }
}
