import java.net.URL;

public class CanonicalizationTest {

    public static void main(String[] args) throws Exception {
        URL base = new URL("jar:file:/foo!/");
        check(new URL(base, ""), "jar:file:/foo!/");
        check(new URL(base, "."), "jar:file:/foo!/");
        check(new URL(base, ".."), "jar:file:/foo!");
        check(new URL(base, ".x"), "jar:file:/foo!/.x");
        check(new URL(base, "..x"), "jar:file:/foo!/..x");
        check(new URL(base, "..."), "jar:file:/foo!/...");
        check(new URL(base, "foo/."), "jar:file:/foo!/foo/");
        check(new URL(base, "foo/.."), "jar:file:/foo!/");
        check(new URL(base, "foo/.x"), "jar:file:/foo!/foo/.x");
        check(new URL(base, "foo/..x"), "jar:file:/foo!/foo/..x");
        check(new URL(base, "foo/..."), "jar:file:/foo!/foo/...");
        check(new URL(base, "foo/./"), "jar:file:/foo!/foo/");
        check(new URL(base, "foo/../"), "jar:file:/foo!/");
        check(new URL(base, "foo/.../"), "jar:file:/foo!/foo/.../");
        check(new URL(base, "foo/../../"), "jar:file:/foo!/");
        check(new URL(base, "foo/../,,/.."), "jar:file:/foo!/");
        check(new URL(base, "foo/../."), "jar:file:/foo!/");
        check(new URL(base, "foo/../.x"), "jar:file:/foo!/.x");
    }

    private static void check(URL url, String expected) {
        if (!url.toString().equals(expected)) {
            throw new AssertionError("Expected " + url + " to equal " + expected);
        }
    }
}
