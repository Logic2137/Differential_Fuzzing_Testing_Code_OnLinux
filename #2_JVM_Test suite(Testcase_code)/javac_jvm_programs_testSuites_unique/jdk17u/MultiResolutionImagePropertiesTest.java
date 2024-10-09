import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class MultiResolutionImagePropertiesTest {

    private final static Map<String, String> PROPS;

    static {
        PROPS = new HashMap<>();
        PROPS.put("one", "ONE");
        PROPS.put("two", "TWO");
        PROPS.put("three", "THREE");
        PROPS.put("other", "OTHER");
        PROPS.put("test", "TEST");
    }

    private final static int SZ = 100;

    private final static Object UNDEF = Image.UndefinedProperty;

    private static BufferedImage generateImage(int scale, Properties p) {
        int x = (int) (SZ * scale);
        BufferedImage tmp = new BufferedImage(x, x, BufferedImage.TYPE_INT_RGB);
        return new BufferedImage(tmp.getColorModel(), tmp.getRaster(), tmp.isAlphaPremultiplied(), p);
    }

    private static void checkProperties(BufferedImage img, String[] keys, String[] undefined) {
        boolean numOK = true;
        if (keys.length == 0) {
            numOK = (img.getPropertyNames() == null);
        } else {
            numOK = (img.getPropertyNames().length == keys.length);
        }
        if (!numOK) {
            throw new RuntimeException("invalid number of properties");
        }
        for (String k : keys) {
            if (!img.getProperty(k).equals(PROPS.get(k))) {
                throw new RuntimeException("invalid property for name " + k);
            }
        }
        for (String k : undefined) {
            if (!img.getProperty(k).equals(UNDEF)) {
                throw new RuntimeException("property for name " + k + " must be undefined");
            }
        }
    }

    private static void checkProperties(BaseMultiResolutionImage img, String[] keys, String[] undefined) {
        for (String k : keys) {
            if (!img.getProperty(k, null).equals(PROPS.get(k))) {
                throw new RuntimeException("invalid property for name " + k);
            }
        }
        for (String k : undefined) {
            if (!img.getProperty(k, null).equals(UNDEF)) {
                throw new RuntimeException("property for name " + k + " must be undefined");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String[] keys = new String[] { "one", "two", "three" };
        String[] otherKeys = new String[] { "other", "test" };
        String[] empty = new String[] {};
        Properties props = new Properties();
        for (String k : keys) {
            props.setProperty(k, PROPS.get(k));
        }
        Properties otherProps = new Properties();
        for (String k : otherKeys) {
            otherProps.setProperty(k, PROPS.get(k));
        }
        Properties defaultProps = new Properties();
        BaseMultiResolutionImage image = new BaseMultiResolutionImage(new BufferedImage[] { generateImage(1, defaultProps), generateImage(2, defaultProps), generateImage(3, defaultProps) });
        for (Image var : image.getResolutionVariants()) {
            if (((BufferedImage) var).getPropertyNames() != null) {
                throw new RuntimeException("PropertyNames should be null");
            }
        }
        image = new BaseMultiResolutionImage(new BufferedImage[] { generateImage(1, props), generateImage(2, otherProps), generateImage(3, defaultProps) });
        checkProperties(image, keys, otherKeys);
        BufferedImage var = (BufferedImage) image.getResolutionVariant(SZ, SZ);
        checkProperties(var, keys, otherKeys);
        var = (BufferedImage) image.getResolutionVariant(2 * SZ, 2 * SZ);
        checkProperties(var, otherKeys, keys);
        var = (BufferedImage) image.getResolutionVariant(3 * SZ, 3 * SZ);
        checkProperties(var, empty, keys);
        checkProperties(var, empty, otherKeys);
        image = new BaseMultiResolutionImage(1, new BufferedImage[] { generateImage(1, props), generateImage(2, otherProps), generateImage(3, defaultProps) });
        checkProperties(image, otherKeys, keys);
        var = (BufferedImage) image.getResolutionVariant(SZ, SZ);
        checkProperties(var, keys, otherKeys);
        var = (BufferedImage) image.getResolutionVariant(2 * SZ, 2 * SZ);
        checkProperties(var, otherKeys, keys);
        var = (BufferedImage) image.getResolutionVariant(3 * SZ, 3 * SZ);
        checkProperties(var, empty, keys);
        checkProperties(var, empty, otherKeys);
        image = new BaseMultiResolutionImage(2, new BufferedImage[] { generateImage(1, defaultProps), generateImage(2, defaultProps), generateImage(3, props) });
        checkProperties(image, keys, otherKeys);
        var = (BufferedImage) image.getResolutionVariant(SZ, SZ);
        checkProperties(var, empty, keys);
        checkProperties(var, empty, otherKeys);
        var = (BufferedImage) image.getResolutionVariant(2 * SZ, 2 * SZ);
        checkProperties(var, empty, keys);
        checkProperties(var, empty, otherKeys);
        var = (BufferedImage) image.getResolutionVariant(3 * SZ, 3 * SZ);
        checkProperties(var, keys, otherKeys);
        checkProperties(new BaseMultiResolutionImage(new BufferedImage[] { generateImage(1, defaultProps), generateImage(2, props), generateImage(3, props) }), empty, keys);
        checkProperties(new BaseMultiResolutionImage(2, new BufferedImage[] { generateImage(1, props), generateImage(2, props), generateImage(3, defaultProps) }), empty, keys);
    }
}
