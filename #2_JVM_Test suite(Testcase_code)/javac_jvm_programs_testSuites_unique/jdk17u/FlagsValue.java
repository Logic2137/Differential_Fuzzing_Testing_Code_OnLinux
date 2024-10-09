
package gc.arguments;

import java.util.regex.*;

public class FlagsValue {

    public static boolean getFlagBoolValue(String flag, String where) {
        Matcher m = Pattern.compile(flag + "\\s+:?= (true|false)").matcher(where);
        if (!m.find()) {
            throw new RuntimeException("Could not find value for flag " + flag + " in output string");
        }
        return m.group(1).equals("true");
    }

    public static long getFlagLongValue(String flag, String where) {
        Matcher m = Pattern.compile(flag + "\\s+:?=\\s+\\d+").matcher(where);
        if (!m.find()) {
            throw new RuntimeException("Could not find value for flag " + flag + " in output string");
        }
        String match = m.group();
        return Long.parseLong(match.substring(match.lastIndexOf(" ") + 1, match.length()));
    }
}
