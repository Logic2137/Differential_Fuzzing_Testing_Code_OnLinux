



import java.io.*;
import java.security.*;
import java.security.KeyStore.*;
import java.security.cert.*;
import javax.crypto.*;
import javax.security.auth.callback.*;

public class ProbeKeystores {
    private static final char[] PASSWORD = "changeit".toCharArray();
    private static final char[] BAD_PASSWORD = "badpasword".toCharArray();
    private static final LoadStoreParameter LOAD_STORE_PARAM =
        new MyLoadStoreParameter(new PasswordProtection(PASSWORD));
    private static final LoadStoreParameter BAD_LOAD_STORE_PARAM =
        new MyLoadStoreParameter(new PasswordProtection(BAD_PASSWORD));
    private static final String DIR = System.getProperty("test.src", ".");
    private static final String CERT_FILE = "trusted.pem";

    private static class MyLoadStoreParameter implements LoadStoreParameter {

        private ProtectionParameter protection;

        MyLoadStoreParameter(ProtectionParameter protection) {
            this.protection = protection;
        }

        public ProtectionParameter getProtectionParameter() {
            return protection;
        }
    }

    public static final void main(String[] args) throws Exception {

        

        init("empty.jks", "JKS");
        init("empty.jceks", "JCEKS");
        init("empty.p12", "PKCS12");

        load("empty.jks", "JKS");
        load("empty.jceks", "JCEKS");
        load("empty.p12", "PKCS12");
        load("empty.jks", "PKCS12"); 
        load("empty.p12", "JKS"); 
        load("empty.jks", "PKCS12", true); 
        load("empty.jks", "JKS", false); 
        load("empty.p12", "JKS", true); 
        load("empty.p12", "PKCS12", false); 

        probe("empty.jks", "JKS");
        probe("empty.jceks", "JCEKS");
        probe("empty.p12", "PKCS12");

        build("empty.jks", "JKS", true);
        build("empty.jks", "JKS", false);
        build("empty.jceks", "JCEKS", true);
        build("empty.jceks", "JCEKS", false);
        build("empty.p12", "PKCS12", true);
        build("empty.p12", "PKCS12", false);

        

        X509Certificate cert = loadCertificate(CERT_FILE);
        init("onecert.jks", "JKS", cert);
        init("onecert.jceks", "JCEKS", cert);
        init("onecert.p12", "PKCS12", cert);

        load("onecert.jks", "JKS");
        load("onecert.jceks", "JCEKS");
        load("onecert.p12", "PKCS12");
        load("onecert.jks", "PKCS12"); 
        load("onecert.p12", "JKS"); 
        load("onecert.jks", "PKCS12", true); 
        load("onecert.jks", "JKS", false); 
        load("onecert.p12", "JKS", true); 
        load("onecert.p12", "PKCS12", false); 

        probe("onecert.jks", "JKS");
        probe("onecert.jceks", "JCEKS");
        probe("onecert.p12", "PKCS12");

        build("onecert.jks", "JKS", true);
        build("onecert.jks", "JKS", false);
        build("onecert.jceks", "JCEKS", true);
        build("onecert.jceks", "JCEKS", false);
        build("onecert.p12", "PKCS12", true);
        build("onecert.p12", "PKCS12", false);

        

        SecretKey key = generateSecretKey("AES", 128);
        init("onekey.jceks", "JCEKS", key);
        init("onekey.p12", "PKCS12", key);

        load("onekey.jceks", "JCEKS");
        load("onekey.p12", "PKCS12");
        load("onekey.p12", "JKS"); 
        load("onekey.p12", "JKS", true); 
        load("onekey.p12", "PKCS12", false); 

        probe("onekey.jceks", "JCEKS");
        probe("onekey.p12", "PKCS12");

        build("onekey.jceks", "JCEKS", true);
        build("onekey.jceks", "JCEKS", false);
        build("onekey.p12", "PKCS12", true);
        build("onekey.p12", "PKCS12", false);

        System.out.println("OK.");
    }

    
    private static void init(String file, String type) throws Exception {
        KeyStore ks = KeyStore.getInstance(type);
        ks.load(null, null);
        try (OutputStream stream = new FileOutputStream(file)) {
            ks.store(stream, PASSWORD);
        }
        System.out.println("Created a " + type + " keystore named '" + file + "'");
    }

    
    private static void init(String file, String type, X509Certificate cert)
        throws Exception {
        KeyStore ks = KeyStore.getInstance(type);
        ks.load(null, null);
        ks.setEntry("mycert", new KeyStore.TrustedCertificateEntry(cert), null);
        try (OutputStream stream = new FileOutputStream(file)) {
            ks.store(stream, PASSWORD);
        }
        System.out.println("Created a " + type + " keystore named '" + file + "'");
    }

    
    private static void init(String file, String type, SecretKey key)
        throws Exception {
        KeyStore ks = KeyStore.getInstance(type);
        ks.load(null, null);
        ks.setEntry("mykey", new KeyStore.SecretKeyEntry(key),
            new PasswordProtection(PASSWORD));
        try (OutputStream stream = new FileOutputStream(file)) {
            ks.store(stream, PASSWORD);
        }
        System.out.println("Created a " + type + " keystore named '" + file + "'");
    }

    
    private static void probe(String file, String type) throws Exception {
        
        KeyStore ks = KeyStore.getInstance(new File(file), PASSWORD);
        if (!type.equalsIgnoreCase(ks.getType())) {
            throw new Exception("ERROR: expected a " + type + " keystore, " +
                "got a " + ks.getType() + " keystore instead");
        } else {
            System.out.println("Probed a " + type + " keystore named '" + file
                    + "' with " + ks.size() + " entries");
        }

        
        try {
            ks = KeyStore.getInstance(new File(file), BAD_PASSWORD);
            throw new Exception("ERROR: expected an exception but got success");
        } catch (IOException e) {
            System.out.println("Failed to load a " + type + " keystore named '" + file + "' (as expected)");
        }

        
        ks = KeyStore.getInstance(new File(file), LOAD_STORE_PARAM);
        if (!type.equalsIgnoreCase(ks.getType())) {
            throw new Exception("ERROR: expected a " + type + " keystore, " +
                "got a " + ks.getType() + " keystore instead");
        } else {
            System.out.println("Probed a " + type + " keystore named '" + file
                    + "' with " + ks.size() + " entries");
        }

        
        try {
            ks = KeyStore.getInstance(new File(file), BAD_LOAD_STORE_PARAM);
            throw new Exception("ERROR: expected an exception but got success");
        } catch (IOException e) {
            System.out.println("Failed to load a " + type + " keystore named '" + file + "' (as expected)");
        }
    }

    
    private static void build(String file, String type, boolean usePassword)
        throws Exception {

        Builder builder;
        if (usePassword) {
            builder = Builder.newInstance(new File(file),
                new PasswordProtection(PASSWORD));
        } else {
            builder = Builder.newInstance(new File(file),
                new CallbackHandlerProtection(new DummyHandler()));
        }
        KeyStore ks = builder.getKeyStore();
        if (!type.equalsIgnoreCase(ks.getType())) {
            throw new Exception("ERROR: expected a " + type + " keystore, " +
                "got a " + ks.getType() + " keystore instead");
        } else {
            System.out.println("Built a " + type + " keystore named '" + file + "'");
        }
    }

    
    private static void load(String file, String type) throws Exception {
        KeyStore ks = KeyStore.getInstance(type);
        try (InputStream stream = new FileInputStream(file)) {
            ks.load(stream, PASSWORD);
        }
        if (!type.equalsIgnoreCase(ks.getType())) {
            throw new Exception("ERROR: expected a " + type + " keystore, " +
                "got a " + ks.getType() + " keystore instead");
        } else {
            System.out.println("Loaded a " + type + " keystore named '" + file + "'");
        }
    }

    
    private static void load(String file, String type, boolean expectFailure)
        throws Exception {
        Security.setProperty("keystore.type.compat", "false");
        try {
            load(file, type);
            if (expectFailure) {
                throw new Exception("ERROR: expected load to fail but it didn't");
            }
        } catch (IOException e) {
            if (expectFailure) {
                System.out.println("Failed to load a " + type + " keystore named '" + file + "' (as expected)");
            } else {
                throw e;
            }
        } finally {
            Security.setProperty("keystore.type.compat", "true");
        }
    }

    
    private static X509Certificate loadCertificate(String certFile)
        throws Exception {
        X509Certificate cert = null;
        try (FileInputStream certStream =
            new FileInputStream(DIR + "/" + certFile)) {
            CertificateFactory factory =
                CertificateFactory.getInstance("X.509");
            return (X509Certificate) factory.generateCertificate(certStream);
        }
    }

    
    private static SecretKey generateSecretKey(String algorithm, int size)
        throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance(algorithm);
        generator.init(size);
        return generator.generateKey();
    }

    private static class DummyHandler implements CallbackHandler {
        public void handle(Callback[] callbacks)
            throws IOException, UnsupportedCallbackException {
            System.out.println("** Callbackhandler invoked");
            for (int i = 0; i < callbacks.length; i++) {
                Callback cb = callbacks[i];
                if (cb instanceof PasswordCallback) {
                    PasswordCallback pcb = (PasswordCallback)cb;
                    pcb.setPassword(PASSWORD);
                    break;
                }
            }
        }
    }
}
