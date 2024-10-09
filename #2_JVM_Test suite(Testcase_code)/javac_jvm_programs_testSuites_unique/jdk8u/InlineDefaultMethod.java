interface InterfaceWithDefaultMethod0 {

    default public int defaultMethod() {
        return 1;
    }
}

interface InterfaceWithDefaultMethod1 extends InterfaceWithDefaultMethod0 {
}

abstract class Subtype implements InterfaceWithDefaultMethod1 {
}

class Decoy extends Subtype {

    public int defaultMethod() {
        return 2;
    }
}

class Instance extends Subtype {
}

public class InlineDefaultMethod {

    public static int test(InterfaceWithDefaultMethod1 x) {
        return x.defaultMethod();
    }

    public static void main(String[] args) {
        InterfaceWithDefaultMethod1 a = new Decoy();
        InterfaceWithDefaultMethod1 b = new Instance();
        if (test(a) != 2 || test(b) != 1) {
            System.err.println("FAILED");
            System.exit(97);
        }
        System.err.println("PASSED");
    }
}
