import java.lang.reflect.*;
import java.util.*;
import static java.util.Calendar.*;

public class StampOverflow {

    public static void main(String[] args) throws IllegalAccessException {
        Field nextstamp = null;
        try {
            nextstamp = Calendar.class.getDeclaredField("nextStamp");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("implementation changed?", e);
        }
        nextstamp.setAccessible(true);
        Calendar cal = new GregorianCalendar();
        int initialValue = nextstamp.getInt(cal);
        nextstamp.setInt(cal, Integer.MAX_VALUE - 100);
        for (int i = 0; i < 1000; i++) {
            invoke(cal);
            int stampValue = nextstamp.getInt(cal);
            if (stampValue < initialValue) {
                throw new RuntimeException("invalid nextStamp: " + stampValue);
            }
        }
    }

    static void invoke(Calendar cal) {
        cal.clear();
        cal.set(2000, NOVEMBER, 2, 0, 0, 0);
        int y = cal.get(YEAR);
        int m = cal.get(MONTH);
        int d = cal.get(DAY_OF_MONTH);
        if (y != 2000 || m != NOVEMBER || d != 2) {
            throw new RuntimeException("wrong date produced (" + y + "/" + (m + 1) + "/" + d + ")");
        }
    }
}
