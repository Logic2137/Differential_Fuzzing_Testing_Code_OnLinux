


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertPath;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathValidator;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.CRLException;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXParameters;
import java.security.cert.X509Certificate;
import java.security.cert.X509CRL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CertUtils {

    private CertUtils() {}

    
    public static X509Certificate getCertFromFile(String certFilePath)
        throws CertificateException, IOException {
        File certFile = new File(System.getProperty("test.src", "."),
                                 certFilePath);
        try (FileInputStream fis = new FileInputStream(certFile)) {
            return (X509Certificate)
                CertificateFactory.getInstance("X.509")
                                  .generateCertificate(fis);
        }
    }

    
    public static X509Certificate getCertFromString(String cert)
        throws CertificateException {
        byte[] certBytes = cert.getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(certBytes);
        return (X509Certificate)
            CertificateFactory.getInstance("X.509").generateCertificate(bais);
    }

    
    public static X509CRL getCRLFromFile(String crlFilePath)
        throws CertificateException, CRLException, IOException {
        File crlFile = new File(System.getProperty("test.src", "."),
                                crlFilePath);
        try (FileInputStream fis = new FileInputStream(crlFile)) {
            return (X509CRL)
                CertificateFactory.getInstance("X.509").generateCRL(fis);
        }
    }

    
    public static X509CRL getCRLFromString(String crl)
        throws CertificateException, CRLException {
        byte[] crlBytes = crl.getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(crlBytes);
        return (X509CRL)
            CertificateFactory.getInstance("X.509").generateCRL(bais);
    }

    
    public static CertPath buildPath(String [] fileNames) throws Exception {
        return buildPath("", fileNames);
    }

    
    public static CertPath buildPath(String relPath, String [] fileNames)
        throws Exception {
        List<X509Certificate> list = new ArrayList<X509Certificate>();
        for (int i = 0; i < fileNames.length; i++) {
            list.add(0, getCertFromFile(relPath + fileNames[i]));
        }
        CertificateFactory cf = CertificateFactory.getInstance("X509");
        return(cf.generateCertPath(list));
    }


    
    public static CertStore createStore(String [] fileNames) throws Exception {
        return createStore("", fileNames);
    }

    
    public static CertStore createStore(String relPath, String [] fileNames)
        throws Exception {
        Set<X509Certificate> certs = new HashSet<X509Certificate>();
        for (int i = 0; i < fileNames.length; i++) {
            certs.add(getCertFromFile(relPath + fileNames[i]));
        }
        return CertStore.getInstance("Collection",
            new CollectionCertStoreParameters(certs));
    }

    
    public static CertStore createCRLStore(String [] fileNames)
        throws Exception {
        return createCRLStore("", fileNames);
    }

    
    public static CertStore createCRLStore(String relPath, String [] fileNames)
        throws Exception {
        Set<X509CRL> crls = new HashSet<X509CRL>();
        for (int i = 0; i < fileNames.length; i++) {
            crls.add(getCRLFromFile(relPath + fileNames[i]));
        }
        return CertStore.getInstance("Collection",
            new CollectionCertStoreParameters(crls));
    }

    
    public static PKIXCertPathBuilderResult build(PKIXBuilderParameters params)
        throws Exception {
        CertPathBuilder builder =
            CertPathBuilder.getInstance("PKIX");
        return (PKIXCertPathBuilderResult) builder.build(params);
    }

    
    public static PKIXCertPathValidatorResult validate
        (CertPath path, PKIXParameters params) throws Exception {
        CertPathValidator validator =
            CertPathValidator.getInstance("PKIX");
        return (PKIXCertPathValidatorResult) validator.validate(path, params);
    }

    
    private static byte[] getTotalBytes(InputStream is) throws IOException {
           byte[] buffer = new byte[8192];
        ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
        int n;
        baos.reset();
        while ((n = is.read(buffer, 0, buffer.length)) != -1) {
            baos.write(buffer, 0, n);
        }
        return baos.toByteArray();
    }
}
