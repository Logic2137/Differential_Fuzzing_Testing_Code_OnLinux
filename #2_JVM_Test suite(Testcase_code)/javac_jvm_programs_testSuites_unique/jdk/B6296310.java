



import java.io.IOException;
import java.io.OutputStream;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ResponseCache;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;



public class B6296310
{
   static SimpleHttpTransaction httpTrans;
   static HttpServer server;

   public static void main(String[] args) throws Exception
   {
      ResponseCache.setDefault(new MyCacheHandler());
      startHttpServer();
      makeHttpCall();
   }

   public static void startHttpServer() throws IOException {
     httpTrans = new SimpleHttpTransaction();
     InetAddress loopback = InetAddress.getLoopbackAddress();
     server = HttpServer.create(new InetSocketAddress(loopback, 0), 10);
     server.createContext("/", httpTrans);
     server.setExecutor(Executors.newSingleThreadExecutor());
     server.start();
   }

   public static void makeHttpCall() throws IOException {
      try {
         System.out.println("http server listen on: " + server.getAddress().getPort());
         URL url = new URL("http" , InetAddress.getLoopbackAddress().getHostAddress(),
                            server.getAddress().getPort(), "/");
         HttpURLConnection uc = (HttpURLConnection)url.openConnection(Proxy.NO_PROXY);
         System.out.println(uc.getResponseCode());
      } finally {
         server.stop(1);
      }
   }
}

class SimpleHttpTransaction implements HttpHandler
{
   
   @Override
   public void handle(HttpExchange trans) {
      try {
         trans.sendResponseHeaders(200, 0);
         trans.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}

class MyCacheHandler extends ResponseCache
{
   public CacheResponse get(URI uri, String rqstMethod, Map rqstHeaders)
   {
      return null;
   }

   public CacheRequest put(URI uri, URLConnection conn)
   {
      return new MyCacheRequest();
   }
}

class MyCacheRequest extends CacheRequest
{
   public void abort() {}

   public OutputStream getBody() throws IOException {
       return null;
   }
}
