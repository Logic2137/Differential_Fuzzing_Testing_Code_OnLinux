

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class EchoHandler implements HttpHandler {
    public void handle (HttpExchange t)
        throws IOException
    {
        InputStream is = t.getRequestBody();
        Headers map = t.getRequestHeaders();
        String fixedrequest = map.getFirst ("XFixed");

        
        String summary = map.getFirst ("XSummary");
        OutputStream os = t.getResponseBody();
        byte[] in;
        in = is.readAllBytes();
        if (summary != null) {
            in = Integer.toString(in.length).getBytes(StandardCharsets.UTF_8);
        }
        if (fixedrequest != null) {
            t.sendResponseHeaders(200, in.length == 0 ? -1 : in.length);
        } else {
            t.sendResponseHeaders(200, 0);
        }
        os.write(in);
        close(t, os);
        close(t, is);
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
