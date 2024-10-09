

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

public abstract class MyHttpHandlerB implements HttpHandler {
    
    

    

    static void touch() {

    }

    static public void test(HttpHandler handler) throws IOException {
        
        handler.handle(null);
    }
}

