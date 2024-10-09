import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Random;

public class XMLSaveLoadBasher {

    private static final int MAX_KEY_SIZE = 120;

    private static final int MIN_KEY_SIZE = 1;

    private static final int MAX_VALUE_SIZE = 120;

    private static final int MIN_VALUE_SIZE = 0;

    public static void main(String[] args) throws Exception {
        testSaveLoad("UTF-8", "test save");
        testSaveLoad("UTF-8", null);
        testSaveLoad("ISO-8859-1", "test save");
        testSaveLoad("KOI8-R", "test save");
    }

    private static void testSaveLoad(String encoding, String comment) throws Exception {
        Properties originalProps = new Properties();
        Properties loadedProps = new Properties();
        Random generator = new Random();
        for (int x = 0; x < 10; x++) {
            String aKey;
            String aValue;
            int keyLen = generator.nextInt(MAX_KEY_SIZE - MIN_KEY_SIZE + 1) + MIN_KEY_SIZE;
            int valLen = generator.nextInt(MAX_VALUE_SIZE - MIN_VALUE_SIZE + 1) + MIN_VALUE_SIZE;
            StringBuffer aKeyBuffer = new StringBuffer(keyLen);
            StringBuffer aValueBuffer = new StringBuffer(valLen);
            for (int y = 0; y < keyLen; y++) {
                char test = (char) (generator.nextInt(6527) + 32);
                aKeyBuffer.append(test);
            }
            aKey = aKeyBuffer.toString();
            for (int y = 0; y < valLen; y++) {
                char test = (char) (generator.nextInt(6527) + 32);
                aValueBuffer.append(test);
            }
            aValue = aValueBuffer.toString();
            try {
                originalProps.setProperty(aKey, aValue);
            } catch (IllegalArgumentException e) {
                System.err.println("disallowing...");
            }
        }
        File oldTestFile = new File("props3");
        oldTestFile.delete();
        System.err.println("Saving...");
        try (OutputStream out = new FileOutputStream("props3")) {
            originalProps.storeToXML(out, comment, encoding);
        }
        System.err.println("Loading...");
        try (InputStream in = new FileInputStream("props3")) {
            loadedProps.loadFromXML(in);
        }
        if (!originalProps.equals(loadedProps))
            throw new RuntimeException("Properties load and save failed");
    }
}
