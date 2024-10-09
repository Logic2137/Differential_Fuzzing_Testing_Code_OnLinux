
package vm.compiler.jbe.dead.dead07;

public class dead07 {

    boolean bol = true;

    static int i = 6;

    public static void main(String[] args) {
        dead07 dce = new dead07();
        System.out.println("f()=" + dce.f() + "; fopt()=" + dce.fopt());
        if (dce.f() == dce.fopt()) {
            System.out.println("Test dead07 Passed.");
        } else {
            throw new Error("Test dead07 Failed: f()=" + dce.f() + " != fopt()=" + dce.fopt());
        }
    }

    int f() {
        if (bol)
            i = 1;
        if (bol)
            i = 2;
        if (bol)
            i = 3;
        if (bol)
            i = 4;
        if (bol)
            i = 5;
        if (bol)
            i = 6;
        if (bol)
            i = 7;
        if (bol)
            i = 8;
        if (bol)
            i = 1;
        if (bol)
            i = 2;
        if (bol)
            i = 3;
        if (bol)
            i = 4;
        if (bol)
            i = 5;
        if (bol)
            i = 6;
        if (bol)
            i = 7;
        if (bol)
            i = 8;
        if (bol)
            i = 1;
        if (bol)
            i = 2;
        if (bol)
            i = 3;
        if (bol)
            i = 4;
        if (bol)
            i = 5;
        if (bol)
            i = 6;
        if (bol)
            i = 7;
        if (bol)
            i = 8;
        return i;
    }

    int fopt() {
        i = 8;
        return i;
    }
}
