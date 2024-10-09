import java.nio.charset.*;

public class IllegalCharsetName {

    public static void main(String[] args) throws Exception {
        String[] illegalNames = { ".", "_", ":", "-", ".name", "_name", ":name", "-name", "name*name", "name?name" };
        for (int i = 0; i < illegalNames.length; i++) {
            try {
                Charset.forName(illegalNames[i]);
                throw new Exception("Charset.forName(): No exception thrown");
            } catch (IllegalCharsetNameException x) {
            }
            try {
                Charset.isSupported(illegalNames[i]);
                throw new Exception("Charset.isSupported(): No exception thrown");
            } catch (IllegalCharsetNameException x) {
            }
        }
        checkAliases(StandardCharsets.ISO_8859_1);
        checkAliases(StandardCharsets.US_ASCII);
        checkAliases(StandardCharsets.UTF_8);
    }

    private static void checkAliases(Charset cs) {
        for (String alias : cs.aliases()) {
            Charset.forName(alias);
            Charset.isSupported(alias);
        }
    }
}
