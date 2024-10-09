import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public final class GetPropertyNames {

    static BufferedImage defaultProps = new BufferedImage(1, 1, TYPE_INT_ARGB);

    public static void main(final String[] args) {
        if (defaultProps.getPropertyNames() != null) {
            throw new RuntimeException("PropertyNames should be null");
        }
        final BufferedImage emptyProps = getBufferedImage(null);
        if (emptyProps.getPropertyNames() != null) {
            throw new RuntimeException("PropertyNames should be null");
        }
        final BufferedImage nullProps = getBufferedImage(new Properties());
        if (nullProps.getPropertyNames() != null) {
            throw new RuntimeException("PropertyNames should be null");
        }
        final Properties properties = new Properties();
        properties.put(1, 1);
        properties.put(2, 2);
        properties.put(3, 3);
        final BufferedImage nonStringProps = getBufferedImage(properties);
        if (nonStringProps.getPropertyNames() != null) {
            throw new RuntimeException("PropertyNames should be null");
        }
        properties.clear();
        properties.setProperty("1", "1");
        properties.setProperty("2", "2");
        validate(getBufferedImage(properties), 2);
        properties.clear();
        properties.put(1, 1);
        properties.put(2, 2);
        properties.put(3, 3);
        properties.setProperty("key1", "value1");
        properties.setProperty("key2", "value2");
        final BufferedImage mixProps = getBufferedImage(properties);
        validate(mixProps, 2);
        if (!"value1".equals(mixProps.getProperty("key1")) || !"value2".equals(mixProps.getProperty("key2"))) {
            throw new RuntimeException("Wrong key-value pair");
        }
    }

    private static BufferedImage getBufferedImage(final Properties properties) {
        return new BufferedImage(defaultProps.getColorModel(), defaultProps.getRaster(), defaultProps.isAlphaPremultiplied(), properties);
    }

    private static void validate(final BufferedImage bi, final int expected) {
        final String[] names = bi.getPropertyNames();
        if (names.length != expected) {
            throw new RuntimeException("Wrong number of names");
        }
        for (final String name : names) {
            final Object property = bi.getProperty(name);
            if (property == Image.UndefinedProperty || property == null) {
                throw new RuntimeException("Unexpected property");
            }
        }
    }
}
