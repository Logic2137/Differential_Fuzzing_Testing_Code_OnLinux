

package sun.security.testlibrary;

import java.io.*;
import java.util.*;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.Extension;
import javax.security.auth.x500.X500Principal;
import java.math.BigInteger;

import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AccessDescription;
import sun.security.x509.AlgorithmId;
import sun.security.x509.AuthorityInfoAccessExtension;
import sun.security.x509.AuthorityKeyIdentifierExtension;
import sun.security.x509.SubjectKeyIdentifierExtension;
import sun.security.x509.BasicConstraintsExtension;
import sun.security.x509.ExtendedKeyUsageExtension;
import sun.security.x509.DNSName;
import sun.security.x509.GeneralName;
import sun.security.x509.GeneralNames;
import sun.security.x509.KeyUsageExtension;
import sun.security.x509.SerialNumber;
import sun.security.x509.SubjectAlternativeNameExtension;
import sun.security.x509.URIName;
import sun.security.x509.KeyIdentifier;


public class CertificateBuilder {
    private final CertificateFactory factory;

    private X500Principal subjectName = null;
    private BigInteger serialNumber = null;
    private PublicKey publicKey = null;
    private Date notBefore = null;
    private Date notAfter = null;
    private final Map<String, Extension> extensions = new HashMap<>();
    private byte[] tbsCertBytes;
    private byte[] signatureBytes;

    
    public CertificateBuilder() throws CertificateException {
        factory = CertificateFactory.getInstance("X.509");
    }

    
    public void setSubjectName(X500Principal name) {
        subjectName = name;
    }

    
    public void setSubjectName(String name) {
        subjectName = new X500Principal(name);
    }

    
    public void setPublicKey(PublicKey pubKey) {
        publicKey = Objects.requireNonNull(pubKey, "Caught null public key");
    }

    
    public void setNotBefore(Date nbDate) {
        Objects.requireNonNull(nbDate, "Caught null notBefore date");
        notBefore = (Date)nbDate.clone();
    }

    
    public void setNotAfter(Date naDate) {
        Objects.requireNonNull(naDate, "Caught null notAfter date");
        notAfter = (Date)naDate.clone();
    }

    
    public void setValidity(Date nbDate, Date naDate) {
        setNotBefore(nbDate);
        setNotAfter(naDate);
    }

    
    public void setSerialNumber(BigInteger serial) {
        Objects.requireNonNull(serial, "Caught null serial number");
        serialNumber = serial;
    }


    
    public void addExtension(Extension ext) {
        Objects.requireNonNull(ext, "Caught null extension");
        extensions.put(ext.getId(), ext);
    }

    
    public void addExtensions(List<Extension> extList) {
        Objects.requireNonNull(extList, "Caught null extension list");
        for (Extension ext : extList) {
            extensions.put(ext.getId(), ext);
        }
    }

    
    public void addSubjectAltNameDNSExt(List<String> dnsNames) throws IOException {
        if (!dnsNames.isEmpty()) {
            GeneralNames gNames = new GeneralNames();
            for (String name : dnsNames) {
                gNames.add(new GeneralName(new DNSName(name)));
            }
            addExtension(new SubjectAlternativeNameExtension(false,
                    gNames));
        }
    }

    
    public void addAIAExt(List<String> locations)
            throws IOException {
        if (!locations.isEmpty()) {
            List<AccessDescription> acDescList = new ArrayList<>();
            for (String ocspUri : locations) {
                acDescList.add(new AccessDescription(
                        AccessDescription.Ad_OCSP_Id,
                        new GeneralName(new URIName(ocspUri))));
            }
            addExtension(new AuthorityInfoAccessExtension(acDescList));
        }
    }

    
    public void addKeyUsageExt(boolean[] bitSettings) throws IOException {
        addExtension(new KeyUsageExtension(bitSettings));
    }

    
    public void addBasicConstraintsExt(boolean crit, boolean isCA,
            int maxPathLen) throws IOException {
        addExtension(new BasicConstraintsExtension(crit, isCA, maxPathLen));
    }

    
    public void addAuthorityKeyIdExt(X509Certificate authorityCert)
            throws IOException {
        addAuthorityKeyIdExt(authorityCert.getPublicKey());
    }

    
    public void addAuthorityKeyIdExt(PublicKey authorityKey) throws IOException {
        KeyIdentifier kid = new KeyIdentifier(authorityKey);
        addExtension(new AuthorityKeyIdentifierExtension(kid, null, null));
    }

    
    public void addSubjectKeyIdExt(PublicKey subjectKey) throws IOException {
        byte[] keyIdBytes = new KeyIdentifier(subjectKey).getIdentifier();
        addExtension(new SubjectKeyIdentifierExtension(keyIdBytes));
    }

    
    public void addExtendedKeyUsageExt(List<String> ekuOids)
            throws IOException {
        if (!ekuOids.isEmpty()) {
            Vector<ObjectIdentifier> oidVector = new Vector<>();
            for (String oid : ekuOids) {
                oidVector.add(new ObjectIdentifier(oid));
            }
            addExtension(new ExtendedKeyUsageExtension(oidVector));
        }
    }

    
    public void reset() {
        extensions.clear();
        subjectName = null;
        notBefore = null;
        notAfter = null;
        serialNumber = null;
        publicKey = null;
        signatureBytes = null;
        tbsCertBytes = null;
    }

    
    public X509Certificate build(X509Certificate issuerCert,
            PrivateKey issuerKey, String algName)
            throws IOException, CertificateException, NoSuchAlgorithmException {
        

        AlgorithmId signAlg = AlgorithmId.get(algName);
        byte[] encodedCert = encodeTopLevel(issuerCert, issuerKey, signAlg);
        ByteArrayInputStream bais = new ByteArrayInputStream(encodedCert);
        return (X509Certificate)factory.generateCertificate(bais);
    }

    
    private byte[] encodeTopLevel(X509Certificate issuerCert,
            PrivateKey issuerKey, AlgorithmId signAlg)
            throws CertificateException, IOException {
        DerOutputStream outerSeq = new DerOutputStream();
        DerOutputStream topLevelItems = new DerOutputStream();

        tbsCertBytes = encodeTbsCert(issuerCert, signAlg);
        topLevelItems.write(tbsCertBytes);
        try {
            signatureBytes = signCert(issuerKey, signAlg);
        } catch (GeneralSecurityException ge) {
            throw new CertificateException(ge);
        }
        signAlg.derEncode(topLevelItems);
        topLevelItems.putBitString(signatureBytes);
        outerSeq.write(DerValue.tag_Sequence, topLevelItems);

        return outerSeq.toByteArray();
    }

    
    private byte[] encodeTbsCert(X509Certificate issuerCert,
            AlgorithmId signAlg) throws IOException {
        DerOutputStream tbsCertSeq = new DerOutputStream();
        DerOutputStream tbsCertItems = new DerOutputStream();

        
        byte[] v3int = {0x02, 0x01, 0x02};
        tbsCertItems.write(DerValue.createTag(DerValue.TAG_CONTEXT, true,
                (byte)0), v3int);

        
        SerialNumber sn = new SerialNumber(serialNumber);
        sn.encode(tbsCertItems);

        
        signAlg.derEncode(tbsCertItems);

        
        if (issuerCert != null) {
            tbsCertItems.write(
                    issuerCert.getSubjectX500Principal().getEncoded());
        } else {
            
            tbsCertItems.write(subjectName.getEncoded());
        }

        
        DerOutputStream valSeq = new DerOutputStream();
        valSeq.putUTCTime(notBefore);
        valSeq.putUTCTime(notAfter);
        tbsCertItems.write(DerValue.tag_Sequence, valSeq);

        
        tbsCertItems.write(subjectName.getEncoded());

        
        tbsCertItems.write(publicKey.getEncoded());

        
        encodeExtensions(tbsCertItems);

        
        tbsCertSeq.write(DerValue.tag_Sequence, tbsCertItems);
        return tbsCertSeq.toByteArray();
    }

    
    private void encodeExtensions(DerOutputStream tbsStream)
            throws IOException {
        DerOutputStream extSequence = new DerOutputStream();
        DerOutputStream extItems = new DerOutputStream();

        for (Extension ext : extensions.values()) {
            ext.encode(extItems);
        }
        extSequence.write(DerValue.tag_Sequence, extItems);
        tbsStream.write(DerValue.createTag(DerValue.TAG_CONTEXT, true,
                (byte)3), extSequence);
    }

    
    private byte[] signCert(PrivateKey issuerKey, AlgorithmId signAlg)
            throws GeneralSecurityException {
        Signature sig = Signature.getInstance(signAlg.getName());
        sig.initSign(issuerKey);
        sig.update(tbsCertBytes);
        return sig.sign();
    }
 }
