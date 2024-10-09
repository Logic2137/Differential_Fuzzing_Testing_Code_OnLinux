import java.lang.reflect.Method;
import java.util.Locale;

public class GetLCIDFromLocale {

    static Method getLCIDMethod = null;

    public static void main(String[] args) {
        try {
            Class ttClass = Class.forName("sun.font.TrueTypeFont");
            getLCIDMethod = ttClass.getDeclaredMethod("getLCIDFromLocale", java.util.Locale.class);
            getLCIDMethod.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Reflection failed");
        }
        if (getLCIDMethod == null) {
            throw new RuntimeException("No method found");
        }
        test(Locale.US, 0x0409);
        test(Locale.GERMAN, 0x0407);
        test(Locale.GERMANY, 0x0407);
        test(new Locale("de", "AT"), 0x0c07);
        test(new Locale("ar"), 0x0401);
        test(new Locale("ar", "SA"), 0x0401);
        test(new Locale("ar", "EG"), 0x0c01);
        test(new Locale("??"), 0x0409);
        test(new Locale("??", "??"), 0x0409);
        test(Locale.KOREA, 0x0412);
    }

    private static void test(Locale locale, int expectedLCID) {
        try {
            short lcid = (Short) getLCIDMethod.invoke(null, locale);
            System.out.println("lcid=" + lcid + " expected=" + expectedLCID);
            if (lcid != expectedLCID) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Method invocation exception");
        }
    }
}
