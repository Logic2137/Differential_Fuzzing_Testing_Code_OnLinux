



public class TestStringInternCleanup {

    static final int COUNT = 1_000_000;
    static final int WINDOW = 1_000;

    static final String[] reachable = new String[WINDOW];

    public static void main(String[] args) throws Exception {
        int rIdx = 0;
        for (int c = 0; c < COUNT; c++) {
            reachable[rIdx] = ("LargeInternedString" + c).intern();
            rIdx++;
            if (rIdx >= WINDOW) {
                rIdx = 0;
            }
        }
    }

}
