


package compiler.inlining;

import java.util.regex.Pattern;

public class LateInlineVirtualNullReceiverCheck {
    static final Pattern pattern = Pattern.compile("");

    public static void test(String s) {
        pattern.matcher(s);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10_000; ++i) {
            try {
                test(null);
            } catch (NullPointerException npe) {
                
            }
        }
        System.out.println("TEST PASSED");
    }
}
