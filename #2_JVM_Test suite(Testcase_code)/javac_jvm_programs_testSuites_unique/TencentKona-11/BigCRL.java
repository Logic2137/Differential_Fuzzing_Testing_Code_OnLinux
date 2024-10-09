import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.PrivateKey;
import java.security.cert.X509CRLEntry;
import java.util.Arrays;
import java.util.Date;
import sun.security.x509.*;
import java.security.cert.CertificateFactory;
import java.io.ByteArrayInputStream;

public class BigCRL {

    public static void main(String[] args) throws Exception {
        int n = 500000;
        String ks = System.getProperty("test.src", ".") + "/../../../../javax/net/ssl/etc/keystore";
        String pass = "passphrase";
        String alias = "dummy";
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(ks), pass.toCharArray());
        Certificate signerCert = keyStore.getCertificate(alias);
        byte[] encoded = signerCert.getEncoded();
        X509CertImpl signerCertImpl = new X509CertImpl(encoded);
        X509CertInfo signerCertInfo = (X509CertInfo) signerCertImpl.get(X509CertImpl.NAME + "." + X509CertImpl.INFO);
        X500Name owner = (X500Name) signerCertInfo.get(X509CertInfo.SUBJECT + "." + X509CertInfo.DN_NAME);
        Date date = new Date();
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, pass.toCharArray());
        String sigAlgName = signerCertImpl.getSigAlgOID();
        X509CRLEntry[] badCerts = new X509CRLEntry[n];
        CRLExtensions ext = new CRLExtensions();
        ext.set("Reason", new CRLReasonCodeExtension(1));
        for (int i = 0; i < n; i++) {
            badCerts[i] = new X509CRLEntryImpl(BigInteger.valueOf(i), date, ext);
        }
        X509CRLImpl crl = new X509CRLImpl(owner, date, date, badCerts);
        crl.sign(privateKey, sigAlgName);
        byte[] data = crl.getEncodedInternal();
        if ((data[1] & 0xff) != 0x84) {
            throw new Exception("The file should be big enough?");
        }
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        cf.generateCRL(new ByteArrayInputStream(data));
    }
}
