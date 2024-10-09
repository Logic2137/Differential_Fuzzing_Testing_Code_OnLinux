


import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRLEntry;
import java.util.Date;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;
import sun.security.x509.*;

public class OrderAndDup {
    public static void main(String[] args) throws Exception {

        
        int count = 20;
        BigInteger[] serials = new BigInteger[count];
        for (int i=0; i<count; i++) {
            serials[i] = BigInteger.valueOf(i*7%10);
        }

        
        X509CRLEntry[] badCerts = new X509CRLEntry[count];
        for (int i=0; i<count; i++) {
            badCerts[i] = new X509CRLEntryImpl(serials[i],
                    new Date(System.currentTimeMillis()+i*1000));
        }
        X500Name owner = new X500Name("CN=CA");
        X509CRLImpl crl = new X509CRLImpl(owner, new Date(), new Date(), badCerts);
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        crl.sign(kpg.genKeyPair().getPrivate(), "SHA1withRSA");
        byte[] data = crl.getEncodedInternal();

        
        checkData(crl, data, serials);

        
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509CRLImpl crl2 = (X509CRLImpl)cf.generateCRL(new ByteArrayInputStream(data));

        
        data = crl2.getEncodedInternal();
        checkData(crl2, data, serials);
    }

    
    
    static void checkData(X509CRLImpl c, byte[] data, BigInteger[] expected)
            throws Exception {
        if (c.getRevokedCertificates().size() != expected.length) {
            throw new Exception("Wrong count in CRL object, now " +
                    c.getRevokedCertificates().size());
        }
        DerValue d1 = new DerValue(data);
        
        DerValue[] d2 = new DerInputStream(
                d1.data.getSequence(0)[4].toByteArray())
                .getSequence(0);
        if (d2.length != expected.length) {
            throw new Exception("Wrong count in raw data, now " + d2.length);
        }
        for (int i=0; i<d2.length; i++) {
            
            BigInteger bi = d2[i].data.getBigInteger();
            if (!bi.equals(expected[i])) {
                throw new Exception("Entry at #" + i + " is " + bi
                        + ", should be " + expected[i]);
            }
        }
    }
}

