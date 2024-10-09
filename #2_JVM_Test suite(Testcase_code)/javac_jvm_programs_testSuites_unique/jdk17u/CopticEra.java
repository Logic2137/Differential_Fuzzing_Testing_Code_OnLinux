
package tck.java.time.chrono;

import java.time.DateTimeException;
import java.time.chrono.Era;

public enum CopticEra implements Era {

    BEFORE_AM, AM;

    public static CopticEra of(int era) {
        switch(era) {
            case 0:
                return BEFORE_AM;
            case 1:
                return AM;
            default:
                throw new DateTimeException("Invalid era: " + era);
        }
    }

    public int getValue() {
        return ordinal();
    }
}
