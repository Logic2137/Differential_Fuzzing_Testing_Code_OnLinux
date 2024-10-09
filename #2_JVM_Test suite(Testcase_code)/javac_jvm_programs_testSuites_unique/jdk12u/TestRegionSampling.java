



public class TestRegionSampling {

    static final long TARGET_MB = Long.getLong("target", 2_000); 

    static volatile Object sink;

    public static void main(String[] args) throws Exception {
        long count = TARGET_MB * 1024 * 1024 / 16;
        for (long c = 0; c < count; c++) {
            sink = new Object();
        }
    }

}
