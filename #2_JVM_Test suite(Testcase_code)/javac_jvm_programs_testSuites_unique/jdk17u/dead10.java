
package vm.compiler.jbe.dead.dead10;

public class dead10 {

    int i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13;

    boolean i14, i15, i16, i17, i18;

    int j = 21;

    boolean bol = true;

    public static void main(String[] args) {
        dead10 dce = new dead10();
        System.out.println("f()=" + dce.f() + "; fopt()=" + dce.fopt());
        if (dce.f() == dce.fopt()) {
            System.out.println("Test dead10 Passed.");
        } else {
            throw new Error("Test dead10 Failed: f()=" + dce.f() + " != fopt()=" + dce.fopt());
        }
    }

    int f() {
        int res;
        i1 = j + 1;
        i2 = j - 1;
        i3 = j * 3;
        i4 = j / 31;
        i5 = j % 71;
        i6 = j << 3;
        i7 = j >> 4;
        i8 = j >>> 5;
        i9 = bol ? 7 : 9;
        i10 = ~j;
        i11 = j & 3;
        i12 = j | 4;
        i13 = j ^ 4;
        i14 = bol && (j < 3);
        i15 = bol || (j > 4);
        i16 = !bol;
        i17 = j == 9;
        i18 = j != 10;
        res = i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8 + i9 + i10 + i11 + i12 + i13;
        i14 = i14 && i15 && i16 && i17 && i18;
        i1 = j + 1;
        i2 = j - 1;
        i3 = j * 3;
        i4 = j / 31;
        i5 = j % 71;
        i6 = j << 3;
        i7 = j >> 4;
        i8 = j >>> 5;
        i9 = bol ? 7 : 9;
        i10 = ~j;
        i11 = j & 3;
        i12 = j | 4;
        i13 = j ^ 4;
        i14 = bol && (j < 3);
        i15 = bol || (j > 4);
        i16 = !bol;
        i17 = j == 9;
        i18 = j != 10;
        res = i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8 + i9 + i10 + i11 + i12 + i13;
        i14 = i14 && i15 && i16 && i17 && i18;
        i1 = j + 1;
        i2 = j - 1;
        i3 = j * 3;
        i4 = j / 31;
        i5 = j % 71;
        i6 = j << 3;
        i7 = j >> 4;
        i8 = j >>> 5;
        i9 = bol ? 7 : 9;
        i10 = ~j;
        i11 = j & 3;
        i12 = j | 4;
        i13 = j ^ 4;
        i14 = bol && (j < 3);
        i15 = bol || (j > 4);
        i16 = !bol;
        i17 = j == 9;
        i18 = j != 10;
        res = i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8 + i9 + i10 + i11 + i12 + i13;
        i14 = i14 && i15 && i16 && i17 && i18;
        return res;
    }

    int fopt() {
        int res;
        i1 = j + 1;
        i2 = j - 1;
        i3 = j * 3;
        i4 = j / 31;
        i5 = j % 71;
        i6 = j << 3;
        i7 = j >> 4;
        i8 = j >>> 5;
        i9 = bol ? 7 : 9;
        i10 = ~j;
        i11 = j & 3;
        i12 = j | 4;
        i13 = j ^ 4;
        res = i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8 + i9 + i10 + i11 + i12 + i13;
        return res;
    }
}
