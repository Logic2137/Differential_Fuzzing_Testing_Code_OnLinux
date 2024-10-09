

import java.io.IOException;
import java.lang.reflect.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.concurrent.CountDownLatch;




public class RacyHandler {
    static volatile boolean factorySet = false;
    static int NUM_THREADS = 2;
    static CountDownLatch cdl = new CountDownLatch(NUM_THREADS + 1);

    public static void main(String[] args) {
        RacyHandler tester = new RacyHandler();
        tester.runTest();
    }

    public void runTest() {
        new Thread(() -> {
            try {
                cdl.await();
                URL.setURLStreamHandlerFactory(proto -> new CustomHttpHandler());
                factorySet = true;
            } catch (Exception ignore) { }
        }).start();
        cdl.countDown();

        for (int i = 0; i < NUM_THREADS; i++) {
            new Thread(() -> {
                try {
                    cdl.await();
                    while (!factorySet) {
                        
                        getURLStreamHandler();
                    }
                } catch (Exception ignore) { }
            }).start();
            cdl.countDown();
        }

        
        while (!factorySet) { }
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException ie) {
        }

        URLStreamHandler httpHandler = getURLStreamHandler();
        System.out.println("After setting factory URL handlers: http " + httpHandler);
        if (!(httpHandler instanceof CustomHttpHandler))
            throw new RuntimeException("FAILED: Incorrect handler type");
    }

    
    public URLStreamHandler getURLStreamHandler() {
        try {
            Method method = URL.class.getDeclaredMethod("getURLStreamHandler",
                    String.class);
            method.setAccessible(true);
            return (URLStreamHandler) method.invoke(null, "http");
        } catch (Exception e) {
            return null;
        }
    }

    class CustomHttpHandler extends URLStreamHandler {
        @Override
        protected URLConnection openConnection(URL u) throws IOException {
            return null;
        }
    }
}
