import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import static java.util.ResourceBundle.Control;
import java.util.stream.*;

public class CodePointTest {

    static final Charset[] props = { StandardCharsets.ISO_8859_1, StandardCharsets.UTF_8, StandardCharsets.US_ASCII };

    static final String encoding = System.getProperty("java.util.PropertyResourceBundle.encoding", "");

    public static void main(String[] args) {
        for (Charset cs : props) {
            try {
                checkProps(cs, cs == StandardCharsets.UTF_8 && encoding.equals("ISO-8859-1"));
                if (cs == StandardCharsets.ISO_8859_1 && encoding.equals("UTF-8")) {
                    throw new RuntimeException("Reading ISO-8859-1 properties in " + "strict UTF-8 encoding should throw an exception");
                }
            } catch (IOException e) {
                if ((e instanceof MalformedInputException || e instanceof UnmappableCharacterException) && cs == StandardCharsets.ISO_8859_1 && encoding.equals("UTF-8")) {
                } else {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    static void checkProps(Charset cs, boolean shouldFail) throws IOException {
        int start = Character.MIN_CODE_POINT;
        int end = 0;
        switch(cs.name()) {
            case "ISO-8859-1":
                end = 0xff;
                break;
            case "UTF-8":
                end = Character.MAX_CODE_POINT;
                break;
            case "US-ASCII":
                end = 0x7f;
                break;
            default:
                assert false;
        }
        Properties p = new Properties();
        String outputName = cs.name() + ".properties";
        ResourceBundle.clearCache();
        IntStream.range(start, end + 1).forEach(c -> {
            if (Character.isDefined(c) && (Character.isSupplementaryCodePoint(c) || !Character.isSurrogate((char) c))) {
                p.setProperty("key" + Integer.toHexString(c), Character.isSupplementaryCodePoint(c) ? String.valueOf(Character.toChars(c)) : Character.toString((char) c));
            }
        });
        try (BufferedWriter bw = Files.newBufferedWriter(FileSystems.getDefault().getPath(System.getProperty("test.classes", "."), outputName), cs)) {
            p.store(bw, null);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Control c = Control.getControl(Control.FORMAT_PROPERTIES);
        ResourceBundle rb;
        try {
            rb = c.newBundle(cs.name(), Locale.ROOT, "java.properties", CodePointTest.class.getClassLoader(), false);
        } catch (IllegalAccessException | InstantiationException ex) {
            throw new RuntimeException(ex);
        }
        Properties result = new Properties();
        rb.keySet().stream().forEach((key) -> {
            result.setProperty(key, rb.getString(key));
        });
        if (!p.equals(result) && !shouldFail) {
            System.out.println("Charset: " + cs);
            rb.keySet().stream().sorted().forEach((key) -> {
                if (!p.getProperty(key).equals(result.getProperty(key))) {
                    System.out.println(key + ": file: " + p.getProperty(key) + ", RB: " + result.getProperty(key));
                }
            });
            throw new RuntimeException("not equal!");
        }
    }
}
