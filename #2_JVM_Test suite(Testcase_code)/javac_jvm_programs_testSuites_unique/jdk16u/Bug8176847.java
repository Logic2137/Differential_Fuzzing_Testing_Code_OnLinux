



import java.util.Calendar;
import java.util.Locale;

public class Bug8176847 {
    public static void main(String[] args) {
        Calendar c = new Calendar.Builder().build();

        try {
            c.getDisplayName(Calendar.MONTH, 3, Locale.US);
            throw new RuntimeException("IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException iae) {
            
        }

        try {
            c.getDisplayNames(Calendar.MONTH, 3, Locale.US);
            throw new RuntimeException("IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException iae) {
            
        }
    }
}
