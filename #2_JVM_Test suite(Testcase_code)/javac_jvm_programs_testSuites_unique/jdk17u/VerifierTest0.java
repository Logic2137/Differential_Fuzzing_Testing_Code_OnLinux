public class VerifierTest0 {

    public static void main(String[] args) {
        boolean good = true;
        good &= mustBeInvalid("VerifierTestA");
        good &= mustBeInvalid("VerifierTestB");
        good &= mustBeInvalid("VerifierTestC");
        good &= mustBeInvalid("VerifierTestD");
        good &= mustBeInvalid("VerifierTestE");
        if (!good) {
            System.out.println("VerifierTest0 failed");
            System.exit(1);
        }
    }

    static boolean mustBeInvalid(String className) {
        System.out.println("Testing: " + className);
        try {
            Class.forName(className);
            System.out.println("ERROR: class " + className + " was loaded unexpectedly.");
            return false;
        } catch (Throwable t) {
            System.out.println("Expected exception:");
            t.printStackTrace();
            return true;
        }
    }
}

class UnverifiableBase {

    static final VerifierTest0 x = new VerifierTest0();
}

interface UnverifiableIntf {

    static final VerifierTest0 x = new VerifierTest0();
}

interface UnverifiableIntfSub extends UnverifiableIntf {
}

class VerifierTestA extends UnverifiableBase {
}

class VerifierTestB extends VerifierTestA {
}

class VerifierTestC implements UnverifiableIntf {
}

class VerifierTestD extends VerifierTestC {
}

class VerifierTestE implements UnverifiableIntfSub {
}
