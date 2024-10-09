

import java.io.IOException;

import javax.net.ssl.SSLSocket;


public interface ConnectionInterceptor {

    
    public void beforeExit(SSLSocket socket) throws IOException;
}
