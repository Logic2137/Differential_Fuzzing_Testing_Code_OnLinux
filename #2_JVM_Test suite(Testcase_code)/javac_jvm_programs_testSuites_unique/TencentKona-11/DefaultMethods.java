



interface A {

    default int getOne() {
        return 1;
    }
}

interface B extends A {

}

interface C extends B {

    @Override
    default int getOne() {
        return 2;
    }
}

abstract class Abstract implements C {
}

class Impl extends Abstract {

    @Override
    public int getOne() {
        return 3;
    }
}

class Impl2 extends Impl {

    public static final int expectedValue = 4;

    @Override
    public int getOne() {
        return expectedValue;
    }
}

public class DefaultMethods {

    static {
        System.loadLibrary("DefaultMethods");
    }

    static native int callAndVerify(Impl impl, String className, int expectedResult, int implExpectedResult);

    
    public static void main(String[] args) {
        Impl2 impl2 = new Impl2();
        if (args.length == 0) {
            callAndVerify(impl2, "A", 1, Impl2.expectedValue);
            callAndVerify(impl2, "B", 1, Impl2.expectedValue);
            callAndVerify(impl2, "C", 2, Impl2.expectedValue);
            callAndVerify(impl2, "Abstract", 2, Impl2.expectedValue);
            callAndVerify(impl2, "Impl", 3, Impl2.expectedValue);
            callAndVerify(impl2, "Impl2", 4, Impl2.expectedValue);
        } else {
            verifyAndRun(args, impl2, Impl2.expectedValue);
        }
    }

    
    static void verifyAndRun(String[] args, Impl2 impl, int expectedValue) {
        if (args.length != 2) {
            throw new RuntimeException("invalid number of input arguments");
        }

        String className = args[0];
        int expectedResult = Integer.parseInt(args[1]);

        callAndVerify(impl, className, expectedResult, expectedValue);
    }
}
