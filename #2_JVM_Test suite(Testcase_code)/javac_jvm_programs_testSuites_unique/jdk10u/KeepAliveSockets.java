



import java.lang.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.util.*;
import com.sun.corba.se.impl.orb.*;

import com.sun.corba.se.impl.transport.*;

public class KeepAliveSockets {

    public static void main(String[] args) throws Exception {

        boolean keepAlive = false;
        String prop = System.getProperty("com.sun.CORBA.transport.enableTcpKeepAlive");
        if (prop != null)
            keepAlive = !"false".equalsIgnoreCase(prop);

        DefaultSocketFactoryImpl sfImpl = new DefaultSocketFactoryImpl();
        ORBImpl orb = new ORBImpl();
        orb.set_parameters(null);
        sfImpl.setORB(orb);

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(0));

        InetSocketAddress isa = new InetSocketAddress("localhost", ssc.socket().getLocalPort());
        Socket s = sfImpl.createSocket("ignore", isa);
        System.out.println("Received factory socket" + s);
        if (keepAlive != s.getKeepAlive())
            throw new RuntimeException("KeepAlive value not honoured in CORBA socket");
    }

}
