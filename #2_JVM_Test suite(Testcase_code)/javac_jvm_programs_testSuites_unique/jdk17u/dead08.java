
package vm.compiler.jbe.dead.dead08;

class struct {

    int i1 = 2;

    int i2 = 3;

    int i3 = 4;

    int i4 = 5;

    int i5 = 6;

    int i6 = 7;

    int i7 = 8;

    int i8 = 9;
}

public class dead08 {

    boolean bol = true;

    public static void main(String[] args) {
        dead08 dce = new dead08();
        System.out.println("f()=" + dce.f() + "; fopt()=" + dce.fopt());
        if (dce.f() == dce.fopt()) {
            System.out.println("Test dead08 Passed.");
        } else {
            throw new Error("Test dead08 Failed: f()=" + dce.f() + " != fopt()=" + dce.fopt());
        }
    }

    int f() {
        struct s = new struct();
        if (bol)
            s.i1 = 1;
        if (bol)
            s.i2 = 2;
        if (bol)
            s.i3 = 3;
        if (bol)
            s.i4 = 4;
        if (bol)
            s.i5 = 5;
        if (bol)
            s.i6 = 6;
        if (bol)
            s.i7 = 7;
        if (bol)
            s.i8 = 8;
        if (bol)
            s.i1 = 1;
        if (bol)
            s.i2 = 2;
        if (bol)
            s.i3 = 3;
        if (bol)
            s.i4 = 4;
        if (bol)
            s.i5 = 5;
        if (bol)
            s.i6 = 6;
        if (bol)
            s.i7 = 7;
        if (bol)
            s.i8 = 8;
        if (bol)
            s.i1 = 1;
        if (bol)
            s.i2 = 2;
        if (bol)
            s.i3 = 3;
        if (bol)
            s.i4 = 4;
        if (bol)
            s.i5 = 5;
        if (bol)
            s.i6 = 6;
        if (bol)
            s.i7 = 7;
        if (bol)
            s.i8 = 8;
        return s.i8;
    }

    int fopt() {
        struct s = new struct();
        s.i8 = 8;
        return s.i8;
    }
}
