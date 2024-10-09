import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Bug4994312 {

    public static void main(String[] args) {
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.GERMAN);
        df.applyLocalizedPattern("tt.MM.uuuu");
        System.out.println(df.format(new Date()));
    }
}
