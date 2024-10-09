
package vm.compiler.jbe.dead.dead15;

public class dead15 {

    int i00 = 0, i01 = 1, i02 = 2, i03 = 3, i04 = 4;

    int i05 = 5, i06 = 6, i07 = 7, i08 = 8, i09 = 9;

    int i10 = 10, i11 = 11, i12 = 12, i13 = 13, i14 = 14;

    int i15 = 15, i16 = 16, i17 = 17, i18 = 18, i19 = 19;

    public static void main(String[] args) {
        dead15 dce = new dead15();
        System.out.println("f()=" + dce.f() + "; fopt()=" + dce.fopt());
        if (dce.f() == dce.fopt()) {
            System.out.println("Test dead15 Passed.");
        } else {
            throw new Error("Test dead15 Failed: f()=" + dce.f() + " != fopt()=" + dce.fopt());
        }
    }

    int f() {
        i00 = i00;
        i01 = i01;
        i02 = i02;
        i03 = i03;
        i04 = i04;
        i05 = i05;
        i06 = i06;
        i07 = i07;
        i08 = i08;
        i09 = i09;
        i10 = i10;
        i11 = i11;
        i12 = i12;
        i13 = i13;
        i14 = i14;
        i15 = i15;
        i16 = i16;
        i17 = i17;
        i18 = i18;
        i19 = i19;
        return i19;
    }

    int fopt() {
        return i19;
    }
}
