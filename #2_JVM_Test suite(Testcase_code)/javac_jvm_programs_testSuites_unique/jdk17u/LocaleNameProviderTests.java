import java.util.Locale;

public class LocaleNameProviderTests {

    private static final String expected = "foo (foo_ca:foo_japanese)";

    public static void main(String... args) {
        String name = Locale.forLanguageTag("foo-u-ca-japanese").getDisplayName(new Locale("foo"));
        if (!name.equals(expected)) {
            throw new RuntimeException("Unicode extension key and/or type name(s) is incorrect. " + "Expected: \"" + expected + "\", got: \"" + name + "\"");
        }
    }
}
