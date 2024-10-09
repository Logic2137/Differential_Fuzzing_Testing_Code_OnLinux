
package nontestng.java.time.chrono;

import java.time.chrono.HijrahChronology;


public class Bug8178823 {
    public static void main(String[] args) {
        HijrahChronology.INSTANCE.isLeapYear(2017);
    }
}
