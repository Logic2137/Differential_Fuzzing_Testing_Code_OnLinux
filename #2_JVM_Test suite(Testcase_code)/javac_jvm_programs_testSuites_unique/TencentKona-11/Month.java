

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


enum Month {
    JANUARY("Jan"),
    FEBRUARY("Feb"),
    MARCH("Mar"),
    APRIL("Apr"),
    MAY("May"),
    JUNE("Jun"),
    JULY("Jul"),
    AUGUST("Aug"),
    SEPTEMBER("Sep"),
    OCTOBER("Oct"),
    NOVEMBER("Nov"),
    DECEMBER("Dec");

    private final String abbr;

    private static final Map<String,Month> abbreviations
                                = new HashMap<String,Month>(12);

    static {
        for (Month m : Month.values()) {
            abbreviations.put(m.abbr, m);
        }
    }

    private Month(String abbr) {
        this.abbr = abbr;
    }

    int value() {
        return ordinal() + 1;
    }

    
    static Month parse(String name) {
        Month m = abbreviations.get(name);
        if (m != null) {
            return m;
        }
        return null;
    }

    
    static String toString(int month) {
        if (month >= JANUARY.value() && month <= DECEMBER.value()) {
            return "Calendar." + Month.values()[month - 1];
        }
        throw new IllegalArgumentException("wrong month number: " + month);
    }
}
