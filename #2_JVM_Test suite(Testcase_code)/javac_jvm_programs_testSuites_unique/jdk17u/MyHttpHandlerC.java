import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

public class MyHttpHandlerC implements HttpHandler {

    public MyHttpHandlerC() {
    }

    public void handle(HttpExchange exchange) {
        throw new RuntimeException("MyHttpHandlerB.test() must not be able to invoke this method");
    }

    static public void test(HttpHandler handler) throws IOException {
        handler.handle(null);
    }
}
