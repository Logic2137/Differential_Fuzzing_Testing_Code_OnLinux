
package providersrc.spi.src;

import java.util.spi.TimeZoneNameProvider;
import java.util.Locale;

public class tznp extends TimeZoneNameProvider {

    public String getDisplayName(String ID, boolean daylight, int style, Locale locale) {
        return "tznp";
    }

    public Locale[] getAvailableLocales() {
        Locale[] locales = { Locale.US };
        return locales;
    }
}
