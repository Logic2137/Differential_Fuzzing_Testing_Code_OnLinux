
package e1;

public class CrackM5Access {

    private static void privateMethod() {
    }

    static void packageMethod() {
    }

    public static void addReads(Module m) {
        CrackM5Access.class.getModule().addReads(m);
    }
}
