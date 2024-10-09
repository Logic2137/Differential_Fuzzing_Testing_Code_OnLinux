import javax.imageio.ImageReadParam;

public class ImageReadParamPasses {

    private static final int maxint = Integer.MAX_VALUE;

    private static void expect(int i, int j) {
        if (i != j) {
            throw new RuntimeException("Expected " + i + ", got " + j);
        }
    }

    private static void checkForIAE(int minPass, int numPasses) {
        ImageReadParam param = new ImageReadParam();
        boolean gotIAE = false;
        try {
            param.setSourceProgressivePasses(minPass, numPasses);
        } catch (IllegalArgumentException iae) {
            gotIAE = true;
        }
        if (!gotIAE) {
            throw new RuntimeException("Failed to get IAE for wraparound!");
        }
    }

    private static void test(int minPass, int numPasses) {
        ImageReadParam param = new ImageReadParam();
        param.setSourceProgressivePasses(minPass, numPasses);
        expect(param.getSourceMinProgressivePass(), minPass);
        expect(param.getSourceNumProgressivePasses(), numPasses);
        int maxPass = numPasses == maxint ? maxint : minPass + numPasses - 1;
        expect(param.getSourceMaxProgressivePass(), maxPass);
    }

    public static void main(String[] args) {
        test(17, 30);
        test(0, maxint);
        test(17, maxint);
        test(maxint - 10, maxint);
        test(maxint - 10, 10);
        test(maxint - 10, 11);
        test(maxint, maxint);
        test(maxint, 1);
        checkForIAE(maxint, 2);
        checkForIAE(maxint - 5, 10);
        checkForIAE(10, maxint - 5);
        checkForIAE(maxint - 1000, maxint - 1000);
        checkForIAE(maxint - 1, maxint - 1);
    }
}
