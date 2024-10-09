



import java.io.*;
import java.util.*;
import java.net.*;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.Permission;
import java.security.KeyStore;
import java.security.cert.*;
import sun.security.provider.*;

public class TokenStore {

    private static String DIR =
                System.getProperty("test.classes", ".") + File.separatorChar;
    private static final char[] storePassword = new char[]
                { 'T', 'o', 'k', 'e', 'n', 'S', 't', 'o', 'r', 'e' };


    
    private static String NO_STORE_FILE =       DIR + "TokenStore.NoStore";
    private static String URL_FILE =            DIR + "TokenStore.Url";
    private static String URL_T_FILE =          DIR + "TokenStore.UrlT";
    private static String URL_T_P_FILE =        DIR + "TokenStore.UrlTP";
    private static String URL_PWD_FILE =        DIR + "TokenStore.UrlPwd";
    private static String URL_T_P_PWD_FILE =    DIR + "TokenStore.UrlTPPwd";
    private static String BADPASS_FILE =        DIR + "TokenStore.BadPass";

    private static String RELPASS_FILE =
                System.getProperty("test.src", ".") + File.separatorChar +
                "TokenStore.RelPassPolicy";

    
    private static ProtectionDomain NO_STORE_DOMAIN;
    private static ProtectionDomain URL_DOMAIN;
    private static ProtectionDomain URL_T_DOMAIN;
    private static ProtectionDomain URL_T_P_DOMAIN;

    
    private static final String POLICY_NO_STORE =
        "grant { permission java.security.AllPermission; };";

    private static final String POLICY_URL =
        "keystore \"file:${test.src}${/}TokenStore.keystore\";"         +
        "grant signedby \"POLICY_URL\" {"                               +
        "    permission java.security.AllPermission;"                   +
        "};"                                                            ;

    private static final String POLICY_URL_T =
        "keystore \"file:${test.src}${/}TokenStore.keystore\", \"JKS\";"+
        "grant signedby \"POLICY_URL_T\" {"                             +
        "    permission java.security.AllPermission;"                   +
        "};"                                                            ;

    private static final String POLICY_URL_T_P =
        "keystore \"file:${test.src}${/}TokenStore.keystore\","         +
        "               \"JKS\", \"SUN\";"                              +
        "grant signedby \"POLICY_URL_T_P\" {"                           +
        "    permission java.security.AllPermission;"                   +
        "};"                                                            ;

    private static final String POLICY_URL_PWD =
        "keystore \"file:${test.src}${/}TokenStore.keystore\";"         +
        "keystorePasswordURL \"file:${test.src}${/}TokenStore.pwd\";"   +
        "grant signedby \"POLICY_URL\" {"                               +
        "    permission java.security.AllPermission;"                   +
        "};"                                                            ;

    private static final String POLICY_URL_T_P_PWD =
        "keystore \"file:${test.src}${/}TokenStore.keystore\","         +
        "               \"JKS\", \"SUN\";"                              +
        "keystorePasswordURL \"file:${test.src}${/}TokenStore.pwd\";"   +
        "grant signedby \"POLICY_URL_T_P\" {"                           +
        "    permission java.security.AllPermission;"                   +
        "};"                                                            ;

    private static final String POLICY_BADPASS =
        "keystore \"file:${test.src}${/}TokenStore.keystore\","         +
        "               \"JKS\", \"SUN\";"                              +
        "keystorePasswordURL \"file:${test.src}${/}TokenStore.java\";"  +
        "grant signedby \"POLICY_URL_T_P\" {"                           +
        "    permission java.security.AllPermission;"                   +
        "};"                                                            ;

