

package jdk.incubator.http;

import java.util.Random;


public class AbstractRandomTest {

    private static Long getSystemSeed() {
        Long seed = null;
        try {
            
            
            seed = Long.valueOf(System.getProperty("seed"));
        } catch (NumberFormatException e) {
            
        }
        return seed;
    }

    private static long getSeed() {
        Long seed = getSystemSeed();
        if (seed == null) {
            seed = (new Random()).nextLong();
        }
        System.out.println("Seed from AbstractRandomTest.getSeed = "+seed+"L");
        return seed;
    }

    private static Random random = new Random(getSeed());

    protected static int randomRange(int lower, int upper) {
        if (lower > upper)
            throw new IllegalArgumentException("lower > upper");
        int diff = upper - lower;
        int r = lower + random.nextInt(diff);
        return r - (r % 8); 
    }
}
