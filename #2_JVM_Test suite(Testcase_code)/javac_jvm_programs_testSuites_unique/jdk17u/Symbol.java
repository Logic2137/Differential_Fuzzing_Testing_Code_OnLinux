import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import static java.util.Calendar.*;
import static java.util.GregorianCalendar.*;

public class Symbol {

    private static Map<String, Symbol> symbols;

    String name;

    Type type;

    int value;

    boolean isArithmetic;

    private Symbol(Type type, Integer value) {
        this(type, value, false);
    }

    private Symbol(Type type) {
        this(type, null, false);
    }

    private Symbol(Type type, boolean isArithmetic) {
        this(type, null, isArithmetic);
    }

    private Symbol(Type type, Integer value, boolean isArithmetic) {
        this.name = type.toString().toLowerCase(Locale.ROOT);
        this.type = type;
        if (value != null)
            this.value = value;
        this.isArithmetic = isArithmetic;
    }

    public int value() {
        return value;
    }

    public String toString() {
        return type.name();
    }

    public boolean isArithmetic() {
        return isArithmetic;
    }

    public static Symbol get(String s) {
        return symbols.get(s);
    }

    public static enum Type {

        TEST,
        LOCALE,
        TIMEZONE,
        NEW,
        USE,
        ASSIGN,
        EVAL,
        CLEAR,
        SET,
        GET,
        ADD,
        ROLL,
        CHECK,
        PRINT,
        EXCEPTION,
        IF,
        INSTANCE,
        GREGORIAN,
        ALL,
        MILLIS,
        DATE,
        DATETIME,
        TIMEOFDAY,
        LENIENT,
        NONLENIENT,
        MINIMUM,
        GREATESTMINIMUM,
        ACTUALMINIMUM,
        MAXIMUM,
        LEASTMAXIMUM,
        ACTUALMAXIMUM,
        FIRSTDAYOFWEEK,
        MINIMALDAYSINFIRSTWEEK,
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        MOD,
        GT,
        GE,
        EQ,
        NEQ,
        LE,
        LT,
        FIELD,
        DAYOFWEEK,
        MONTH,
        AMPM,
        ERA
    }

    private static final void put(String key, Symbol sym) {
        Symbol s = symbols.put(key, sym);
        if (s != null) {
            throw new RuntimeException("duplicated key: " + key);
        }
    }

