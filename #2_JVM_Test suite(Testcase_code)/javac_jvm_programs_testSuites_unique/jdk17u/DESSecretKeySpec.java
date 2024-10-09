import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

public class DESSecretKeySpec {

    public static void main(String[] arg) throws Exception {
        Cipher c;
        byte[] key = new byte[] { '1', '2', '3', '4', '5', '6', '7', '8', '1', '2', '3', '4', '5', '6', '7', '8', '1', '2', '3', '4', '5', '6', '7', '8' };
        System.out.println("Testing DES key");
        SecretKeySpec skey = new SecretKeySpec(key, "DES");
        c = Cipher.getInstance("DES/CBC/PKCS5Padding", "SunJCE");
        SecretKeyFactory.getInstance("DES", "SunJCE").generateSecret(skey);
        System.out.println("Testing DESede key");
        skey = new SecretKeySpec(key, "DESede");
        c = Cipher.getInstance("DESede/CBC/PKCS5Padding", "SunJCE");
        SecretKeyFactory.getInstance("TripleDES", "SunJCE").generateSecret(skey);
    }
}
