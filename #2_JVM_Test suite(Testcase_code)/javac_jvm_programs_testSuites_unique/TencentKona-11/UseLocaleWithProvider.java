


import java.util.Locale;
import java.util.Scanner;

public class UseLocaleWithProvider {

    public static void main(String[] args) {

        try {
            testScannerUseLocale("-123.4", Locale.US, -123.4);
            testScannerUseLocale("-123,45", new Locale("fi", "FI"), -123.45);
            testScannerUseLocale("334,65", Locale.FRENCH, 334.65);
            testScannerUseLocale("4.334,65", Locale.GERMAN, 4334.65);
        } catch (ClassCastException ex) {
            throw new RuntimeException("[FAILED: With" +
                    " java.locale.providers=SPI,COMPAT, Scanner.useLocale()" +
                    " shouldn't throw ClassCastException]");
        }
    }

    private static void testScannerUseLocale(String number, Locale locale,
                                             Number actual) {
        Scanner sc = new Scanner(number).useLocale(locale);
        if (!sc.hasNextFloat() || sc.nextFloat() != actual.floatValue()) {
            throw new RuntimeException("[FAILED: With" +
                    " java.locale.providers=SPI,COMPAT, Scanner" +
                    ".hasNextFloat() or Scanner.nextFloat() is unable to" +
                    " scan the given number: " + number + ", in the given" +
                    " locale:" + locale + "]");
        }
    }
}

