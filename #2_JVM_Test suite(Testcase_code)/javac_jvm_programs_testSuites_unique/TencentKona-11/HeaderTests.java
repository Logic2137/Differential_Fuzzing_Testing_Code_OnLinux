import sun.net.www.HeaderParser;
import sun.net.www.MessageHeader;
import java.io.*;
import java.util.Iterator;

public class HeaderTests {

    static MessageHeader createMessageHeader(String s) {
        ByteArrayInputStream bis = new ByteArrayInputStream(s.getBytes());
        MessageHeader h = new MessageHeader();
        try {
            h.parseHeader(bis);
        } catch (IOException e) {
            throw new RuntimeException("IOException parsing header");
        }
        return h;
    }

    static String s1 = "Foo: bar\r\n" + "Fub: abc\r\n" + "Foo:\r\n" + "Fub: \r\n" + "Foo: param1=one param2=\"two\" param3 param4=\"value=4\"\r\n" + "Fub: xparam1=one xparam2=\"two\" xparam3 xparam4=\"value=4\"\r\n";

    static String s2 = "p1=1 p2=2 p3=3 p4=4 p5=5 p6=\"six\" p7=7 p8=8 p9=9 p10=10 p11=11 p12=12";

    static String s3 = "p1=1, p2=2, p3=3, p4=4, p5=5, p6=\"six\", p7=7, p8=8, p9=9, p10=10, p11=11, p12=12";

    static String[][] s23_expect = { { "p2", "2" }, { "p3", "3" }, { "p4", "4" }, { "p5", "5" }, { "p6", "six" }, { "p7", "7" }, { "p8", "8" }, { "p9", "9" }, { "p10", "10" }, { "p11", "11" }, { "p12", "12" } };

    static String[][] s23_expect1 = { { "p1", "1" }, { "p2", "2" }, { "p3", "3" }, { "p4", "4" }, { "p5", "5" }, { "p6", "six" }, { "p7", "7" }, { "p8", "8" }, { "p9", "9" }, { "p10", "10" }, { "p11", "11" } };

    static String[][] s23_expect2 = { { "p5", "5" }, { "p6", "six" } };

    static String[][][] s1_Foo = { { { "bar", null } }, { { "param1", "one" }, { "param2", "two" }, { "param3", null }, { "param4", "value=4" } } };

    static String[][][] s1_Fub = { { { "abc", null } }, { { "xparam1", "one" }, { "xparam2", "two" }, { "xparam3", null }, { "xparam4", "value=4" } } };

    public static void main(String[] args) {
        MessageHeader head = createMessageHeader(s1);
        Iterator iter = head.multiValueIterator("Foo");
        checkHeader(iter, s1_Foo);
        iter = head.multiValueIterator("Fub");
        checkHeader(iter, s1_Fub);
        HeaderParser hp = new HeaderParser(s2).subsequence(1, 12);
        check(hp, s23_expect);
        hp = new HeaderParser(s3).subsequence(1, 12);
        check(hp, s23_expect);
        hp = new HeaderParser(s3).subsequence(0, 11);
        check(hp, s23_expect1);
        hp = new HeaderParser(s2).subsequence(4, 6);
        check(hp, s23_expect2);
    }

    static void checkHeader(Iterator iter, String[][][] expect) {
        for (int i = 0; iter.hasNext(); ) {
            String s = (String) iter.next();
            HeaderParser p = new HeaderParser(s);
            boolean empty = check(p, expect[i]);
            if (!empty) {
                i++;
            }
        }
    }

    static boolean check(HeaderParser p, String[][] expect) {
        Iterator keys = p.keys();
        Iterator vals = p.values();
        boolean empty = true;
        for (int j = 0; keys.hasNext(); j++) {
            empty = false;
            String key = (String) keys.next();
            String ival = (String) vals.next();
            String val = p.findValue(key);
            if (val == null && ival == null)
                continue;
            if (!val.equals(ival)) {
                throw new RuntimeException("Error " + val + "/" + ival);
            }
            if (!expect[j][0].equals(key)) {
                throw new RuntimeException("Error " + key + "/" + expect[j][0]);
            }
            if (expect[j][1] != null && !expect[j][1].equals(val)) {
                throw new RuntimeException("Error " + val + "/" + expect[j][1]);
            }
        }
        return empty;
    }
}
