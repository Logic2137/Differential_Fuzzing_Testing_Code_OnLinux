



import java.util.Arrays;
import java.util.List;
import java.util.Locale.FilteringMode;
import java.util.stream.Collectors;

public class FilteringModeTest {
    private static boolean err = false;
    private static List<String> modeNames = List.of("AUTOSELECT_FILTERING",
                                                    "EXTENDED_FILTERING",
                                                    "IGNORE_EXTENDED_RANGES",
                                                    "MAP_EXTENDED_RANGES",
                                                    "REJECT_EXTENDED_RANGES");

    public static void main(String[] args) throws Exception {
        testValues();
        testValueOf();

        if (err) {
            throw new RuntimeException("Failed.");
        }
    }

    private static void testValueOf() {
        try {
            FilteringMode.valueOf("").name();
            err = true;
            System.err.println("IAE should be thrown for valueOf(\"\").");
        } catch (IllegalArgumentException ex) {
        }

        try {
            FilteringMode.valueOf(null).name();
            err = true;
            System.err.println("NPE should be thrown for valueOf(null).");
        } catch (NullPointerException ex) {
        }

        modeNames.forEach((expectedName) -> {
            String name = FilteringMode.valueOf(expectedName).name();
            if (!expectedName.equals(name)) {
                err = true;
                System.err.println("FilteringMode.valueOf(" + expectedName
                        + ") returned unexpected value. Expected: "
                        + expectedName + ", got: " + name);
            }
        });
    }

    private static void testValues() {
        FilteringMode[] modeArray = FilteringMode.values();
        List<String> modeNames2 = Arrays.stream(modeArray)
                .map(mode -> mode.name())
                .collect(Collectors.toList());

        if (!modeNames.equals(modeNames2)) {
            err = true;
            System.err.println("FilteringMode.values() returned unexpected value. Expected:"
                    + modeNames + " Got:" + modeNames2);
        }
    }
}
