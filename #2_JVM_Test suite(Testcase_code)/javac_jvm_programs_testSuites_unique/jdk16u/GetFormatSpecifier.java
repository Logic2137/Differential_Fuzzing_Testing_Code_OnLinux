


import java.util.MissingFormatArgumentException;

public class GetFormatSpecifier {

    static void fail(String s) {
        throw new RuntimeException(s);
    }

    public static void main(String[] args) {

        
        
        
        final String formatSpecifier = "%1$5.3s";
        try {
            String formatResult = String.format(formatSpecifier);
            fail("MissingFormatArgumentException not thrown.");
        } catch (MissingFormatArgumentException ex) {
            final String returnedFormatSpecifier = ex.getFormatSpecifier();
            if (!returnedFormatSpecifier.equals(formatSpecifier)) {
                fail("The specified format specifier: " + formatSpecifier
                        + " does not match the value from getFormatSpecifier(): "
                        + returnedFormatSpecifier);
            }
        }
    }
}
