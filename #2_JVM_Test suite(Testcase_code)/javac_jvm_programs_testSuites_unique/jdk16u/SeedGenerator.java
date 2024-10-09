



package sun.security.provider;

public class SeedGenerator {

    static int count = 100;
    static int lastCount = 100;

    public static void generateSeed(byte[] result) {
        count--;
    }

    
    public static void checkUsage(int less) throws Exception {
        if (lastCount != count + less) {
            throw new Exception(String.format(
                    "lastCount = %d, count = %d, less = %d",
                    lastCount, count, less));
        }
        lastCount = count;
    }

    
    static byte[] getSystemEntropy() {
        return new byte[20];
    }
}
