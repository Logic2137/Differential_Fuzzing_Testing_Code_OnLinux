import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Random;

public class SaveLoadBasher {

    private static String keyValueSeparators = "=: \t\r\n\f#!\\";

    public static void main(String[] args) throws Exception {
        Properties originalProps = new Properties();
        Properties loadedProps = new Properties();
        Random generator = new Random();
        int achar = 0;
        StringBuffer aKeyBuffer = new StringBuffer(120);
        StringBuffer aValueBuffer = new StringBuffer(120);
        String aKey;
        String aValue;
        for (int x = 0; x < 300; x++) {
            for (int y = 0; y < 100; y++) {
                achar = generator.nextInt();
                char test;
                if (achar < 99999) {
                    test = (char) (achar);
                } else {
                    int zz = achar % 10;
                    test = keyValueSeparators.charAt(zz);
                }
                aKeyBuffer.append(test);
            }
            aKey = aKeyBuffer.toString();
            for (int y = 0; y < 100; y++) {
                achar = generator.nextInt();
                char test = (char) (achar);
                aValueBuffer.append(test);
            }
            aValue = aValueBuffer.toString();
            try {
                originalProps.put(aKey, aValue);
            } catch (IllegalArgumentException e) {
                System.err.println("disallowing...");
            }
            aKeyBuffer.setLength(0);
            aValueBuffer.setLength(0);
        }
        File oldTestFile = new File("props3");
        oldTestFile.delete();
        System.out.println("Saving...");
        OutputStream out = new FileOutputStream("props3");
        originalProps.store(out, "test properties");
        out.close();
        System.out.println("Loading...");
        InputStream in = new FileInputStream("props3");
        try {
            loadedProps.load(in);
        } finally {
            in.close();
        }
        if (!originalProps.equals(loadedProps))
            throw new RuntimeException("Properties load and save failed");
    }
}
