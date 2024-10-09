

package custom;

import java.util.Locale;
import java.util.TimeZone;
import java.util.spi.TimeZoneNameProvider;

public class CustomTimeZoneNameProvider extends TimeZoneNameProvider {

    public static final String ZONE_ID = "Custom/Timezone";

    @Override
    public String getDisplayName(String ID, boolean daylight, int style, Locale locale) {
        if (ZONE_ID.equals(ID)) {
            switch (style) {
                case TimeZone.SHORT:
                    if (daylight) {
                        return "CUST_ST";
                    } else {
                        return "CUST_WT";
                    }
                case TimeZone.LONG:
                    if (daylight) {
                        return "Custom Summer Time";
                    } else {
                        return "Custom Winter Time";
                    }
            }
        }
        return null;
    }

    @Override
    public String getGenericDisplayName(String ID, int style, Locale locale) {
        if (ZONE_ID.equals(ID)) {
            switch (style) {
                case TimeZone.SHORT:
                    return "Custom Time";
                case TimeZone.LONG:
                    return "Custom Timezone Time";
            }
        }
        return null;
    }

    @Override
    public boolean isSupportedLocale(Locale locale) {
        return true;
    }

    @Override
    public Locale[] getAvailableLocales() {
        return new Locale[]{
            Locale.getDefault()
        };
    }
}