    private static void init() throws Exception {

        

        PolicyParser pp = new PolicyParser();
        pp.read(new StringReader(POLICY_NO_STORE));
        pp.write(new FileWriter(NO_STORE_FILE, false));

        pp = new PolicyParser();
        pp.read(new StringReader(POLICY_URL));
        pp.write(new FileWriter(URL_FILE, false));

        pp = new PolicyParser();
        pp.read(new StringReader(POLICY_URL_T));
        pp.write(new FileWriter(URL_T_FILE, false));

        pp = new PolicyParser();
        pp.read(new StringReader(POLICY_URL_T_P));
        pp.write(new FileWriter(URL_T_P_FILE, false));

        pp = new PolicyParser();
        pp.read(new StringReader(POLICY_URL_PWD));
        pp.write(new FileWriter(URL_PWD_FILE, false));

        pp = new PolicyParser();
        pp.read(new StringReader(POLICY_URL_T_P_PWD));
        pp.write(new FileWriter(URL_T_P_PWD_FILE, false));

        pp = new PolicyParser();
        pp.read(new StringReader(POLICY_BADPASS));
        pp.write(new FileWriter(BADPASS_FILE, false));

        

        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(new FileInputStream
                        (System.getProperty("test.src", ".") +
                        File.separatorChar +
                        "TokenStore.keystore"),
                storePassword);

        NO_STORE_DOMAIN = new ProtectionDomain
                        (new CodeSource(new URL("file:/foo"),
                            (java.security.cert.Certificate[]) null),
                        null,  
                        null,  
                        null);  

        Certificate[] chain = (Certificate[])
                        ks.getCertificateChain("POLICY_URL");
        URL_DOMAIN = new ProtectionDomain
                        (new CodeSource(new URL("file:/foo"), chain),
                        null,  
                        null,  
                        null);  

        chain = (Certificate[])
                        ks.getCertificateChain("POLICY_URL_T");
        URL_T_DOMAIN = new ProtectionDomain
                        (new CodeSource(new URL("file:/foo"), chain),
                        null,  
                        null,  
                        null);  

        chain = (Certificate[])
                        ks.getCertificateChain("POLICY_URL_T_P");
        URL_T_P_DOMAIN = new ProtectionDomain
                        (new CodeSource(new URL("file:/foo"), chain),
                        null,  
                        null,  
                        null);  
    }

    public static void main(String[] args) throws Exception {

        init();

        

        System.setProperty("java.security.policy", "=" + NO_STORE_FILE);
        PolicyFile p = new PolicyFile();
        checkPerm(p, NO_STORE_DOMAIN);

        

        System.setProperty("java.security.policy", "=" + URL_FILE);
        p = new PolicyFile();
        checkPerm(p, URL_DOMAIN);

        

        System.setProperty("java.security.policy", "=" + URL_T_FILE);
        p = new PolicyFile();
        checkPerm(p, URL_T_DOMAIN);

        

        System.setProperty("java.security.policy", "=" + URL_T_P_FILE);
        p = new PolicyFile();
        checkPerm(p, URL_T_P_DOMAIN);

        

        System.setProperty("java.security.policy", "=" + URL_FILE);
        p = new PolicyFile();
        checkPerm(p, URL_DOMAIN);

        

        System.setProperty("java.security.policy", "=" + URL_T_P_FILE);
        p = new PolicyFile();
        checkPerm(p, URL_T_P_DOMAIN);

        

        System.setProperty("java.security.policy", "=" + BADPASS_FILE);
        p = new PolicyFile();
        try {
            checkPerm(p, URL_T_P_DOMAIN);
            throw new RuntimeException("expected SecurityException");
        } catch (SecurityException se) {
            
            
        }

        

        System.setProperty("java.security.policy", "=" + RELPASS_FILE);
        p = new PolicyFile();
        checkPerm(p, URL_T_P_DOMAIN);
    }

    private static void checkPerm(PolicyFile p, ProtectionDomain pd)
                throws Exception {
        boolean foundIt = false;
        Enumeration perms = p.getPermissions(pd).elements();
        while (perms.hasMoreElements()) {
            Permission perm = (Permission)perms.nextElement();
            if (perm instanceof AllPermission) {
                foundIt = true;
                break;
            }
        }
        if (!foundIt) {
            throw new SecurityException("expected AllPermission");
        }
    }
}
