import java.io.*;
import java.security.*;
import javax.crypto.*;

public class Sealtest {

    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpgen = KeyPairGenerator.getInstance("DSA");
        kpgen.initialize(512);
        KeyPair kp = kpgen.generateKeyPair();
        KeyGenerator kg = KeyGenerator.getInstance("DES", "SunJCE");
        SecretKey skey = kg.generateKey();
        Cipher c = Cipher.getInstance("DES/CFB16/PKCS5Padding", "SunJCE");
        c.init(Cipher.ENCRYPT_MODE, skey);
        SealedObject sealed = new SealedObject(kp.getPrivate(), c);
        try (FileOutputStream fos = new FileOutputStream("sealed");
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(sealed);
        }
        try (FileInputStream fis = new FileInputStream("sealed");
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            sealed = (SealedObject) ois.readObject();
        }
        System.out.println(sealed.getAlgorithm());
        PrivateKey priv = (PrivateKey) sealed.getObject(skey);
        if (!priv.equals(kp.getPrivate()))
            throw new Exception("TEST FAILED");
        System.out.println("TEST SUCCEEDED");
    }
}
