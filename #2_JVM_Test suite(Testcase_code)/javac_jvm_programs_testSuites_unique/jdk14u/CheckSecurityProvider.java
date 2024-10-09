



import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class CheckSecurityProvider {
    public static void main(String[] args) throws Exception {
        ModuleLayer layer = ModuleLayer.boot();

        System.setSecurityManager(new SecurityManager());

        String os = System.getProperty("os.name");
        

        List<String> expected = new ArrayList<>();

        
        if (os.equals("SunOS")) {
            layer.findModule("jdk.crypto.ucrypto")
                .ifPresent(m -> expected.add("com.oracle.security.ucrypto.UcryptoProvider"));
            layer.findModule("jdk.crypto.cryptoki")
                .ifPresent(m -> expected.add("sun.security.pkcs11.SunPKCS11"));
        }
        expected.add("sun.security.provider.Sun");
        expected.add("sun.security.rsa.SunRsaSign");
        layer.findModule("jdk.crypto.ec")
            .ifPresent(m -> expected.add("sun.security.ec.SunEC"));
        expected.add("sun.security.ssl.SunJSSE");
        expected.add("com.sun.crypto.provider.SunJCE");
        layer.findModule("jdk.security.jgss")
            .ifPresent(m -> expected.add("sun.security.jgss.SunProvider"));
        layer.findModule("java.security.sasl")
            .ifPresent(m -> expected.add("com.sun.security.sasl.Provider"));
        layer.findModule("java.xml.crypto")
            .ifPresent(m -> expected.add("org.jcp.xml.dsig.internal.dom.XMLDSigRI"));
        layer.findModule("java.smartcardio")
            .ifPresent(m -> expected.add("sun.security.smartcardio.SunPCSC"));
        layer.findModule("java.naming")
            .ifPresent(m -> expected.add("sun.security.provider.certpath.ldap.JdkLDAP"));
        layer.findModule("jdk.security.jgss")
            .ifPresent(m -> expected.add("com.sun.security.sasl.gsskerb.JdkSASL"));
        if (os.startsWith("Windows")) {
            layer.findModule("jdk.crypto.mscapi")
                .ifPresent(m -> expected.add("sun.security.mscapi.SunMSCAPI"));
        }
        if (os.contains("OS X")) {
            expected.add("apple.security.AppleProvider");
        }
        if (!os.equals("SunOS")) {
            layer.findModule("jdk.crypto.cryptoki")
                .ifPresent(m -> expected.add("sun.security.pkcs11.SunPKCS11"));
        }

        List<String> actual = Stream.of(Security.getProviders())
            .map(p -> p.getClass().getName())
            .collect(Collectors.toList());

        System.out.println("Expected providers:");
        expected.stream().forEach(System.out::println);
        System.out.println("Actual providers:");
        actual.stream().forEach(System.out::println);

        if (expected.size() != actual.size()) {
            throw new Exception("Unexpected provider count. "
                + "Expected: " + expected.size() + ". Actual: " + actual.size());
        }
        Iterator<String> iter = expected.iterator();
        for (String p: actual) {
            String nextExpected = iter.next();
            if (!nextExpected.equals(p)) {
                throw new Exception("Expected " + nextExpected + ", actual " + p);
            }
        }
    }
}
