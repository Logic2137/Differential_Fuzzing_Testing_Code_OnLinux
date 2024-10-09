

import sun.security.x509.X500Name;
import sun.security.x509.X509CRLImpl;

import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Date;


public class DefaultParamSpec {
    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSASSA-PSS");
        KeyFactory kf = KeyFactory.getInstance("RSASSA-PSS");
        kpg.initialize(new RSAKeyGenParameterSpec(2048,
                RSAKeyGenParameterSpec.F4,
                new PSSParameterSpec(
                        "SHA-384", "MGF1",
                        new MGF1ParameterSpec("SHA-384"),
                        48, PSSParameterSpec.TRAILER_FIELD_BC)));

        X509CRLImpl crl = new X509CRLImpl(
                new X500Name("CN=Issuer"), new Date(), new Date());
        crl.sign(kpg.generateKeyPair().getPrivate(), "RSASSA-PSS");
    }
}
