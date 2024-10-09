



import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static java.util.GregorianCalendar.*;

@SuppressWarnings("deprecation")
public class Bug4955000 {

    
    
    public static void main(String[] args) {
        TimeZone defaultTZ = TimeZone.getDefault();
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("NST"));
            GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            
            int[] years1 = {
                Integer.MIN_VALUE,
                Integer.MIN_VALUE + 1,
                gc.getMinimum(YEAR) - 1,
                gc.getMaximum(YEAR) + 1,
                Integer.MAX_VALUE - 1,
                Integer.MAX_VALUE
            };
            for (int i = 0; i < years1.length; i++) {
                gc.clear();
                gc.set(years1[i], JANUARY, 1);
                long t = gc.getTimeInMillis();
                long utc = Date.UTC(years1[i] - 1900, 1 - 1, 1,
                        0, 0, 0); 
                if (t != utc) {
                    throw new RuntimeException("t (" + t + ") != utc (" + utc + ")");
                }
            }

            
            int[] years = {
                gc.getGreatestMinimum(YEAR),
                gc.getGreatestMinimum(YEAR) + 1,
                -1,
                0,
                1,
                gc.getLeastMaximum(YEAR) - 1,
                gc.getLeastMaximum(YEAR)
            };

            int[] months = {
                gc.getMinimum(MONTH),
                gc.getMinimum(MONTH) + 1,
                gc.getMaximum(MONTH) - 1,
                gc.getMaximum(MONTH)
            };

            int[] dates = {
                gc.getMinimum(DAY_OF_MONTH),
                gc.getMinimum(DAY_OF_MONTH) + 1,
                gc.getMaximum(DAY_OF_MONTH) - 1,
                gc.getMaximum(DAY_OF_MONTH)
            };

            int[] hs = {
                gc.getMinimum(HOUR),
                gc.getMinimum(HOUR) + 1,
                gc.getMaximum(HOUR) - 1,
                gc.getMaximum(HOUR)
            };

            int[] ms = {
                gc.getMinimum(MINUTE),
                gc.getMinimum(MINUTE) + 1,
                gc.getMaximum(MINUTE) - 1,
                gc.getMaximum(MINUTE)
            };

            int[] ss = {
                gc.getMinimum(SECOND),
                gc.getMinimum(SECOND) + 1,
                gc.getMaximum(SECOND) - 1,
                gc.getMaximum(SECOND)
            };

            for (int i = 0; i < years.length; i++) {
                for (int j = 0; j < months.length; j++) {
                    for (int k = 0; k < dates.length; k++) {
                        for (int m = 0; m < hs.length; m++) {
                            for (int n = 0; n < ms.length; n++) {
                                for (int p = 0; p < ss.length; p++) {
                                    int year = years[i] - 1900;
                                    int month = months[j];
                                    int date = dates[k];
                                    int hours = hs[m];
                                    int minutes = ms[n];
                                    int seconds = ss[p];

                                    long result = Date.UTC(year, month, date,
                                            hours, minutes, seconds);

                                    gc.clear();
                                    gc.set(year + 1900, month, date, hours, minutes, seconds);

                                    long expected = gc.getTime().getTime();

                                    if (expected != result) {
                                        throw new RuntimeException("expected (" + expected
                                                + ") != result (" + result + ")");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            TimeZone.setDefault(defaultTZ);
        }
    }
}
