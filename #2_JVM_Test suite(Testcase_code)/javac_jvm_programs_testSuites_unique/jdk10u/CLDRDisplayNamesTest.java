



import java.text.*;
import java.util.*;
import static java.util.TimeZone.*;

public class CLDRDisplayNamesTest {
    
    static final String[][] CLDR_DATA = {
        {
            "ja-JP",
            "\u30a2\u30e1\u30ea\u30ab\u592a\u5e73\u6d0b\u6a19\u6e96\u6642",
            "PST",
            "\u30a2\u30e1\u30ea\u30ab\u592a\u5e73\u6d0b\u590f\u6642\u9593",
            "PDT",
            
            
        },
        {
            "zh-CN",
            "\u5317\u7f8e\u592a\u5e73\u6d0b\u6807\u51c6\u65f6\u95f4",
            "PST",
            "\u5317\u7f8e\u592a\u5e73\u6d0b\u590f\u4ee4\u65f6\u95f4",
            "PDT",
            
            
        },
        {
            "de-DE",
            "Nordamerikanische Westk\u00fcsten-Normalzeit",
            "PST",
            "Nordamerikanische Westk\u00fcsten-Sommerzeit",
            "PDT",
            
            
        },
    };

    public static void main(String[] args) {
        
        
        TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
        int errors = 0;
        for (String[] data : CLDR_DATA) {
            Locale locale = Locale.forLanguageTag(data[0]);
            for (int i = 1; i < data.length; i++) {
                int style = ((i % 2) == 1) ? LONG : SHORT;
                boolean daylight = (i == 3 || i == 4);
                String name = tz.getDisplayName(daylight, style, locale);
                if (!data[i].equals(name)) {
                    System.err.printf("error: got '%s' expected '%s' (style=%d, daylight=%s, locale=%s)%n",
                                      name, data[i], style, daylight, locale);
                    errors++;
                }
            }
        }

        
        tz = TimeZone.getTimeZone("Europe/Vienna");
        String name = tz.getDisplayName(false, SHORT, Locale.ENGLISH);
        if (!"CET".equals(name)) {
            System.err.printf("error: got '%s' expected 'CET' %n", name);
            errors++;
        }

        
        SimpleDateFormat fmtROOT = new SimpleDateFormat("EEE MMM d hh:mm:ss z yyyy", Locale.ROOT);
        SimpleDateFormat fmtUS = new SimpleDateFormat("EEE MMM d hh:mm:ss z yyyy", Locale.US);
        SimpleDateFormat fmtUK = new SimpleDateFormat("EEE MMM d hh:mm:ss z yyyy", Locale.UK);
        Locale originalLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.ROOT);
            fmtROOT.parse("Thu Nov 13 04:35:51 AKST 2008");
            fmtUS.parse("Thu Nov 13 04:35:51 AKST 2008");
            fmtUK.parse("Thu Nov 13 04:35:51 GMT-09:00 2008");
        } catch (ParseException pe) {
            System.err.println(pe);
            errors++;
        } finally {
            Locale.setDefault(originalLocale);
        }

        if (errors > 0) {
            throw new RuntimeException("test failed");
        }
    }
}
