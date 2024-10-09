



import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;


public class MessageHeadersTest {

    static final String BODY =
          "This is the body dude,\r\n"
        + "not a header!\r\n";

    static final String MESSAGE_OK =
          "HTTP/1.1 200 OK\r\n"
        + "Content-Length: " + BODY.length() + "\r\n"
        + "MY-Folding-Header: YES\r\n"
        + " OR\r\n"
        + " NO\r\n"
        + "\r\n"
        + BODY;

    static final String MESSAGE_NOK =
          "HTTP/1.1 101 Switching Protocols\r\n"
        + "\r\n";

    static final class ByteBufferInputStream extends InputStream {
        final ByteBuffer buffer;
        int lastRead = -1;
        ByteBufferInputStream(ByteBuffer buffer) {
            this.buffer = buffer;
        }
        @Override
        public int read() throws IOException {
            if (buffer.hasRemaining()) {
                return lastRead = buffer.get();
            }
            return -1;
        }
    }

    public static void main(String[] args) throws IOException {
        testMessageHeaders(MESSAGE_OK);
        testMessageHeaders(MESSAGE_NOK);
    }

    
    static void testMessageHeaders(String msg) throws IOException {
        byte[] bytes = msg.getBytes("US-ASCII");
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        
        String statusLine = readStatusLine(buffer);
        System.out.println("StatusLine: " + statusLine);
        if (!statusLine.startsWith("HTTP/1.1")) {
            throw new AssertionError("bad status line: " + statusLine);
        }

        
        
        
        
        
        ByteBufferInputStream bbis = new ByteBufferInputStream(buffer);
        sun.net.www.MessageHeader mh = new sun.net.www.MessageHeader(bbis);

        
        String contentLengthValue = mh.findValue("Content-length");
        int contentLength = contentLengthValue == null ? 0
            : Integer.parseInt(contentLengthValue);

        
        
        
        if (contentLengthValue == null) {
            
            
            if (!MESSAGE_NOK.equals(msg)) {
                throw new AssertionError("Content-length: header not found");
            }
            
            
            
            
            byte c = buffer.get();
            if (c != '\n' || bbis.lastRead != '\r') {
                throw new AssertionError("Unexpected byte sequence for empty body"
                        + ": " + bbis.lastRead + " " + c + " expected "
                        + (byte)'\r' + " " + (byte)'\n');
            }
        } else {
            if (MESSAGE_NOK.equals(msg)) {
                throw new AssertionError("Content-length: header found in"
                          + " error 101 message");
            }
        }

        
        
        
        String remaining = readRemainingBytes(buffer);
        System.out.println("Body: <<<" + remaining + ">>>");
        if (remaining.length() != contentLength) {
            throw new AssertionError("Unexpected body length: " + remaining.length()
                     + " expected " + contentLengthValue);
        }
        if (contentLengthValue != null) {
            if (!BODY.equals(remaining)) {
                throw new AssertionError("Body does not match!");
            }
        }
    }

    static String readRemainingBytes(ByteBuffer buffer) throws UnsupportedEncodingException {
        byte[] res = new byte[buffer.limit() - buffer.position()];
        System.arraycopy(buffer.array(), buffer.position(), res, 0, res.length);
        buffer.position(buffer.limit());
        return new String(res, "US-ASCII");
    }

    static String readStatusLine(ByteBuffer buffer) throws IOException {
        buffer.mark();
        int p = buffer.position();
        while(buffer.hasRemaining()) {
            char c = (char)buffer.get();
            if (c == '\r') {
                c = (char)buffer.get();
                if (c == '\n') {
                    byte[] res = new byte[buffer.position() - p -2];
                    System.arraycopy(buffer.array(), p, res, 0, res.length);
                    return new String(res, "US-ASCII");
                }
            }
        }
        throw new IOException("Status line not found");
    }
}
