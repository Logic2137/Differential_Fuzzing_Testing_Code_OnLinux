



import sun.security.util.UntrustedCertificates;

import java.io.*;
import java.security.KeyStore;
import java.security.cert.*;
import java.util.*;

public class CheckBlockedCerts {
    public static void main(String[] args) throws Exception {

        String home = System.getProperty("java.home");
        boolean failed = false;

        
        File file = new File(home, "lib/security/cacerts");
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream fis = new FileInputStream(file)) {
            ks.load(fis, null);
        }
        System.out.println("Check for cacerts: " + ks.size());
        for (String alias: Collections.list(ks.aliases())) {
            X509Certificate cert = (X509Certificate)ks.getCertificate(alias);
            if (UntrustedCertificates.isUntrusted(cert)) {
                System.out.print(alias + " is untrusted");
                failed = true;
            }
        }

        
        Set<Certificate> blocked = new HashSet<>();

        
        File blockedCertsFile = new File(System.getProperty("test.src"),
                "../../../../../make/data/blockedcertsconverter/blocked.certs.pem");

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        try (FileInputStream fis = new FileInputStream(blockedCertsFile)) {
            Collection<? extends Certificate> certs
                    = cf.generateCertificates(fis);
            System.out.println(certs.size());
            for (Certificate c: certs) {
                blocked.add(c);
                X509Certificate cert = ((X509Certificate)c);
                if (!UntrustedCertificates.isUntrusted(cert)) {
                    System.out.println(cert.getSubjectX500Principal() +
                            " is trusted");
                    failed = true;
                }
            }
        }

        
        file = new File(home, "lib/security/blocked.certs");
        System.out.print("Check for " + file + ": ");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file)))) {
            int acount = 0;
            int ccount = 0;
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                if (line.startsWith("Algorithm")) {
                    acount++;
                } else if (!line.isEmpty() && !line.startsWith("#")) {
                    ccount++;
                }
            }
            System.out.println(acount + " algs, " + ccount + " certs" );
            if (acount != 1) {
                System.out.println("There are " + acount + " algorithms");
                failed = true;
            }
            
            if (ccount != blocked.size() * 2
                    && !blocked.isEmpty()) {
                System.out.println("Wrong blocked.certs size: "
                        + ccount + " fingerprints, "
                        + blocked.size() + " certs");
                failed = true;
            }
        }

        if (failed) {
            throw new Exception("Failed");
        }
    }
}
