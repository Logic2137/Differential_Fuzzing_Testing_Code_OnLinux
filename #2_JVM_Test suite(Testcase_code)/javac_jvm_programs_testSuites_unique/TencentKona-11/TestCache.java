

import java.net.*;
import java.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.File;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.ResponseCache;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.jar.JarInputStream;
import java.util.jar.JarFile;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedActionException;
import java.security.Principal;
import java.security.cert.Certificate;
import javax.net.ssl.SSLPeerUnverifiedException;

public class TestCache extends java.net.ResponseCache {
    private boolean inCacheHandler = false;
    private boolean _downloading = false;

    public static volatile boolean fail = false;

    public static void reset() {
        
        System.out.println("install deploy cache handler");
        ResponseCache.setDefault(new TestCache());
    }

    public synchronized CacheResponse get(final URI uri, String rqstMethod,
            Map requestHeaders) throws IOException {
        System.out.println("get: " + uri);
        Thread.currentThread().dumpStack();
        return null;
    }

    public synchronized CacheRequest put(URI uri, URLConnection conn)
    throws IOException {
        System.out.println("put: " + uri);
        Thread.currentThread().dumpStack();
        URL url = uri.toURL();
        return new DeployCacheRequest(url, conn);

    }
}

class DeployByteArrayOutputStream extends java.io.ByteArrayOutputStream {

    private URL _url;
    private URLConnection _conn;

    DeployByteArrayOutputStream(URL url, URLConnection conn) {
        _url = url;
        _conn = conn;
    }


    public void close() throws IOException {

        System.out.println("contentLength: " + _conn.getContentLength());
        System.out.println("byte array size: " + size());
        if ( _conn.getContentLength() == size()) {
            System.out.println("correct content length");
        } else {
            System.out.println("wrong content length");
            System.out.println("TEST FAILED");
            TestCache.fail = true;
        }
        super.close();
    }
}

class DeployCacheRequest extends java.net.CacheRequest {

    private URL _url;
    private URLConnection _conn;
    private boolean _downloading = false;

    DeployCacheRequest(URL url, URLConnection conn) {
        System.out.println("DeployCacheRequest ctor for: " + url);
        _url = url;
        _conn = conn;
    }

    public void abort() {
        System.out.println("abort called");
    }

    public OutputStream getBody() throws IOException {
        System.out.println("getBody called");
        return new DeployByteArrayOutputStream(_url, _conn);
    }
}
