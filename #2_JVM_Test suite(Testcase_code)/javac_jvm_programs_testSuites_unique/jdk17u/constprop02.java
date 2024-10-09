
package vm.compiler.jbe.constprop.constprop02;

public class constprop02 {

    public static void main(String[] args) {
        boolean bPass = true;
        constprop02 cpr = new constprop02();
        bPass &= cpr.verify(cpr.testDiv_un_opt());
        bPass &= cpr.verify(cpr.testDiv_hand_opt());
        System.out.println("------------------------");
        if (!bPass) {
            throw new Error("Test constprop02 Failed.");
        }
        System.out.println("Test constprop02 Passed.");
    }

    String testDiv_un_opt() {
        int ia, ib;
        long lc, ld;
        double de, df;
        System.out.println("testDiv_un_opt:");
        ia = ib = 513;
        if (1 != ia / ib)
            return "case 1 failed";
        ia = -ia;
        if (-1 != ia / ib)
            return "case 2 failed";
        ia = ib = 1073741824;
        if (1 != ia / ib)
            return "case 3 failed";
        ia = -ia;
        if (-1 != ia / ib)
            return "case 4 failed";
        lc = ld = 8L;
        if (1 != lc / ld)
            return "case 5 failed";
        lc = -lc;
        if (-1 != lc / ld)
            return "case 6 failed";
        lc = ld = 1073741824L;
        if (1 != lc / ld)
            return "case 7 failed";
        lc = -lc;
        if (-1 != lc / ld)
            return "case 8 failed";
        ib = 0;
        try {
            ia = ia / ib;
            return "case 9 failed";
        } catch (java.lang.Exception x) {
        }
        ld = 0;
        try {
            lc = lc / ld;
            return "case 10 failed";
        } catch (java.lang.Exception x) {
        }
        try {
            lc = lc % ld;
            return "case 11 failed";
        } catch (java.lang.Exception x) {
        }
        de = df = 16385.0;
        if (1.0 != de / df)
            return "case 12 failed";
        de = -de;
        if (-1.0 != de / df)
            return "case 13 failed";
        df = 0.0;
        try {
            de = de / df;
        } catch (java.lang.Exception x) {
            return "case 14 failed";
        }
        try {
            de = de % df;
            de = 5.66666666666 % df;
        } catch (java.lang.Exception x) {
            return "case 15 failed";
        }
        return null;
    }

    String testDiv_hand_opt() {
        int ia, ib;
        long lc, ld;
        double de, df;
        System.out.println("testDiv_hand_opt:");
        if (1 != 513 / 513)
            return "case 1 failed";
        if (-1 != -513 / 513)
            return "case 2 failed";
        if (1 != 1073741824 / 1073741824)
            return "case 3 failed";
        if (-1 != 1073741824 / -1073741824)
            return "case 4 failed";
        if (1 != 8L / 8L)
            return "case 5 failed";
        if (-1 != -8L / 8L)
            return "case 6 failed";
        if (1 != 1073741824L / 1073741824L)
            return "case 7 failed";
        if (-1 != 1073741824L / -1073741824L)
            return "case 8 failed";
        ib = 0;
        try {
            ia = -1073741824 / ib;
            return "case 9 failed";
        } catch (java.lang.Exception x) {
        }
        ld = 0L;
        try {
            lc = -1073741824L / ld;
            return "case 10 failed";
        } catch (java.lang.Exception x) {
        }
        try {
            lc = -1073741824L % ld;
            return "case 11 failed";
        } catch (java.lang.Exception x) {
        }
        if (1.0 != 16385.0 / 16385.0)
            return "case 12 failed";
        if (-1.0 != -16385.0 / 16385.0)
            return "case 13 failed";
        df = 0.0;
        try {
            de = -1073741824L / df;
        } catch (java.lang.Exception x) {
            return "case 14 failed";
        }
        try {
            de = -1073741824L % 0.0;
            de = 5.66666666666 % df;
        } catch (java.lang.Exception x) {
            return "cnase 15 failed";
        }
        return null;
    }

    boolean verify(String str) {
        boolean st = true;
        if (null == str || str.equals(""))
            System.out.println("OK");
        else {
            st = false;
            System.out.println("** " + str + " **");
        }
        return st;
    }
}
