






import javax.net.ssl.*;


public class CustomizedCipherSuites {

    private static String contextProtocol;
    private static boolean isClientMode;

    private static String enabledCipherSuite;
    private static String disabledCipherSuite;

    public static void main(String[] args) throws Exception {

        contextProtocol = trimQuotes(args[0]);
        isClientMode = Boolean.parseBoolean(args[1]);
        enabledCipherSuite = trimQuotes(args[2]);
        disabledCipherSuite = trimQuotes(args[3]);

        
        
        
        SSLContext context = SSLContext.getInstance(contextProtocol);

        
        if (!contextProtocol.equals("Default")) {
            
            context.init((KeyManager[])null, (TrustManager[])null, null);
        }

        
        if (isClientMode) {
            
            
            
            SSLParameters parameters = context.getDefaultSSLParameters();
            System.out.println("Checking SSLContext default parameters ...");
            checkEnabledCiphers(parameters.getCipherSuites());
        }

        
        
        
        SSLParameters parameters = context.getSupportedSSLParameters();
        System.out.println("Checking SSLContext suppport parameters ...");
        checkSupportedCiphers(parameters.getCipherSuites());


        
        
        
        SSLEngine engine = context.createSSLEngine();
        engine.setUseClientMode(isClientMode);

        System.out.println("Checking SSLEngine default cipher suites ...");
        checkEnabledCiphers(engine.getEnabledCipherSuites());

        
        
        
        System.out.println("Checking SSLEngine supported cipher suites ...");
        checkSupportedCiphers(engine.getSupportedCipherSuites());

        if (isClientMode) {
            SSLSocketFactory factory = context.getSocketFactory();
            
            try (SSLSocket socket = (SSLSocket)factory.createSocket()) {
                
                
                
                System.out.println(
                        "Checking SSLSocket default cipher suites ...");
                checkEnabledCiphers(socket.getEnabledCipherSuites());

                
                
                
                System.out.println(
                        "Checking SSLSocket supported cipher suites ...");
                checkSupportedCiphers(socket.getSupportedCipherSuites());
            }
        } else {
            SSLServerSocketFactory factory = context.getServerSocketFactory();
            
            try (SSLServerSocket socket =
                    (SSLServerSocket)factory.createServerSocket()) {
                
                
                
                System.out.println(
                        "Checking SSLServerSocket default cipher suites ...");
                checkEnabledCiphers(socket.getEnabledCipherSuites());

                
                
                
                System.out.println(
                        "Checking SSLServerSocket supported cipher suites ...");
                checkSupportedCiphers(socket.getSupportedCipherSuites());
            }
        }

        System.out.println("\t... Success");
    }

    private static void checkEnabledCiphers(
            String[] ciphers) throws Exception {

        if (ciphers.length == 0) {
            throw new Exception("No default cipher suites");
        }

        boolean isMatch = false;
        if (enabledCipherSuite.isEmpty()) {
            
            isMatch = true;
        }

        boolean isBroken = false;
        for (String cipher : ciphers) {
            System.out.println("\tdefault cipher suite " + cipher);
            if (!enabledCipherSuite.isEmpty() &&
                        cipher.equals(enabledCipherSuite)) {
                isMatch = true;
            }

            if (!disabledCipherSuite.isEmpty() &&
                        cipher.equals(disabledCipherSuite)) {
                isBroken = true;
            }
        }

        if (!isMatch) {
            throw new Exception(
                "Cipher suite " + enabledCipherSuite + " should be enabled");
        }

        if (isBroken) {
            throw new Exception(
                "Cipher suite " + disabledCipherSuite + " should be disabled");
        }
    }

    private static void checkSupportedCiphers(
            String[] ciphers) throws Exception {

        if (ciphers.length == 0) {
            throw new Exception("No supported cipher suites");
        }

        boolean hasEnabledCipherSuite = enabledCipherSuite.isEmpty();
        boolean hasDisabledCipherSuite = disabledCipherSuite.isEmpty();
        for (String cipher : ciphers) {
            System.out.println("\tsupported cipher suite " + cipher);
            if (!enabledCipherSuite.isEmpty() &&
                        cipher.equals(enabledCipherSuite)) {
                hasEnabledCipherSuite = true;
            }

            if (!disabledCipherSuite.isEmpty() &&
                        cipher.equals(disabledCipherSuite)) {
                hasDisabledCipherSuite = true;
            }
        }

        if (!hasEnabledCipherSuite) {
            throw new Exception(
                "Cipher suite " + enabledCipherSuite + " should be supported");
        }

        if (!hasDisabledCipherSuite) {
            throw new Exception(
                "Cipher suite " + disabledCipherSuite + " should be supported");
        }
    }

    private static String trimQuotes(String candidate) {
        if (candidate != null && candidate.length() != 0) {
            
            if (candidate.length() > 1 && candidate.charAt(0) == '"' &&
                    candidate.charAt(candidate.length() - 1) == '"') {
                return candidate.substring(1, candidate.length() - 1);
            }
        }

        return candidate;
    }
}