    static {
        symbols = new HashMap<String, Symbol>();
        Symbol sym;
        put("test", new Symbol(Type.TEST));
        put("locale", new Symbol(Type.LOCALE));
        sym = new Symbol(Type.TIMEZONE);
        put("tz", sym);
        put("timezone", sym);
        put("new", new Symbol(Type.NEW));
        put("use", new Symbol(Type.USE));
        put("assign", new Symbol(Type.ASSIGN));
        put("eval", new Symbol(Type.EVAL));
        put("clear", new Symbol(Type.CLEAR));
        put("set", new Symbol(Type.SET));
        put("get", new Symbol(Type.GET));
        put("add", new Symbol(Type.ADD));
        put("roll", new Symbol(Type.ROLL));
        put("check", new Symbol(Type.CHECK));
        put("print", new Symbol(Type.PRINT));
        put("exception", new Symbol(Type.EXCEPTION));
        put("throw", get("exception"));
        put("if", new Symbol(Type.IF));
        put("instance", new Symbol(Type.INSTANCE));
        put("gregorian", new Symbol(Type.GREGORIAN));
        put("all", new Symbol(Type.ALL));
        put("millis", new Symbol(Type.MILLIS));
        put("date", new Symbol(Type.DATE));
        put("datetime", new Symbol(Type.DATETIME));
        put("timeofday", new Symbol(Type.TIMEOFDAY));
        put("lenient", new Symbol(Type.LENIENT));
        sym = new Symbol(Type.NONLENIENT);
        put("non-lenient", sym);
        put("nonlenient", sym);
        put("firstdayofweek", new Symbol(Type.FIRSTDAYOFWEEK));
        put("minimaldaysinfirstweek", new Symbol(Type.MINIMALDAYSINFIRSTWEEK));
        sym = new Symbol(Type.MINIMUM);
        put("minimum", sym);
        put("min", sym);
        sym = new Symbol(Type.GREATESTMINIMUM);
        put("greatestminimum", sym);
        put("greatestmin", sym);
        sym = new Symbol(Type.ACTUALMINIMUM);
        put("actualminimum", sym);
        put("actualmin", sym);
        sym = new Symbol(Type.MAXIMUM);
        put("maximum", sym);
        put("max", sym);
        sym = new Symbol(Type.LEASTMAXIMUM);
        put("leastmaximum", sym);
        put("leastmax", sym);
        sym = new Symbol(Type.ACTUALMAXIMUM);
        put("actualmaximum", sym);
        put("actualmax", sym);
        put("+", new Symbol(Type.PLUS, true));
        put("-", new Symbol(Type.MINUS, true));
        put("*", new Symbol(Type.MULTIPLY, true));
        put("/", new Symbol(Type.DIVIDE, true));
        put("%", new Symbol(Type.MOD, true));
        put(">", new Symbol(Type.GT, false));
        put(">=", new Symbol(Type.GE, false));
        put("==", new Symbol(Type.EQ, false));
        put("!=", new Symbol(Type.NEQ, false));
        put("<=", new Symbol(Type.LE, false));
        put("<", new Symbol(Type.LT, false));
        put("era", new Symbol(Type.FIELD, ERA));
        put("year", new Symbol(Type.FIELD, YEAR));
        put("month", new Symbol(Type.FIELD, MONTH));
        sym = new Symbol(Type.FIELD, WEEK_OF_YEAR);
        put("week_of_year", sym);
        put("weekofyear", sym);
        put("woy", sym);
        sym = new Symbol(Type.FIELD, WEEK_OF_MONTH);
        put("week_of_month", sym);
        put("weekofmonth", sym);
        put("wom", sym);
        sym = new Symbol(Type.FIELD, DAY_OF_MONTH);
        put("day_of_month", sym);
        put("dayofmonth", sym);
        put("dom", sym);
        sym = new Symbol(Type.FIELD, DAY_OF_YEAR);
        put("day_of_year", sym);
        put("dayofyear", sym);
        put("doy", sym);
        sym = new Symbol(Type.FIELD, DAY_OF_WEEK);
        put("day_of_week", sym);
        put("dayofweek", sym);
        put("dow", sym);
        sym = new Symbol(Type.FIELD, DAY_OF_WEEK_IN_MONTH);
        put("day_of_week_in_month", sym);
        put("dayofweekinmonth", sym);
        put("dowim", sym);
        sym = new Symbol(Type.FIELD, AM_PM);
        put("am_pm", sym);
        put("ampm", sym);
        put("hour", new Symbol(Type.FIELD, HOUR));
        sym = new Symbol(Type.FIELD, HOUR_OF_DAY);
        put("hour_of_day", sym);
        put("hourofday", sym);
        put("hod", sym);
        put("minute", new Symbol(Type.FIELD, MINUTE));
        put("second", new Symbol(Type.FIELD, SECOND));
        put("millisecond", new Symbol(Type.FIELD, MILLISECOND));
        sym = new Symbol(Type.FIELD, ZONE_OFFSET);
        put("zone_offset", sym);
        put("zoneoffset", sym);
        put("zo", sym);
        sym = new Symbol(Type.FIELD, DST_OFFSET);
        put("dst_offset", sym);
        put("dstoffset", sym);
        put("do", sym);
        sym = new Symbol(Type.DAYOFWEEK, SUNDAY);
        put("sunday", sym);
        put("sun", sym);
        sym = new Symbol(Type.DAYOFWEEK, MONDAY);
        put("monday", sym);
        put("mon", sym);
        sym = new Symbol(Type.DAYOFWEEK, TUESDAY);
        put("tuesday", sym);
        put("tue", sym);
        sym = new Symbol(Type.DAYOFWEEK, WEDNESDAY);
        put("wednesday", sym);
        put("wed", sym);
        sym = new Symbol(Type.DAYOFWEEK, THURSDAY);
        put("thursday", sym);
        put("thu", sym);
        sym = new Symbol(Type.DAYOFWEEK, FRIDAY);
        put("friday", sym);
        put("fri", sym);
        sym = new Symbol(Type.DAYOFWEEK, SATURDAY);
        put("saturday", sym);
        put("sat", sym);
        sym = new Symbol(Type.MONTH, JANUARY);
        put("january", sym);
        put("jan", sym);
        sym = new Symbol(Type.MONTH, FEBRUARY);
        put("february", sym);
        put("feb", sym);
        sym = new Symbol(Type.MONTH, MARCH);
        put("march", sym);
        put("mar", sym);
        sym = new Symbol(Type.MONTH, APRIL);
        put("april", sym);
        put("apr", sym);
        sym = new Symbol(Type.MONTH, MAY);
        put("may", sym);
        sym = new Symbol(Type.MONTH, JUNE);
        put("june", sym);
        put("jun", sym);
        sym = new Symbol(Type.MONTH, JULY);
        put("july", sym);
        put("jul", sym);
        sym = new Symbol(Type.MONTH, AUGUST);
        put("august", sym);
        put("aug", sym);
        sym = new Symbol(Type.MONTH, SEPTEMBER);
        put("september", sym);
        put("sep", sym);
        sym = new Symbol(Type.MONTH, OCTOBER);
        put("octobwe", sym);
        put("oct", sym);
        sym = new Symbol(Type.MONTH, NOVEMBER);
        put("november", sym);
        put("nov", sym);
        sym = new Symbol(Type.MONTH, DECEMBER);
        put("december", sym);
        put("dec", sym);
        sym = new Symbol(Type.MONTH, UNDECIMBER);
        put("undecimber", sym);
        put("am", new Symbol(Type.AMPM, AM));
        put("pm", new Symbol(Type.AMPM, PM));
        sym = new Symbol(Type.ERA, BC);
        put("bc", sym);
        put("bce", sym);
        sym = new Symbol(Type.ERA, AD);
        put("ad", sym);
        put("ce", sym);
        put("be", new Symbol(Type.ERA, 1));
        sym = new Symbol(Type.ERA, 0);
        put("before_meiji", sym);
        put("beforemeiji", sym);
        put("meiji", new Symbol(Type.ERA, 1));
        put("taisho", new Symbol(Type.ERA, 2));
        put("showa", new Symbol(Type.ERA, 3));
        put("heisei", new Symbol(Type.ERA, 4));
        put("reiwa", new Symbol(Type.ERA, 5));
    }
}
