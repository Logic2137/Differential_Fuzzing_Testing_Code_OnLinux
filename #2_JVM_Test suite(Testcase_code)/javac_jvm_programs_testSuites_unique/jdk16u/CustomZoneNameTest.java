

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;


public class CustomZoneNameTest {

    private final static long now = 1575669972372L;
    private final static Instant instant = Instant.ofEpochMilli(now);
    private final static ZoneId customZone = ZoneId.of("Custom/Timezone");

    
    private final static Map<String, String>  formats = Map.of(
        "yyyy-MM-dd HH:mm:ss.SSS VV", "2019-12-06 22:06:12.372 Custom/Timezone",
        "yyyy-MM-dd HH:mm:ss.SSS z", "2019-12-06 22:06:12.372 CUST_WT",
        "yyyy-MM-dd HH:mm:ss.SSS zzzz", "2019-12-06 22:06:12.372 Custom Winter Time",
        "yyyy-MM-dd HH:mm:ss.SSS v", "2019-12-06 22:06:12.372 Custom Time",
        "yyyy-MM-dd HH:mm:ss.SSS vvvv", "2019-12-06 22:06:12.372 Custom Timezone Time"
    );

    public static void main(String... args) {
        testFormatting();
        testParsing();
    }

    private static void testFormatting() {
        var customZDT = ZonedDateTime.ofInstant(instant, customZone);
        formats.entrySet().stream()
            .filter(e -> {
                var formatted = DateTimeFormatter.ofPattern(e.getKey()).format(customZDT);
                var expected = e.getValue();
                System.out.println("testFormatting. Pattern: " + e.getKey() +
                        ", expected: " + expected +
                        ", formatted: " + formatted);
                return !formatted.equals(expected);
            })
            .findAny()
            .ifPresent(e -> {
                throw new RuntimeException(
                        "Provider's custom name was not retrieved for the format " +
                        e.getKey());
            });
    }

    public static void testParsing() {
        formats.entrySet().stream()
            .filter(e -> {
                var fmt = DateTimeFormatter.ofPattern(e.getKey());
                var input = e.getValue();
                var parsedInstant = fmt.parse(input, Instant::from).toEpochMilli();
                var parsedZone = fmt.parse(input, ZonedDateTime::from).getZone();
                System.out.println("testParsing. Input: " + input +
                        ", expected instant: " + now +
                        ", expected zone: " + customZone +
                        ", parsed instant: " + parsedInstant +
                        ", parsed zone: " + parsedZone);
                return parsedInstant != now ||
                        !parsedZone.equals(customZone);
            })
            .findAny()
            .ifPresent(e -> {
                throw new RuntimeException("Parsing failed for the format " +
                                e.getKey());
            });
    }
}
