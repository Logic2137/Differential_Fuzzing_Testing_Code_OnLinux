import java.util.*;

public class Test7177917 {

    static double d;

    static Random r = new Random(0);

    static long m_pow(double[][] values) {
        double res = 0;
        long start = System.nanoTime();
        for (int i = 0; i < values.length; i++) {
            res += Math.pow(values[i][0], values[i][1]);
        }
        long stop = System.nanoTime();
        d = res;
        return (stop - start) / 1000;
    }

    static long m_exp(double[] values) {
        double res = 0;
        long start = System.nanoTime();
        for (int i = 0; i < values.length; i++) {
            res += Math.exp(values[i]);
        }
        long stop = System.nanoTime();
        d = res;
        return (stop - start) / 1000;
    }

    static double[][] pow_values(int nb) {
        double[][] res = new double[nb][2];
        for (int i = 0; i < nb; i++) {
            double ylogx = (1 + (r.nextDouble() * 2045)) - 1023;
            double x = Math.abs(Double.longBitsToDouble(r.nextLong()));
            while (x != x) {
                x = Math.abs(Double.longBitsToDouble(r.nextLong()));
            }
            double logx = Math.log(x) / Math.log(2);
            double y = ylogx / logx;
            res[i][0] = x;
            res[i][1] = y;
        }
        return res;
    }

    static double[] exp_values(int nb) {
        double[] res = new double[nb];
        for (int i = 0; i < nb; i++) {
            double ylogx = (1 + (r.nextDouble() * 2045)) - 1023;
            double x = Math.E;
            double logx = Math.log(x) / Math.log(2);
            double y = ylogx / logx;
            res[i] = y;
        }
        return res;
    }

    static public void main(String[] args) {
        {
            double[][] warmup_values = pow_values(10);
            m_pow(warmup_values);
            for (int i = 0; i < 20000; i++) {
                m_pow(warmup_values);
            }
            double[][] values = pow_values(1000000);
            System.out.println("==> POW " + m_pow(values));
            double[][] nan_values = new double[1][2];
            nan_values[0][0] = Double.NaN;
            nan_values[0][1] = Double.NaN;
            m_pow(nan_values);
            for (int i = 0; i < 20000; i++) {
                m_pow(warmup_values);
            }
            System.out.println("==> POW " + m_pow(values));
        }
        {
            double[] warmup_values = exp_values(10);
            m_exp(warmup_values);
            for (int i = 0; i < 20000; i++) {
                m_exp(warmup_values);
            }
            double[] values = exp_values(1000000);
            System.out.println("==> EXP " + m_exp(values));
            double[] nan_values = new double[1];
            nan_values[0] = Double.NaN;
            m_exp(nan_values);
            for (int i = 0; i < 20000; i++) {
                m_exp(warmup_values);
            }
            System.out.println("==> EXP " + m_exp(values));
        }
    }
}
