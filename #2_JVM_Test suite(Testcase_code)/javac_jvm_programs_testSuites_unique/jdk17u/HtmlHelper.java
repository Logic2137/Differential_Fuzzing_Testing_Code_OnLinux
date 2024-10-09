public class HtmlHelper {

    private static final String STYLE = "style=\"font-family: Courier New; " + "font-size: 12px; " + "white-space: pre-wrap\"";

    public static String htmlRow(String... values) {
        StringBuilder row = new StringBuilder();
        row.append(startTr());
        for (String value : values) {
            row.append(startTd());
            row.append(value);
            row.append(endTd());
        }
        row.append(endTr());
        return row.toString();
    }

    public static String startHtml() {
        return startTag("html");
    }

    public static String endHtml() {
        return endTag("html");
    }

    public static String startPre() {
        return startTag("pre " + STYLE);
    }

    public static String endPre() {
        return endTag("pre");
    }

    public static String startTable() {
        return startTag("table border=\"1\" padding=\"1\" cellspacing=\"0\" " + STYLE);
    }

    public static String endTable() {
        return endTag("table");
    }

    public static String startTr() {
        return "\t" + startTag("tr") + "\n";
    }

    public static String endTr() {
        return "\t" + endTag("tr") + "\n";
    }

    public static String startTd() {
        return "\t\t" + startTag("td");
    }

    public static String endTd() {
        return endTag("td") + "\n";
    }

    public static String startTag(String tag) {
        return "<" + tag + ">";
    }

    public static String endTag(String tag) {
        return "</" + tag + ">";
    }

    public static String anchorName(String name, String text) {
        return "<a name=" + name + "><hr/>" + text + "</a>";
    }

    public static String anchorLink(String file, String anchorName, String text) {
        return "<a href=" + file + "#" + anchorName + ">" + text + "</a>";
    }
}
