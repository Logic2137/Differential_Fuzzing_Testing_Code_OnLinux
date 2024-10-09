public class Test6796786 {

    static volatile float d1;

    static volatile float d2;

    public static void main(String[] args) {
        int total = 0;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 100000; i++) {
            if (Float.floatToRawIntBits(-(d1 - d2)) == Float.floatToRawIntBits(-0.0f)) {
                total++;
            }
        }
        if (total != 100000) {
            throw new InternalError();
        }
    }
}
