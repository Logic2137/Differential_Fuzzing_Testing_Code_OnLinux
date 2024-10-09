public class FdlibmTranslit {

    private FdlibmTranslit() {
        throw new UnsupportedOperationException("No FdLibmTranslit instances for you.");
    }

    private static int __LO(double x) {
        long transducer = Double.doubleToRawLongBits(x);
        return (int) transducer;
    }

    private static double __LO(double x, int low) {
        long transX = Double.doubleToRawLongBits(x);
        return Double.longBitsToDouble((transX & 0xFFFF_FFFF_0000_0000L) | (low & 0x0000_0000_FFFF_FFFFL));
    }

    private static int __HI(double x) {
        long transducer = Double.doubleToRawLongBits(x);
        return (int) (transducer >> 32);
    }

    private static double __HI(double x, int high) {
        long transX = Double.doubleToRawLongBits(x);
        return Double.longBitsToDouble((transX & 0x0000_0000_FFFF_FFFFL) | (((long) high)) << 32);
    }

    public static double hypot(double x, double y) {
        return Hypot.compute(x, y);
    }

    public static class Cbrt {

        private static final int B1 = 715094163;

        private static final int B2 = 696219795;

        private static final double C = 5.42857142857142815906e-01;

        private static final double D = -7.05306122448979611050e-01;

        private static final double E = 1.41428571428571436819e+00;

        private static final double F = 1.60714285714285720630e+00;

        private static final double G = 3.57142857142857150787e-01;

        public static strictfp double compute(double x) {
            int hx;
            double r, s, t = 0.0, w;
            int sign;
            hx = __HI(x);
            sign = hx & 0x80000000;
            hx ^= sign;
            if (hx >= 0x7ff00000)
                return (x + x);
            if ((hx | __LO(x)) == 0)
                return (x);
            x = __HI(x, hx);
            if (hx < 0x00100000) {
                t = __HI(t, 0x43500000);
                t *= x;
                t = __HI(t, __HI(t) / 3 + B2);
            } else {
                t = __HI(t, hx / 3 + B1);
            }
            r = t * t / x;
            s = C + r * t;
            t *= G + F / (s + E + D / s);
            t = __LO(t, 0);
            t = __HI(t, __HI(t) + 0x00000001);
            s = t * t;
            r = x / s;
            w = t + t;
            r = (r - t) / (w + r);
            t = t + t * r;
            t = __HI(t, __HI(t) | sign);
            return (t);
        }
    }

    static class Hypot {

        public static double compute(double x, double y) {
            double a = x;
            double b = y;
            double t1, t2, y1, y2, w;
            int j, k, ha, hb;
            ha = __HI(x) & 0x7fffffff;
            hb = __HI(y) & 0x7fffffff;
            if (hb > ha) {
                a = y;
                b = x;
                j = ha;
                ha = hb;
                hb = j;
            } else {
                a = x;
                b = y;
            }
            a = __HI(a, ha);
            b = __HI(b, hb);
            if ((ha - hb) > 0x3c00000) {
                return a + b;
            }
            k = 0;
            if (ha > 0x5f300000) {
                if (ha >= 0x7ff00000) {
                    w = a + b;
                    if (((ha & 0xfffff) | __LO(a)) == 0)
                        w = a;
                    if (((hb ^ 0x7ff00000) | __LO(b)) == 0)
                        w = b;
                    return w;
                }
                ha -= 0x25800000;
                hb -= 0x25800000;
                k += 600;
                a = __HI(a, ha);
                b = __HI(b, hb);
            }
            if (hb < 0x20b00000) {
                if (hb <= 0x000fffff) {
                    if ((hb | (__LO(b))) == 0)
                        return a;
                    t1 = 0;
                    t1 = __HI(t1, 0x7fd00000);
                    b *= t1;
                    a *= t1;
                    k -= 1022;
                } else {
                    ha += 0x25800000;
                    hb += 0x25800000;
                    k -= 600;
                    a = __HI(a, ha);
                    b = __HI(b, hb);
                }
            }
            w = a - b;
            if (w > b) {
                t1 = 0;
                t1 = __HI(t1, ha);
                t2 = a - t1;
                w = Math.sqrt(t1 * t1 - (b * (-b) - t2 * (a + t1)));
            } else {
                a = a + a;
                y1 = 0;
                y1 = __HI(y1, hb);
                y2 = b - y1;
                t1 = 0;
                t1 = __HI(t1, ha + 0x00100000);
                t2 = a - t1;
                w = Math.sqrt(t1 * y1 - (w * (-w) - (t1 * y2 + t2 * b)));
            }
            if (k != 0) {
                t1 = 1.0;
                int t1_hi = __HI(t1);
                t1_hi += (k << 20);
                t1 = __HI(t1, t1_hi);
                return t1 * w;
            } else
                return w;
        }
    }

    static class Exp {

        private static final double one = 1.0;

        private static final double[] halF = { 0.5, -0.5 };

        private static final double huge = 1.0e+300;

        private static final double twom1000 = 9.33263618503218878990e-302;

        private static final double o_threshold = 7.09782712893383973096e+02;

        private static final double u_threshold = -7.45133219101941108420e+02;

        private static final double[] ln2HI = { 6.93147180369123816490e-01, -6.93147180369123816490e-01 };

        private static final double[] ln2LO = { 1.90821492927058770002e-10, -1.90821492927058770002e-10 };

        private static final double invln2 = 1.44269504088896338700e+00;

        private static final double P1 = 1.66666666666666019037e-01;

        private static final double P2 = -2.77777777770155933842e-03;

        private static final double P3 = 6.61375632143793436117e-05;

        private static final double P4 = -1.65339022054652515390e-06;

        private static final double P5 = 4.13813679705723846039e-08;

        public static strictfp double compute(double x) {
            double y, hi = 0, lo = 0, c, t;
            int k = 0, xsb;
            int hx;
            hx = __HI(x);
            xsb = (hx >> 31) & 1;
            hx &= 0x7fffffff;
            if (hx >= 0x40862E42) {
                if (hx >= 0x7ff00000) {
                    if (((hx & 0xfffff) | __LO(x)) != 0)
                        return x + x;
                    else
                        return (xsb == 0) ? x : 0.0;
                }
                if (x > o_threshold)
                    return huge * huge;
                if (x < u_threshold)
                    return twom1000 * twom1000;
            }
            if (hx > 0x3fd62e42) {
                if (hx < 0x3FF0A2B2) {
                    hi = x - ln2HI[xsb];
                    lo = ln2LO[xsb];
                    k = 1 - xsb - xsb;
                } else {
                    k = (int) (invln2 * x + halF[xsb]);
                    t = k;
                    hi = x - t * ln2HI[0];
                    lo = t * ln2LO[0];
                }
                x = hi - lo;
            } else if (hx < 0x3e300000) {
                if (huge + x > one)
                    return one + x;
            } else
                k = 0;
            t = x * x;
            c = x - t * (P1 + t * (P2 + t * (P3 + t * (P4 + t * P5))));
            if (k == 0)
                return one - ((x * c) / (c - 2.0) - x);
            else
                y = one - ((lo - (x * c) / (2.0 - c)) - hi);
            if (k >= -1021) {
                y = __HI(y, __HI(y) + (k << 20));
                return y;
            } else {
                y = __HI(y, __HI(y) + ((k + 1000) << 20));
                return y * twom1000;
            }
        }
    }
}
