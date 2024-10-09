import java.text.*;
import java.util.*;

public class Bug6215962 {

    public static void main(String[] args) {
        testMessageFormat();
        testChoiceFormat();
        testDateFormatSymbols();
    }

    static void testMessageFormat() {
        MessageFormat mf1 = new MessageFormat("{0}", null);
        MessageFormat mf2 = new MessageFormat("{0}", null);
        check(mf1, mf2, true);
        mf1.setLocale(null);
        check(mf1, mf2, true);
        mf1 = new MessageFormat("{0}", Locale.US);
        check(mf1, mf2, false);
        mf2 = new MessageFormat("{0}", Locale.JAPAN);
        check(mf1, mf2, false);
        mf1 = new MessageFormat("{0}", new Locale("ja", "JP"));
        check(mf1, mf2, true);
        mf1.setLocale(null);
        check(mf1, mf2, false);
        mf1 = new MessageFormat("{0}", new Locale("ja", "JP", "FOO"));
        check(mf1, mf2, false);
        mf2 = new MessageFormat("{1}", new Locale("ja", "JP", "FOO"));
        check(mf1, mf2, false);
        mf1 = new MessageFormat("{1}", new Locale("ja", "JP", "FOO"));
        check(mf1, mf2, true);
        mf1 = new MessageFormat("{1, date}", new Locale("ja", "JP", "FOO"));
        check(mf1, mf2, false);
        mf2 = new MessageFormat("{1, date}", new Locale("ja", "JP", "FOO"));
        check(mf1, mf2, true);
    }

    static void check(MessageFormat f1, MessageFormat f2, boolean expected) {
        boolean got = f1.equals(f2);
        if (got != expected) {
            throw new RuntimeException("Test failed for MessageFormat.equals(). Got: " + got + ", Expected: " + expected);
        }
    }

    static void testChoiceFormat() {
        double[] limits0 = { 0, 1, 2, 3, 4, 5, 6 };
        double[] limits1 = { 1, 2, 3, 4, 5, 6, 7 };
        String[] monthNames0 = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        String[] monthNames1 = { "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat" };
        ChoiceFormat cf1 = new ChoiceFormat(limits1, monthNames0);
        ChoiceFormat cf2 = new ChoiceFormat(limits1, monthNames0);
        check(cf1, cf2, true);
        cf2 = new ChoiceFormat(limits0, monthNames0);
        check(cf1, cf2, false);
        cf2 = new ChoiceFormat(limits1, monthNames1);
        check(cf1, cf2, false);
    }

    static void check(ChoiceFormat f1, ChoiceFormat f2, boolean expected) {
        boolean got = f1.equals(f2);
        if (got != expected) {
            throw new RuntimeException("Test failed for ChoiceFormat.equals(). Got: " + got + ", Expected: " + expected);
        }
    }

    static void testDateFormatSymbols() {
        DateFormatSymbols dfs1 = new DateFormatSymbols();
        DateFormatSymbols dfs2 = new DateFormatSymbols();
        check(dfs1, dfs2, true);
        String[] tmp = dfs1.getMonths();
        String saved = tmp[0];
        tmp[0] = "Foo";
        dfs1.setMonths(tmp);
        check(dfs1, dfs2, false);
        tmp[0] = saved;
        dfs1.setMonths(tmp);
        check(dfs1, dfs2, true);
        String pattern = dfs2.getLocalPatternChars();
        dfs2.setLocalPatternChars("Bar");
        check(dfs1, dfs2, false);
        dfs2.setLocalPatternChars(pattern);
        check(dfs1, dfs2, true);
        String[][] zones = dfs1.getZoneStrings();
        saved = zones[0][1];
        zones[0][1] = "Yokohama Summer Time";
        dfs1.setZoneStrings(zones);
        check(dfs1, dfs2, false);
        zones[0][1] = saved;
        dfs1.setZoneStrings(zones);
        check(dfs1, dfs2, true);
    }

    static void check(DateFormatSymbols dfs1, DateFormatSymbols dfs2, boolean expected) {
        boolean got = dfs1.equals(dfs2);
        if (got != expected) {
            throw new RuntimeException("Test failed for DateFormatSymbols.equals(). Got: " + got + ", Expected: " + expected);
        }
    }
}
