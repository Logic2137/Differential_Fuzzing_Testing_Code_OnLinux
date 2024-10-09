

package jdk.test.lib;

import java.util.Random;
import java.util.SplittableRandom;


public class RandomFactory {
    
    private static Long getSystemSeed() {
        Long seed = null;
        try {
            
            
            
            seed = Long.valueOf(System.getProperty("seed"));
        } catch (NumberFormatException e) {
            
        }

        return seed;
    }

    
    private static long getRandomSeed() {
        return new Random().nextLong();
    }

    
    public static long getSeed() {
        Long seed = getSystemSeed();
        if (seed == null) {
            seed = getRandomSeed();
        }
        System.out.println("Seed from RandomFactory = "+seed+"L");
        return seed;
    }

    
    public static Random getRandom() {
        return new Random(getSeed());
    }

    
    public static SplittableRandom getSplittableRandom() {
        return new SplittableRandom(getSeed());
    }
}
