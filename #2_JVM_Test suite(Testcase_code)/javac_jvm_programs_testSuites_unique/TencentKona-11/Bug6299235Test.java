



import java.awt.Toolkit;
import java.util.Locale;



public class Bug6299235Test {
    private static final Locale ru_RU = new Locale("ru", "RU");

    public static void main(String args[]) {
        Locale locale = Locale.getDefault();
        try {
            Locale.setDefault(ru_RU);
            
            String value = Toolkit.getProperty("foo", "undefined");
            if (!value.equals("bar")) {
                throw new RuntimeException("key = foo, value = " + value);
            }
            
            value = Toolkit.getProperty("AWT.enter", "DO NOT ENTER");
            if (value.equals("DO NOT ENTER")) {
                throw new RuntimeException("AWT.enter undefined.");
            }
        } finally {
            
            Locale.setDefault(locale);
        }
        System.out.println("Bug6299235Test passed");
    }
}
