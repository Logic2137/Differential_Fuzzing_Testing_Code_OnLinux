



public class ConstFPVectorization {

    static float[] f = new float[16];
    static double[] d = new double[16];

    static void floatLoop(int count) {
        for (int i = 0; i < count; i++) {
            f[i] = -0.0f;
        }
    }

    static void doubleLoop(int count) {
        for (int i = 0; i < count; i++) {
            d[i] = -0.0d;
        }
    }

    public static void main(String args[]) {
        for (int i = 0; i < 10_000; i++) {
            floatLoop(Integer.parseInt(args[0]));
            doubleLoop(Integer.parseInt(args[0]));
        }
        for (int i = 0; i < Integer.parseInt(args[0]); i++) {
            if (Float.floatToRawIntBits(f[i]) != Float.floatToRawIntBits(-0.0f))
                throw new Error("Float error at index " + i);
            if (Double.doubleToRawLongBits(d[i]) != Double.doubleToRawLongBits(-0.0d))
                throw new Error("Double error at index " + i);
        }
    }
}
