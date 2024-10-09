import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;
import java.io.*;
import java.net.*;
import java.security.*;
import javax.net.ssl.*;
import com.sun.net.httpserver.*;

public class EchoHandler implements HttpHandler {

    byte[] read(InputStream is) throws IOException {
        byte[] buf = new byte[1024];
        byte[] result = new byte[0];
        while (true) {
            int n = is.read(buf);
            if (n > 0) {
                byte[] b1 = new byte[result.length + n];
                System.arraycopy(result, 0, b1, 0, result.length);
                System.arraycopy(buf, 0, b1, result.length, n);
                result = b1;
            } else if (n == -1) {
                return result;
            }
        }
    }

    public void handle(HttpExchange t) throws IOException {
        InputStream is = t.getRequestBody();
        Headers map = t.getRequestHeaders();
        String fixedrequest = map.getFirst("XFixed");
        String summary = map.getFirst("XSummary");
        if (fixedrequest != null && summary == null) {
            byte[] in = read(is);
            t.sendResponseHeaders(200, in.length);
            OutputStream os = t.getResponseBody();
            os.write(in);
            close(t, os);
            close(t, is);
        } else {
            OutputStream os = t.getResponseBody();
            byte[] buf = new byte[64 * 1024];
            t.sendResponseHeaders(200, 0);
            int n, count = 0;
            ;
            while ((n = is.read(buf)) != -1) {
                if (summary == null) {
                    os.write(buf, 0, n);
                }
                count += n;
            }
            if (summary != null) {
                String s = Integer.toString(count);
                os.write(s.getBytes());
            }
            close(t, os);
            close(t, is);
        }
    }

    protected void close(OutputStream os) throws IOException {
        os.close();
    }

    protected void close(InputStream is) throws IOException {
        is.close();
    }

    protected void close(HttpExchange t, OutputStream os) throws IOException {
        close(os);
    }

    protected void close(HttpExchange t, InputStream is) throws IOException {
        close(is);
    }
}
