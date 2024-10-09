import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class Regex {

    public static void main(String[] args) {
        testIntersect();
    }

    static void testIntersect() {
        try {
            new RegularExpression("(?[b-d]&[a-r])", "X");
            throw new RuntimeException("Xerces XPath Regex: " + "intersection not allowed in XML schema mode, " + "exception expected above.");
        } catch (ParseException e) {
        }
        RegularExpression ce = new RegularExpression("(?[a-e]&[c-r])");
        if (!(ce.matches("c") && ce.matches("d") && ce.matches("e"))) {
            throw new RuntimeException("Xerces XPath Regex Error: " + "[c-e] expected to match c,d,e.");
        }
        if (ce.matches("b") || ce.matches("f")) {
            throw new RuntimeException("Xerces XPath Regex Error: " + "[c-e] not expected to match b or f.");
        }
        RegularExpression bd = new RegularExpression("(?[b-d]&[a-r])");
        if (!(bd.matches("b") && bd.matches("c") && bd.matches("d"))) {
            throw new RuntimeException("Xerces XPath Regex Error: " + "[b-d] expected to match b,c,d.");
        }
        if (bd.matches("e") || bd.matches("a")) {
            throw new RuntimeException("Xerces XPath Regex Error: " + "[b-d] not expected to match a or e.");
        }
        RegularExpression bd2 = new RegularExpression("(?[a-r]&[b-d])");
        if (!(bd.matches("b") && bd.matches("c") && bd.matches("d"))) {
            throw new RuntimeException("Xerces XPath Regex Error: " + "[b-d] expected to match b,c,d, test 2.");
        }
        if (bd2.matches("e") || bd2.matches("a")) {
            throw new RuntimeException("Xerces XPath Regex Error: " + "[b-d] not expected to match a or e, test 2.");
        }
        RegularExpression dh = new RegularExpression("(?[d-z]&[a-h])");
        if (!(dh.matches("d") && dh.matches("e") && dh.matches("h"))) {
            throw new RuntimeException("Xerces XPath Regex Error: " + "[d-h] expected to match d,e,h.");
        }
        if (dh.matches("c") || bd2.matches("i")) {
            throw new RuntimeException("Xerces XPath Regex Error: " + "[d-h] not expected to match c or i.");
        }
        RegularExpression dfhk = new RegularExpression("(?[b-r]&[d-fh-k])");
        if (!(dfhk.matches("d") && dfhk.matches("f") && dfhk.matches("h") && dfhk.matches("k"))) {
            throw new RuntimeException("Xerces XPath Regex Error: " + "[d-fh-k] expected to match d,f,h,k.");
        }
        if (dfhk.matches("c") || dfhk.matches("g") || dfhk.matches("l")) {
            throw new RuntimeException("Xerces XPath Regex Error: " + "[d-fh-k] not expected to match c,g,l.");
        }
        RegularExpression cfhk = new RegularExpression("(?[c-r]&[b-fh-k])");
        if (!(cfhk.matches("c") && cfhk.matches("f") && cfhk.matches("h") && cfhk.matches("k"))) {
            throw new RuntimeException("Xerces XPath Regex Error: " + "[c-fh-k] expected to match c,f,h,k.");
        }
        if (cfhk.matches("b") || cfhk.matches("g") || cfhk.matches("l")) {
            throw new RuntimeException("Xerces XPath Regex Error: " + "[c-fh-k] not expected to match b,g,l.");
        }
        RegularExpression ekor = new RegularExpression("(?[a-r]&[e-z]&[c-ko-s])");
        if (!(ekor.matches("e") && ekor.matches("k") && ekor.matches("o") && ekor.matches("r"))) {
            throw new RuntimeException("Xerces XPath Regex Error: " + "[e-ko-r] expected to match e,k,o,r.");
        }
        if (ekor.matches("d") || ekor.matches("l") || ekor.matches("s")) {
            throw new RuntimeException("Xerces XPath Regex Error: " + "[e-ko-r] not expected to match d,l,s.");
        }
    }
}
