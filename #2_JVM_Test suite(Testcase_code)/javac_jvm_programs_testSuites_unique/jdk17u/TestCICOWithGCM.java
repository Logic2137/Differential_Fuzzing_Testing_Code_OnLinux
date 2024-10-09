import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.math.*;
import java.io.*;
import java.util.*;

public class TestCICOWithGCM {

    public static void main(String[] args) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("AES", "SunJCE");
        kg.init(128);
        SecretKey key = kg.generateKey();
        byte[] plainText = new byte[800];
        Random rdm = new Random();
        rdm.nextBytes(plainText);
        Cipher encCipher = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");
        encCipher.init(Cipher.ENCRYPT_MODE, key);
        Cipher decCipher = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");
        decCipher.init(Cipher.DECRYPT_MODE, key, encCipher.getParameters());
        ByteArrayInputStream baInput = new ByteArrayInputStream(plainText);
        CipherInputStream ciInput = new CipherInputStream(baInput, encCipher);
        ByteArrayOutputStream baOutput = new ByteArrayOutputStream();
        CipherOutputStream ciOutput = new CipherOutputStream(baOutput, decCipher);
        byte[] buffer = new byte[800];
        int len = ciInput.read(buffer);
        System.out.println("read " + len + " bytes from input buffer");
        while (len != -1) {
            ciOutput.write(buffer, 0, len);
            System.out.println("wite " + len + " bytes to output buffer");
            len = ciInput.read(buffer);
            if (len != -1) {
                System.out.println("read " + len + " bytes from input buffer");
            } else {
                System.out.println("finished reading");
            }
        }
        ciOutput.flush();
        ciInput.close();
        ciOutput.close();
        byte[] recovered = baOutput.toByteArray();
        System.out.println("recovered " + recovered.length + " bytes");
        if (!Arrays.equals(plainText, recovered)) {
            throw new RuntimeException("diff check failed!");
        } else {
            System.out.println("diff check passed");
        }
    }
}
