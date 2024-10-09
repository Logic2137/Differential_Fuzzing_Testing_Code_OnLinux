



public class StripMinedLoopReorgOffsets {

    public static final int N = 400;

    public static long instanceCount=-4622920139809038929L;

    public void mainTest(String[] strArr1) {

        int i1=-211, i20=54720, i21=205, i22=2184, i23=58, i24=-50110, iArr3[]=new int[N];

        for (i20 = 16; 331 > i20; ++i20) {
            i1 = i20;
            i21 += i1;
            iArr3[i20] <<= (int)StripMinedLoopReorgOffsets.instanceCount;
            for (i22 = 4; i22 < 80; i22++) {
                i21 = i23;
                i24 = 1;
                while (++i24 < 2) {
                    try {
                        iArr3[i22] = (i23 / i1);
                    } catch (ArithmeticException a_e) {}
                }
            }
        }

        System.out.println("i1 i20 = " + i1 + "," + i20);
        System.out.println("i21 i22 i23 = " + i21 + "," + i22 + "," + i23);
        System.out.println("i24 = " + i24);
    }
    public static void main(String[] strArr) {
        StripMinedLoopReorgOffsets _instance = new StripMinedLoopReorgOffsets();
        for (int i = 0; i < 10; i++ ) {
            _instance.mainTest(strArr);
        }
    }
}
