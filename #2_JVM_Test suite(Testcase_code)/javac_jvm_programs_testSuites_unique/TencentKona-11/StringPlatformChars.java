

 
import java.util.Arrays;

public class StringPlatformChars {

    private static final String JNU_ENCODING = System.getProperty("sun.jnu.encoding");

    public static void main(String... args) throws Exception {
        System.out.println("sun.jnu.encoding: " + JNU_ENCODING);
        System.loadLibrary("stringPlatformChars");

        
        StringBuilder unicodeSb = new StringBuilder();
        StringBuilder asciiSb = new StringBuilder();
        StringBuilder latinSb = new StringBuilder();

        for (int i = 0; i < 2000; i++) {
            unicodeSb.append('\uFEFE');
            testString(unicodeSb.toString());

            asciiSb.append('x');
            testString(asciiSb.toString());

            latinSb.append('\u00FE');
            testString(latinSb.toString());

            testString(latinSb.toString() + asciiSb.toString() + unicodeSb.toString());
        }

        
        for (char c = '\u0001'; c < Character.MAX_VALUE; c++) {
            testString(String.valueOf(c));
        }
        
        
        if (getBytes("\u0000abcdef").length != 0 ||
            getBytes("a\u0000bcdef").length != 1) {
            System.out.println("Mismatching values for strings including \\u0000");
            throw new AssertionError();
        }
    }

    private static void testString(String s) throws Exception {
        byte[] nativeBytes = getBytes(s);
        byte[] stringBytes = s.getBytes(JNU_ENCODING);

        if (!Arrays.equals(nativeBytes, stringBytes)) {
            System.out.println("Mismatching values for: '" + s + "' " + Arrays.toString(s.chars().toArray()));
            System.out.println("Native: " + Arrays.toString(nativeBytes));
            System.out.println("String: " + Arrays.toString(stringBytes));
            throw new AssertionError(s);
        }

        String javaNewS = new String(nativeBytes, JNU_ENCODING);
        String nativeNewS = newString(nativeBytes);
        if (!javaNewS.equals(nativeNewS)) {
            System.out.println("New string via native doesn't match via java: '" + javaNewS + "' and '" + nativeNewS + "'");
            throw new AssertionError(s);
        }
    }

    static native byte[] getBytes(String string);

    static native String newString(byte[] bytes);
}
