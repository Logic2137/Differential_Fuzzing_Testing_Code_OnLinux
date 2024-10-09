


import java.io.*;
import sun.net.www.MessageHeader;

public class MessageHeaderTest {
    public static void main (String[] args) throws Exception {
        for (int i=0; i<7; i++) {
            ByteArrayInputStream bis = new ByteArrayInputStream(headers[i].getBytes());
            MessageHeader h = new MessageHeader(bis);
            String before = h.toString();
            before = before.substring(before.indexOf('{'));
            boolean result = h.filterNTLMResponses("WWW-Authenticate");
            String after = h.toString();
            after = after.substring(after.indexOf('{'));
            if (!expected[i].equals(after)) {
                throw new RuntimeException(Integer.toString(i) + " expected != after");
            }
            if (result != expectedResult[i]) {
                throw new RuntimeException(Integer.toString(i) + " result != expectedResult");
            }
        }
    }

    static String expected[] = {
        "{null: HTTP/1.1 200 Ok}{Foo: bar}{Bar: foo}{WWW-Authenticate: NTLM sdsds}",
        "{null: HTTP/1.1 200 Ok}{Foo: bar}{Bar: foo}{WWW-Authenticate: }",
        "{null: HTTP/1.1 200 Ok}{Foo: bar}{Bar: foo}{WWW-Authenticate: NTLM sdsds}",
        "{null: HTTP/1.1 200 Ok}{Foo: bar}{Bar: foo}{WWW-Authenticate: NTLM sdsds}",
        "{null: HTTP/1.1 200 Ok}{Foo: bar}{Bar: foo}{WWW-Authenticate: NTLM sdsds}{Bar: foo}",
        "{null: HTTP/1.1 200 Ok}{WWW-Authenticate: Negotiate}{Foo: bar}{Bar: foo}{WWW-Authenticate: NTLM}{Bar: foo}{WWW-Authenticate: Kerberos}",
        "{null: HTTP/1.1 200 Ok}{Foo: foo}{Bar: }{WWW-Authenticate: NTLM blob}{Bar: foo blob}"
    };

    static boolean[] expectedResult = {
        false, false, true, true, true, false, false
    };

    static String[] headers = {
        "HTTP/1.1 200 Ok\r\nFoo: bar\r\nBar: foo\r\nWWW-Authenticate: NTLM sdsds",
        "HTTP/1.1 200 Ok\r\nFoo: bar\r\nBar: foo\r\nWWW-Authenticate:",
        "HTTP/1.1 200 Ok\r\nFoo: bar\r\nBar: foo\r\nWWW-Authenticate: NTLM sdsds\r\nWWW-Authenticate: Negotiate",
        "HTTP/1.1 200 Ok\r\nFoo: bar\r\nBar: foo\r\nWWW-Authenticate: NTLM sdsds\r\nWWW-Authenticate: Negotiate\r\nWWW-Authenticate: Kerberos",
        "HTTP/1.1 200 Ok\r\nWWW-Authenticate: Negotiate\r\nFoo: bar\r\nBar: foo\r\nWWW-Authenticate: NTLM sdsds\r\nBar: foo\r\nWWW-Authenticate: Kerberos",
        "HTTP/1.1 200 Ok\r\nWWW-Authenticate: Negotiate\r\nFoo: bar\r\nBar: foo\r\nWWW-Authenticate: NTLM\r\nBar: foo\r\nWWW-Authenticate: Kerberos",
        "HTTP/1.1 200 Ok\r\nFoo: foo\r\nBar:\r\nWWW-Authenticate: NTLM blob\r\nBar: foo blob"
    };
}
