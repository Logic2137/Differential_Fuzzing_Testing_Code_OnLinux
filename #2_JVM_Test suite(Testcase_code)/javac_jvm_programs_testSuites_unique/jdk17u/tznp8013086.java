
package providersrc.spi.src;

import java.util.spi.TimeZoneNameProvider;
import java.util.Locale;
import java.util.TimeZone;

public class tznp8013086 extends TimeZoneNameProvider {

    public String getDisplayName(String ID, boolean daylight, int style, Locale locale) {
        if (!daylight && style == TimeZone.LONG) {
            return "tznp8013086";
        } else {
            return null;
        }
    }

    public Locale[] getAvailableLocales() {
        Locale[] locales = { Locale.JAPAN };
        return locales;
    }
}
