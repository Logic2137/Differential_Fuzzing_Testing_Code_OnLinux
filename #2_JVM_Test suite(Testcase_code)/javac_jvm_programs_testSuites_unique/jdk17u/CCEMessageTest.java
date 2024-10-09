
package compiler.c1;

public class CCEMessageTest {

    public static void main(String... args) {
        String[] s = null;
        try {
            s = (String[]) new Object[1];
        } catch (ClassCastException cce) {
            if (!cce.getMessage().contains("[Ljava.lang.String;"))
                throw new AssertionError("Incorrect CCE message", cce);
        }
        if (s != null)
            throw new AssertionError("Unexpected error");
    }
}
