import java.util.*;
import java.text.*;

public class Bug5097350 {

    public static void main(String[] args) {
        String[] tzids = TimeZone.getAvailableIDs();
        List<String> ids = new ArrayList<>(tzids.length + 10);
        ids.addAll(Arrays.asList(tzids));
        ids.add("GMT+1");
        ids.add("GMT-7:00");
        ids.add("GMT+10:20");
        ids.add("GMT-00:00");
        ids.add("GMT+00:00");
        for (String id : ids) {
            test(id);
        }
    }

    private static void test(String id) {
        TimeZone tz1 = TimeZone.getTimeZone(id);
        int offset1 = tz1.getRawOffset();
        tz1.setRawOffset(offset1 + 13 * 60 * 60 * 1000);
        TimeZone tz2 = TimeZone.getTimeZone(id);
        if (tz1 == tz2) {
            throw new RuntimeException("TimeZones are identical: " + id);
        }
        if (offset1 != tz2.getRawOffset()) {
            throw new RuntimeException("Offset changed through aliasing: " + id);
        }
    }
}
