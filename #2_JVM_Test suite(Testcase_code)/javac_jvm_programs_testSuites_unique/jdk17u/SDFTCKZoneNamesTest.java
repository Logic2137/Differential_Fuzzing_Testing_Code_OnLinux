import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class SDFTCKZoneNamesTest {

    StringBuffer myFormat(Date date, SimpleDateFormat sdf) {
        String pattern = sdf.toPattern();
        StringBuffer toAppendTo = new StringBuffer("");
        boolean inQuote = false;
        char prevCh = 0;
        char ch;
        int count = 0;
        for (int i = 0; i < pattern.length(); i++) {
            ch = pattern.charAt(i);
            if (inQuote) {
                if (ch == '\'') {
                    inQuote = false;
                    if (count == 0)
                        toAppendTo.append(ch);
                    else
                        count = 0;
                } else {
                    toAppendTo.append(ch);
                    count++;
                }
            } else {
                if (ch == '\'') {
                    inQuote = true;
                    if (count > 0) {
                        toAppendTo.append(subFormat(prevCh, count, date, sdf));
                        count = 0;
                        prevCh = 0;
                    }
                } else if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z') {
                    if (ch != prevCh && count > 0) {
                        toAppendTo.append(subFormat(prevCh, count, date, sdf));
                        prevCh = ch;
                        count = 1;
                    } else {
                        if (ch != prevCh)
                            prevCh = ch;
                        count++;
                    }
                } else if (count > 0) {
                    toAppendTo.append(subFormat(prevCh, count, date, sdf));
                    toAppendTo.append(ch);
                    prevCh = 0;
                    count = 0;
                } else
                    toAppendTo.append(ch);
            }
        }
        if (count > 0) {
            toAppendTo.append(subFormat(prevCh, count, date, sdf));
        }
        return toAppendTo;
    }

    private String subFormat(char ch, int count, Date date, SimpleDateFormat sdf) throws IllegalArgumentException {
        int value = 0;
        int patternCharIndex = -1;
        int maxIntCount = 10;
        String current = "";
        DateFormatSymbols formatData = sdf.getDateFormatSymbols();
        Calendar calendar = sdf.getCalendar();
        calendar.setTime(date);
        NumberFormat nf = sdf.getNumberFormat();
        nf.setGroupingUsed(false);
        if ((patternCharIndex = "GyMdkHmsSEDFwWahKz".indexOf(ch)) == -1)
            throw new IllegalArgumentException("Illegal pattern character " + "'" + ch + "'");
        switch(patternCharIndex) {
            case 0:
                value = calendar.get(Calendar.ERA);
                current = formatData.getEras()[value];
                break;
            case 1:
                value = calendar.get(Calendar.YEAR);
                if (count == 2) {
                    current = zeroPaddingNumber(value, 2, 2, nf);
                } else {
                    current = zeroPaddingNumber(value, count, maxIntCount, nf);
                }
                break;
            case 2:
                value = calendar.get(Calendar.MONTH);
                if (count >= 4)
                    current = formatData.getMonths()[value];
                else if (count == 3)
                    current = formatData.getShortMonths()[value];
                else
                    current = zeroPaddingNumber(value + 1, count, maxIntCount, nf);
                break;
            case 3:
                value = calendar.get(Calendar.DATE);
                current = zeroPaddingNumber(value, count, maxIntCount, nf);
                break;
            case 4:
                if ((value = calendar.get(Calendar.HOUR_OF_DAY)) == 0)
                    current = zeroPaddingNumber(calendar.getMaximum(Calendar.HOUR_OF_DAY) + 1, count, maxIntCount, nf);
                else
                    current = zeroPaddingNumber(value, count, maxIntCount, nf);
                break;
            case 5:
                value = calendar.get(Calendar.HOUR_OF_DAY);
                current = zeroPaddingNumber(value, count, maxIntCount, nf);
                break;
            case 6:
                value = calendar.get(Calendar.MINUTE);
                current = zeroPaddingNumber(value, count, maxIntCount, nf);
                break;
            case 7:
                value = calendar.get(Calendar.SECOND);
                current = zeroPaddingNumber(value, count, maxIntCount, nf);
                break;
            case 8:
                value = calendar.get(Calendar.MILLISECOND);
                current = zeroPaddingNumber(value, count, maxIntCount, nf);
                break;
            case 9:
                value = calendar.get(Calendar.DAY_OF_WEEK);
                if (count >= 4)
                    current = formatData.getWeekdays()[value];
                else
                    current = formatData.getShortWeekdays()[value];
                break;
            case 10:
                value = calendar.get(Calendar.DAY_OF_YEAR);
                current = zeroPaddingNumber(value, count, maxIntCount, nf);
                break;
            case 11:
                value = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
                current = zeroPaddingNumber(value, count, maxIntCount, nf);
                break;
            case 12:
                value = calendar.get(Calendar.WEEK_OF_YEAR);
                current = zeroPaddingNumber(value, count, maxIntCount, nf);
                break;
            case 13:
                value = calendar.get(Calendar.WEEK_OF_MONTH);
                current = zeroPaddingNumber(value, count, maxIntCount, nf);
                break;
            case 14:
                value = calendar.get(Calendar.AM_PM);
                current = formatData.getAmPmStrings()[value];
                break;
            case 15:
                if ((value = calendar.get(Calendar.HOUR)) == 0)
                    current = zeroPaddingNumber(calendar.getLeastMaximum(Calendar.HOUR) + 1, count, maxIntCount, nf);
                else
                    current = zeroPaddingNumber(value, count, maxIntCount, nf);
                break;
            case 16:
                value = calendar.get(Calendar.HOUR);
                current = zeroPaddingNumber(value, count, maxIntCount, nf);
                break;
            case 17:
                int zoneIndex = getZoneIndex(calendar.getTimeZone().getID(), formatData);
                if (zoneIndex == -1) {
                    StringBuffer zoneString = new StringBuffer();
                    value = calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);
                    if (value < 0) {
                        zoneString.append("GMT-");
                        value = -value;
                    } else
                        zoneString.append("GMT+");
                    zoneString.append(zeroPaddingNumber((int) (value / (60 * 60 * 1000)), 2, 2, nf));
                    zoneString.append(':');
                    zoneString.append(zeroPaddingNumber((int) ((value % (60 * 60 * 1000)) / (60 * 1000)), 2, 2, nf));
                    current = zoneString.toString();
                } else if (calendar.get(Calendar.DST_OFFSET) != 0) {
                    if (count >= 4)
                        current = formatData.getZoneStrings()[zoneIndex][3];
                    else
                        current = formatData.getZoneStrings()[zoneIndex][4];
                } else {
                    if (count >= 4)
                        current = formatData.getZoneStrings()[zoneIndex][1];
                    else
                        current = formatData.getZoneStrings()[zoneIndex][2];
                }
                break;
        }
        return current;
    }

    String zeroPaddingNumber(long value, int minDigits, int maxDigits, NumberFormat nf) {
        nf.setMinimumIntegerDigits(minDigits);
        nf.setMaximumIntegerDigits(maxDigits);
        return nf.format(value);
    }

    int getZoneIndex(String ID, DateFormatSymbols dfs) {
        String[][] zoneStrings = dfs.getZoneStrings();
        for (int index = 0; index < zoneStrings.length; index++) {
            if (ID.equalsIgnoreCase(zoneStrings[index][0]))
                return index;
        }
        return -1;
    }

    final int second = 1000;

    final int minute = 60 * second;

    final int hour = 60 * minute;

    final int day = 24 * hour;

    final int month = 30 * day;

    final int year = 365 * day;

    final int someday = 30 * year + 3 * month + 19 * day + 5 * hour;

    public static void main(String[] argv) {
        Locale defaultLocale = Locale.getDefault();
        SDFTCKZoneNamesTest test = new SDFTCKZoneNamesTest();
        try {
            List.of(Locale.ROOT, Locale.CHINA, Locale.forLanguageTag("es-419"), Locale.GERMANY, Locale.forLanguageTag("hi-IN"), Locale.JAPAN, Locale.TAIWAN, Locale.UK, Locale.US, Locale.forLanguageTag("uz-Cyrl-UZ"), Locale.forLanguageTag("zh-SG"), Locale.forLanguageTag("zh-HK"), Locale.forLanguageTag("zh-MO")).stream().forEach(l -> {
                System.out.printf("Testing locale: %s%n", l);
                Locale.setDefault(l);
                test.SimpleDateFormat0062();
            });
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }

    public void SimpleDateFormat0062() {
        boolean passed = true;
        String[] patterns = { "'s0mething'z mm::hh,yyyy zz", "zzzz", "z" };
        SimpleDateFormat sdf = new SimpleDateFormat();
        Date date = new Date(1234567890);
        for (String[] tz : sdf.getDateFormatSymbols().getZoneStrings()) {
            sdf.setTimeZone(TimeZone.getTimeZone(tz[0]));
            for (int i = 0; i < patterns.length && passed; i++) {
                StringBuffer result = new StringBuffer("qwerty");
                FieldPosition fp = new FieldPosition(DateFormat.TIMEZONE_FIELD);
                sdf.applyPattern(patterns[i]);
                String expected = new StringBuffer("qwerty").append(myFormat(date, sdf)).toString();
                String formatted = sdf.format(date, result, fp).toString();
                if (!expected.equals(formatted)) {
                    System.out.println("method format(date, StringBuffer, FieldPosition) formats wrong");
                    System.out.println("  pattern: " + patterns[i]);
                    System.out.println("  time zone ID:   " + tz[0]);
                    System.out.println("  expected result:  " + expected);
                    System.out.println("  formatted result: " + formatted);
                    passed = false;
                }
                if (passed && !expected.equals(result.toString())) {
                    System.out.println("method format(Date date, StringBuffer toAppendTo, FieldPosition fp) toAppendTo is not " + "equal to output");
                    System.out.println("  pattern: " + patterns[i]);
                    System.out.println("  time zone ID:   " + tz[0]);
                    System.out.println("  toAppendTo   : " + result);
                    System.out.println("  formatted date: " + formatted);
                    passed = false;
                }
            }
        }
        if (passed) {
            System.out.println("PASSED : OKAY");
        } else {
            throw new RuntimeException("FAILED");
        }
    }
}
