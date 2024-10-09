



import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.*;
import static java.net.Proxy.NO_PROXY;

public class CookieManagerTest {

    static CookieTransactionHandler httpTrans;
    static HttpServer server;

    static final String hostAddress = getAddr();

    
    static String getAddr() {
        try {
            InetAddress lh = InetAddress.getLocalHost();
            System.out.println("Trying: " + lh);
            if (lh.isReachable(5_000)) {
                System.out.println("Using: " + lh);
                return lh.getHostAddress();
            }
        } catch (IOException x) {
            System.out.println("Debug: caught:" + x);
        }
        System.out.println("Using: \"127.0.0.1\"");
        return "127.0.0.1";
    }

    public static void main(String[] args) throws Exception {
        startHttpServer();
        makeHttpCall();

        if (httpTrans.badRequest) {
            throw new RuntimeException("Test failed : bad cookie header");
        }
        checkCookiePolicy();
    }

   public static void startHttpServer() throws IOException {
        httpTrans = new CookieTransactionHandler();
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/", httpTrans);
        server.start();
    }

    
    private static void checkCookiePolicy() throws Exception {
        CookiePolicy cp = CookiePolicy.ACCEPT_ORIGINAL_SERVER;
        boolean retVal;
        retVal = cp.shouldAccept(null, null);
        checkValue(retVal);
        retVal = cp.shouldAccept(null, new HttpCookie("CookieName", "CookieVal"));
        checkValue(retVal);
        retVal = cp.shouldAccept((new URL("http", "localhost", 2345, "/")).toURI(),
                                  null);
        checkValue(retVal);
    }

    private static void checkValue(boolean val) {
        if (val)
            throw new RuntimeException("Return value is not false!");
    }

    public static void makeHttpCall() throws IOException {
        try {
            int port = server.getAddress().getPort();
            System.out.println("http server listenining on: " + port);

            
            CookieHandler.setDefault(new CookieManager());

            for (int i = 0; i < CookieTransactionHandler.testCount; i++) {
                System.out.println("====== CookieManager test " + (i+1)
                                    + " ======");
                ((CookieManager)CookieHandler.getDefault())
                    .setCookiePolicy(CookieTransactionHandler.testPolicies[i]);
                ((CookieManager)CookieHandler.getDefault())
                    .getCookieStore().removeAll();
                URL url = new URL("http" ,
                                  hostAddress,
                                  server.getAddress().getPort(),
                                  CookieTransactionHandler.testCases[i][0]
                                                          .serverPath);
                System.out.println("Requesting " + url);
                HttpURLConnection uc = (HttpURLConnection)url.openConnection(NO_PROXY);
                uc.getResponseCode();
                uc.disconnect();
            }
        } finally {
            server.stop(0);
        }
    }
}

class CookieTransactionHandler implements HttpHandler {

    private int testcaseDone = 0;
    private int testDone = 0;

