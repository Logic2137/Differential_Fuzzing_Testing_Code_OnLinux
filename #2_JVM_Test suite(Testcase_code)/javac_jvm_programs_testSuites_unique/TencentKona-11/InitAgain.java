
import java.security.*;
import java.security.spec.*;


public class InitAgain {

    public static void main(String[] args) throws Exception {

        byte[] msg = "hello".getBytes();

        Signature s1 = Signature.getInstance("RSASSA-PSS");
        Signature s2 = Signature.getInstance("RSASSA-PSS");

        s1.setParameter(PSSParameterSpec.DEFAULT);
        s2.setParameter(PSSParameterSpec.DEFAULT);

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        KeyPair kp = kpg.generateKeyPair();

        s1.initSign(kp.getPrivate());
        s1.update(msg);
        s1.initSign(kp.getPrivate());
        s1.update(msg);
        
        
        

        s2.initVerify(kp.getPublic());
        s2.update(msg);
        s2.initVerify(kp.getPublic());
        s2.update(msg);
        s2.initVerify(kp.getPublic());
        s2.update(msg);
        
        
        

        if (!s2.verify(s1.sign())) {
            throw new Exception();
        }
    }
}
