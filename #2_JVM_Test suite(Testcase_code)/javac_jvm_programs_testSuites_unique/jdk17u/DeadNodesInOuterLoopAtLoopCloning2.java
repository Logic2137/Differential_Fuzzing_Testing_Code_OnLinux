public class DeadNodesInOuterLoopAtLoopCloning2 {

    public static void vMeth(boolean b, int i, float f) {
        int i1 = -4, i2 = 14, i16 = 10;
        byte by = 116;
        for (i1 = 323; i1 > 3; i1 -= 2) {
            if (i2 != 0) {
                return;
            }
            for (i16 = 1; i16 < 10; i16++) {
                i2 = by;
                i += (i16 - i2);
                i2 -= i16;
                if (b) {
                    i = by;
                }
                i2 *= 20;
            }
        }
    }

    public static void main(String[] strArr) {
        DeadNodesInOuterLoopAtLoopCloning2 _instance = new DeadNodesInOuterLoopAtLoopCloning2();
        for (int i = 0; i < 10; i++) {
            _instance.vMeth(true, 168, -125.661F);
        }
    }
}
