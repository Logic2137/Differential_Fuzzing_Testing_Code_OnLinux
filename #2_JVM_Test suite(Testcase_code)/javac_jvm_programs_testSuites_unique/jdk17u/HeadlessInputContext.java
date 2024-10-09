import java.awt.im.InputContext;
import java.util.Locale;

public class HeadlessInputContext {

    public static void main(String[] args) {
        InputContext ic = InputContext.getInstance();
        for (Locale locale : Locale.getAvailableLocales()) ic.selectInputMethod(locale);
        ic.getLocale();
    }
}
