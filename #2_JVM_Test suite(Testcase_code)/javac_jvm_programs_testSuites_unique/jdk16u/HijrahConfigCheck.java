

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.Chronology;
import java.util.Locale;

public class HijrahConfigCheck {
    private static final String CALTYPE = "islamic-test";

    public static void main(String... args) {
        
        if (Chronology.getAvailableChronologies().stream()
                .filter(c -> c.getCalendarType().equals(CALTYPE))
                .count() != 1) {
            throw new RuntimeException(CALTYPE + " chronology was not found, or " +
                    "appeared more than once in Chronology.getAvailableChronologies()");
        }

        
        Chronology c1 = Chronology.of(CALTYPE);
        Chronology c2 = Chronology.ofLocale(Locale.forLanguageTag("und-u-ca-" + CALTYPE ));
        if (!c1.equals(c2)) {
            throw new RuntimeException(CALTYPE + " chronologies differ. c1: " + c1 +
                                        ", c2: " + c2);
        }

        
        
        LocalDateTime iso = LocalDateTime.of(LocalDate.of(2020, 1, 10), LocalTime.MIN);
        ChronoLocalDateTime hijrah = c1.date(1000, 1, 10).atTime(LocalTime.MIN);
        if (!iso.toInstant(ZoneOffset.UTC).equals(hijrah.toInstant(ZoneOffset.UTC))) {
            throw new RuntimeException("test Hijrah date is incorrect. LocalDate: " +
                    iso + ", test date: " + hijrah);
        }
    }
}
