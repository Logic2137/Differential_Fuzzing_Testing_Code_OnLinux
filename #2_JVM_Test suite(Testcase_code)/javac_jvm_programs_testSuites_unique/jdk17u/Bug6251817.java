import java.text.*;
import java.util.*;

public class Bug6251817 {

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("zzzz", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("Australia/Lord_Howe"));
        String got = sdf.format(new Date());
        if (!got.equals("Lord Howe Standard Time") && !got.equals("Lord Howe Daylight Time")) {
            throw new RuntimeException("Timezone display name for Australia/Lord_Howe is incorrect. Got:" + got);
        }
    }
}
