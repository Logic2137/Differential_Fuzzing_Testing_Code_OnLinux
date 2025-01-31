



import java.util.Locale;
import java.util.TimeZone;

public class Bug6377794 {
    static Locale[] locales2Test = new Locale[] {
        new Locale("en"),
        new Locale("de"),
        new Locale("es"),
        new Locale("fr"),
        new Locale("it"),
        new Locale("ja"),
        new Locale("ko"),
        new Locale("sv"),
        new Locale("zh","CN"),
        new Locale("zh","TW")
    };

    public static void main(String[] args) {
        TimeZone SystemVYST9 = TimeZone.getTimeZone("SystemV/YST9");
        Locale tzLocale;
        for (int i = 0; i < locales2Test.length; i++) {
            tzLocale = locales2Test[i];
            if (!SystemVYST9.getDisplayName(false, TimeZone.SHORT, tzLocale).equals
               ("AKST"))
                throw new RuntimeException("\n" + tzLocale + ": SHORT, " +
                                           "non-daylight saving name for " +
                                           "SystemV/YST9 should be \"AKST\"");
        }



            TimeZone SystemVPST8 = TimeZone.getTimeZone("SystemV/PST8");
            tzLocale = locales2Test[0];
            if (!SystemVPST8.getDisplayName(false, TimeZone.LONG, tzLocale).equals
               ("Pacific Standard Time"))
                throw new RuntimeException("\n" + tzLocale + ": LONG, " +
                                           "non-daylight saving name for " +
                                           "SystemV/PST8 should be " +
                                           "\"Pacific Standard Time\"");
            tzLocale = locales2Test[1];
            if (!SystemVPST8.getDisplayName(false, TimeZone.LONG, tzLocale).equals
               ("Pazifische Normalzeit"))
                throw new RuntimeException("\n" + tzLocale + ": LONG, " +
                                           "non-daylight saving name for " +
                                           "SystemV/PST8 should be " +
                                           "\"Pazifische Normalzeit\"");
            tzLocale = locales2Test[2];
            if (!SystemVPST8.getDisplayName(false, TimeZone.LONG, tzLocale).equals
               ("Hora est\u00e1ndar del Pac\u00edfico"))
                throw new RuntimeException("\n" + tzLocale + ": LONG, " +
                                           "non-daylight saving name for " +
                                           "SystemV/PST8 should be " +
                                           "\"Hora est\u00e1ndar del Pac\u00edfico\"");
            tzLocale = locales2Test[3];
            if (!SystemVPST8.getDisplayName(false, TimeZone.LONG, tzLocale).equals
               ("Heure normale du Pacifique"))
                throw new RuntimeException("\n" + tzLocale + ": LONG, " +
                                           "non-daylight saving name for " +
                                           "SystemV/PST8 should be " +
                                           "\"Heure normale du Pacifique\"");
            tzLocale = locales2Test[4];
            if (!SystemVPST8.getDisplayName(false, TimeZone.LONG, tzLocale).equals
               ("Ora solare della costa occidentale USA"))
                throw new RuntimeException("\n" + tzLocale + ": LONG, " +
                                           "non-daylight saving name for " +
                                           "SystemV/PST8 should be " +
                                           "\"Ora solare della costa occidentale USA\"");
            tzLocale = locales2Test[5];
            if (!SystemVPST8.getDisplayName(false, TimeZone.LONG, tzLocale).equals
               ("\u592a\u5e73\u6d0b\u6a19\u6e96\u6642"))
                throw new RuntimeException("\n" + tzLocale + ": LONG, " +
                                           "non-daylight saving name for " +
                                           "SystemV/PST8 should be " +
                                           "\"\u592a\u5e73\u6d0b\u6a19\u6e96\u6642\"");
            tzLocale = locales2Test[6];
            if (!SystemVPST8.getDisplayName(false, TimeZone.LONG, tzLocale).equals
               ("\ud0dc\ud3c9\uc591 \ud45c\uc900\uc2dc"))
                throw new RuntimeException("\n" + tzLocale + ": LONG, " +
                                           "non-daylight saving name for " +
                                           "SystemV/PST8 should be " +
                                           "\"\ud0dc\ud3c9\uc591 \ud45c\uc900\uc2dc\"");
            tzLocale = locales2Test[7];
            if (!SystemVPST8.getDisplayName(false, TimeZone.LONG, tzLocale).equals
               ("Stilla havet, normaltid"))
                throw new RuntimeException("\n" + tzLocale + ": LONG, " +
                                           "non-daylight saving name for " +
                                           "SystemV/PST8 should be " +
                                           "\"Stilla havet, normaltid\"");
            tzLocale = locales2Test[8];
            if (!SystemVPST8.getDisplayName(false, TimeZone.LONG, tzLocale).equals
               ("\u592a\u5e73\u6d0b\u6807\u51c6\u65f6\u95f4"))
                throw new RuntimeException("\n" + tzLocale + ": LONG, " +
                                           "non-daylight saving name for " +
                                           "SystemV/PST8 should be " +
                                           "\"\u592a\u5e73\u6d0b\u6807\u51c6\u65f6\u95f4\"");
            tzLocale = locales2Test[9];
            if (!SystemVPST8.getDisplayName(false, TimeZone.LONG, tzLocale).equals
               ("\u592a\u5e73\u6d0b\u6a19\u6e96\u6642\u9593"))
                throw new RuntimeException("\n" + tzLocale + ": LONG, " +
                                           "non-daylight saving name for " +
                                           "SystemV/PST8 should be " +
                                           "\"\u592a\u5e73\u6d0b\u6a19\u6e96\u6642\u9593\"");
   }
}
