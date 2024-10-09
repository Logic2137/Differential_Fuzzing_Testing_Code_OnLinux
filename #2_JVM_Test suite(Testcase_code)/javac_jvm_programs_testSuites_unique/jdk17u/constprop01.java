
package vm.compiler.jbe.constprop.constprop01;

class Starter {

    int global;

    int gl_unopt;

    int gl_opt;

    int a_one;

    int zero;

    Starter() {
        global = 1;
        gl_unopt = 1;
        gl_opt = 1;
        a_one = 1;
        zero = global - a_one;
    }

    void increment_unopt() {
        gl_unopt += a_one;
    }

    void increment_opt() {
        gl_opt += a_one;
    }
}

public class constprop01 {

    static int err = 0;

    static final int is = 16386;

    double a, b, c, d, x, z;

    double a_opt, b_opt, c_opt, d_opt, x_opt, z_opt;

    static Starter st = new Starter();

    public static void main(String[] args) {
        constprop01 cpr = new constprop01();
        cpr.un_optimized();
        cpr.hand_optimized();
        if (st.gl_unopt != st.gl_opt) {
            System.out.println(">>Bad output: gl_unopt is not equal to gl_optimized. gl_unopt = " + st.gl_unopt + "; gl_optimized = " + st.gl_opt);
            err = 1;
        }
        if (cpr.a != cpr.a_opt) {
            System.out.println(">>Bad output: a_unoptimized=" + cpr.a + "; a_optimized=" + cpr.a_opt);
            err = 1;
        }
        if (cpr.b != cpr.b_opt) {
            System.out.println(">>Bad output: b_unoptimized=" + cpr.b + "; b_optimized=" + cpr.b_opt);
            err = 1;
        }
        if (cpr.c != cpr.c_opt) {
            System.out.println(">>Bad output: c_unoptimized=" + cpr.c + "; c_optimized=" + cpr.c_opt);
            err = 1;
        }
        if (cpr.d != cpr.d_opt) {
            System.out.println(">>Bad output: d_unoptimized=" + cpr.d + "; d_optimized=" + cpr.d_opt);
            err = 1;
        }
        if (cpr.x != cpr.x_opt) {
            System.out.println(">>Bad output: x_unoptimized=" + cpr.x + "; x_optimized=" + cpr.x_opt);
            err = 1;
        }
        if (cpr.z != cpr.z_opt) {
            System.out.println(">>Bad output: z_unoptimized=" + cpr.z + "; z_optimized=" + cpr.z_opt);
            err = 1;
        }
        if (err == 0) {
            System.out.println("Test constprop01 Passed.");
        } else {
            throw new Error("Test constprop01 Failed.");
        }
    }

    void un_optimized() {
        int k, m;
        a = st.a_one;
        c = st.a_one;
        d = st.a_one;
        x = st.a_one;
        for (k = 0; k < is; k++) {
            st.increment_unopt();
        }
        System.out.println("gl_unopt = " + st.gl_unopt + " after incrementing it 16386 times.");
        st.gl_unopt -= 16386;
        System.out.println("gl_unopt = " + st.gl_unopt + " after subtracting 16386.");
        m = 32769;
        b = c * m;
        System.out.println("c = " + c + "; m = 2; b = " + b);
        a = Math.E / (a * d * Math.pow(1 / Math.PI, 15));
        c = 2 * Math.E * d;
        d = 2 * Math.E * a;
        System.out.println("e = " + Math.E + "; d = " + d + "; a = " + a);
        System.out.println("x = " + x);
        x += m;
        System.out.println("x = " + x + "; m = " + m);
        z = Math.pow(1 / Math.PI, 15) + st.zero / Math.PI;
        System.out.println("z = " + z);
    }

    void hand_optimized() {
        int k, m;
        a_opt = st.a_one;
        c_opt = st.a_one;
        d_opt = st.a_one;
        x_opt = st.a_one;
        for (k = 0; k < 16386; k++) {
            st.increment_opt();
        }
        System.out.println("gl_opt = " + st.gl_opt + " after incrementing it 16386 times.");
        st.gl_opt -= 16386;
        System.out.println("gl_opt = " + st.gl_opt + " after subtracting 16386.");
        m = 32769;
        b_opt = c_opt * 32769;
        System.out.println("c_opt = " + c_opt + "; b_opt = " + b_opt);
        double e = 2.7182818284590452354;
        a_opt = 2.7182818284590452354 / (a_opt * d_opt * Math.pow(0.3183098861837907, 15));
        c_opt = 2 * 2.7182818284590452354 * d_opt;
        d_opt = 2 * 2.7182818284590452354 * a_opt;
        System.out.println("e = " + 2.7182818284590452354 + "; d_opt = " + d_opt + "; a_opt = " + a_opt);
        System.out.println("x_opt = " + x_opt);
        x_opt += 32769;
        System.out.println("x_opt = " + x_opt + "; m = " + m);
        z_opt = 3.4894092627910365E-8 + 0 / 3.14159265358979323846;
        System.out.println("z_opt = " + z_opt);
    }
}
