



public class Test8004051 {
    public static void main(String[] argv) {
        Object o = new Object();
        fillPrimRect(1.1f, 1.2f, 1.3f, 1.4f,
                     o, o,
                     1.5f, 1.6f, 1.7f, 1.8f,
                     2.0f, 2.1f, 2.2f, 2.3f,
                     2.4f, 2.5f, 2.6f, 2.7f,
                     100, 101);
        System.out.println("Test passed, test did not assert");
    }

    static boolean fillPrimRect(float x, float y, float w, float h,
                                Object rectTex, Object wrapTex,
                                float bx, float by, float bw, float bh,
                                float f1, float f2, float f3, float f4,
                                float f5, float f6, float f7, float f8,
                                int i1, int i2 ) {
        System.out.println(x + " " + y + " " + w + " " + h + " " +
                           bx + " " + by + " " + bw + " " + bh);
        return true;
    }
}