    public static boolean badRequest = false;
    
    
    public static final int testCount = 6;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (testDone < testCases[testcaseDone].length) {
            
            
            if (testDone > 0) checkRequest(exchange.getRequestHeaders());
            exchange.getResponseHeaders().add("Location",
                    testCases[testcaseDone][testDone].serverPath);
            exchange.getResponseHeaders()
                    .add(testCases[testcaseDone][testDone].headerToken,
                         testCases[testcaseDone][testDone].cookieToSend);
            exchange.sendResponseHeaders(302, -1);
            testDone++;
        } else {
            
            if (testDone > 0) checkRequest(exchange.getRequestHeaders());
            testcaseDone++;
            testDone = 0;
            exchange.sendResponseHeaders(200, -1);
        }
        exchange.close();
    }

    private void checkRequest(Headers hdrs) {

        assert testDone > 0;
        String cookieHeader = hdrs.getFirst("Cookie");
        if (cookieHeader != null &&
            cookieHeader
                .equalsIgnoreCase(testCases[testcaseDone][testDone-1]
                                  .cookieToRecv))
        {
            System.out.printf("%15s %s\n", "PASSED:", cookieHeader);
        } else {
            System.out.printf("%15s %s\n", "FAILED:", cookieHeader);
            System.out.printf("%15s %s\n\n", "should be:",
                    testCases[testcaseDone][testDone-1].cookieToRecv);
            badRequest = true;
        }
    }

    
    public static class CookieTestCase {
        public String headerToken;
        public String cookieToSend;
        public String cookieToRecv;
        public String serverPath;

        public CookieTestCase(String h, String cts, String ctr, String sp) {
            headerToken = h;
            cookieToSend = cts;
            cookieToRecv = ctr;
            serverPath = sp;
        }
    };

    

    
    public static CookieTestCase[][] testCases = null;
    
    public static CookiePolicy[] testPolicies = null;

    CookieTransactionHandler() {
        testCases = new CookieTestCase[testCount][];
        testPolicies = new CookiePolicy[testCount];

        String localHostAddr = CookieManagerTest.hostAddress;

        int count = 0;

        
        testPolicies[count] = CookiePolicy.ACCEPT_ORIGINAL_SERVER;
        testCases[count++] = new CookieTestCase[]{
                new CookieTestCase("Set-Cookie",
                "CUSTOMER=WILE:BOB; " +
                "path=/; expires=Sat, 09-Nov-2030 23:12:40 GMT;" + "domain=." +
                localHostAddr,
                "CUSTOMER=WILE:BOB",
                "/"
                ),
                new CookieTestCase("Set-Cookie",
                "PART_NUMBER=ROCKET_LAUNCHER_0001; path=/;" + "domain=." + localHostAddr,
                "CUSTOMER=WILE:BOB; PART_NUMBER=ROCKET_LAUNCHER_0001",
                "/"
                ),
                new CookieTestCase("Set-Cookie",
                "SHIPPING=FEDEX; path=/foo;" + "domain=." + localHostAddr,
                "CUSTOMER=WILE:BOB; PART_NUMBER=ROCKET_LAUNCHER_0001",
                "/"
                ),
                new CookieTestCase("Set-Cookie",
                "SHIPPING=FEDEX; path=/foo;" + "domain=." + localHostAddr,
                "CUSTOMER=WILE:BOB; PART_NUMBER=ROCKET_LAUNCHER_0001; SHIPPING=FEDEX",
                "/foo"
                )
                };

        
        testPolicies[count] = CookiePolicy.ACCEPT_ORIGINAL_SERVER;
        testCases[count++] = new CookieTestCase[]{
                new CookieTestCase("Set-Cookie",
                "PART_NUMBER=ROCKET_LAUNCHER_0001; path=/;" + "domain=." + localHostAddr,
                "PART_NUMBER=ROCKET_LAUNCHER_0001",
                "/"
                ),
                new CookieTestCase("Set-Cookie",
                "PART_NUMBER=RIDING_ROCKET_0023; path=/ammo;" + "domain=." + localHostAddr,
                "PART_NUMBER=RIDING_ROCKET_0023; PART_NUMBER=ROCKET_LAUNCHER_0001",
                "/ammo"
                )
                };

        
        testPolicies[count] = CookiePolicy.ACCEPT_ORIGINAL_SERVER;
        testCases[count++] = new CookieTestCase[]{
                new CookieTestCase("Set-Cookie2",
                "Customer=\"WILE_E_COYOTE\"; Version=\"1\"; Path=\"/acme\";" + "domain=." + localHostAddr,
                "$Version=\"1\"; Customer=\"WILE_E_COYOTE\";$Path=\"/acme\";$Domain=\"." + localHostAddr + "\"",
                "/acme/login"
                ),
                new CookieTestCase("Set-Cookie2",
                "Part_Number=\"Rocket_Launcher_0001\"; Version=\"1\";Path=\"/acme\";" + "domain=." + localHostAddr,
                "$Version=\"1\"; Customer=\"WILE_E_COYOTE\";$Path=\"/acme\";" + "$Domain=\"." +
                    localHostAddr  + "\"" + "; Part_Number=\"Rocket_Launcher_0001\";$Path=\"/acme\";"
                    + "$Domain=\"." + localHostAddr +  "\"",
                "/acme/pickitem"
                ),
                new CookieTestCase("Set-Cookie2",
                "Shipping=\"FedEx\"; Version=\"1\"; Path=\"/acme\";" + "domain=." + localHostAddr,
                "$Version=\"1\"; Customer=\"WILE_E_COYOTE\";$Path=\"/acme\";" + "$Domain=\"." + localHostAddr  +
                    "\"" + "; Part_Number=\"Rocket_Launcher_0001\";$Path=\"/acme\";" + "$Domain=\"."
                    + localHostAddr  + "\"" + "; Shipping=\"FedEx\";$Path=\"/acme\";" +
                    "$Domain=\"." + localHostAddr + "\"",
                "/acme/shipping"
                )
                };

        
        testPolicies[count] = CookiePolicy.ACCEPT_ORIGINAL_SERVER;
        testCases[count++] = new CookieTestCase[]{
                new CookieTestCase("Set-Cookie2",
                "Part_Number=\"Rocket_Launcher_0001\"; Version=\"1\"; Path=\"/acme\";" + "domain=." + localHostAddr,
                "$Version=\"1\"; Part_Number=\"Rocket_Launcher_0001\";$Path=\"/acme\";$Domain=\"." + localHostAddr + "\"",
                "/acme/ammo"
                ),
                new CookieTestCase("Set-Cookie2",
                "Part_Number=\"Riding_Rocket_0023\"; Version=\"1\"; Path=\"/acme/ammo\";" + "domain=."
                    + localHostAddr,
                "$Version=\"1\"; Part_Number=\"Riding_Rocket_0023\";$Path=\"/acme/ammo\";$Domain=\"."
                    + localHostAddr  + "\"" + "; Part_Number=\"Rocket_Launcher_0001\";$Path=\"/acme\";"
                    + "$Domain=\"." + localHostAddr + "\"",
                "/acme/ammo"
                ),
                new CookieTestCase("",
                "",
                "$Version=\"1\"; Part_Number=\"Rocket_Launcher_0001\";$Path=\"/acme\";" + "$Domain=\"." + localHostAddr + "\"",
                "/acme/parts"
                )
                };

        
        testPolicies[count] = CookiePolicy.ACCEPT_ORIGINAL_SERVER;
        testCases[count++] = new CookieTestCase[]{
                new CookieTestCase("Set-Cookie2",
                "Part_Number=\"Rocket_Launcher_0001\"; Version=\"1\"; Path=\"/acme\";" + "domain=." + localHostAddr,
                "$Version=\"1\"; Part_Number=\"Rocket_Launcher_0001\";$Path=\"/acme\";$Domain=\"." + localHostAddr + "\"",
                "/acme"
                ),
                new CookieTestCase("Set-Cookie2",
                "Part_Number=\"Rocket_Launcher_2000\"; Version=\"1\"; Path=\"/acme\";" + "domain=." + localHostAddr,
                "$Version=\"1\"; Part_Number=\"Rocket_Launcher_2000\";$Path=\"/acme\";$Domain=\"." + localHostAddr + "\"",
                "/acme"
                )
                };

        
        
        testPolicies[count] = CookiePolicy.ACCEPT_ALL;
        testCases[count++] = new CookieTestCase[]{
                new CookieTestCase("Set-Cookie2",
                "Customer=\"WILE_E_COYOTE\"; Version=\"1\"; Path=\"/acme\"",
                "$Version=\"1\"; Customer=\"WILE_E_COYOTE\";$Path=\"/acme\";$Domain=\""+localHostAddr+"\"",
                "/acme/login"
                ),
                new CookieTestCase("Set-Cookie2",
                "Part_Number=\"Rocket_Launcher_0001\"; Version=\"1\";Path=\"/acme\"",
                "$Version=\"1\"; Customer=\"WILE_E_COYOTE\";$Path=\"/acme\";$Domain=\""+localHostAddr+"\"" +
                    "; Part_Number=\"Rocket_Launcher_0001\";$Path=\"/acme\";$Domain=\""+localHostAddr+"\"",
                "/acme/pickitem"
                ),
                new CookieTestCase("Set-Cookie2",
                "Shipping=\"FedEx\"; Version=\"1\"; Path=\"/acme\"",
                "$Version=\"1\"; Customer=\"WILE_E_COYOTE\";$Path=\"/acme\";$Domain=\""+localHostAddr+"\"" +
                    "; Part_Number=\"Rocket_Launcher_0001\";$Path=\"/acme\";$Domain=\""+localHostAddr+"\"" +
                    "; Shipping=\"FedEx\";$Path=\"/acme\";$Domain=\""+localHostAddr+"\"",
                "/acme/shipping"
                )
                };

        assert count == testCount;
    }
}
