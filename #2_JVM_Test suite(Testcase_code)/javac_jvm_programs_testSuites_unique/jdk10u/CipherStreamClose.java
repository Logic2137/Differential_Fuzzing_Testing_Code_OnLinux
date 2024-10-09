



import java.io.*;
import java.security.DigestOutputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class CipherStreamClose {
    private static final String message = "This is the sample message";
    static boolean debug = false;

    
    public static byte[] blockEncrypt(String message, SecretKey key)
        throws Exception {

        byte[] data;
        Cipher encCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        encCipher.init(Cipher.ENCRYPT_MODE, key);
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(message);
            }
            data = bos.toByteArray();
        }

        if (debug) {
            System.out.println(DatatypeConverter.printHexBinary(data));
        }
        return encCipher.doFinal(data);

    }

    
    public static Object blockDecrypt(byte[] data, SecretKey key)
        throws Exception {

        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, key);
        data = c.doFinal(data);
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
            try (ObjectInputStream ois = new ObjectInputStream(bis)) {
                return ois.readObject();
            }
        }
    }

    public static byte[] streamEncrypt(String message, SecretKey key,
        MessageDigest digest)
        throws Exception {

        byte[] data;
        Cipher encCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        encCipher.init(Cipher.ENCRYPT_MODE, key);
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DigestOutputStream dos = new DigestOutputStream(bos, digest);
            CipherOutputStream cos = new CipherOutputStream(dos, encCipher)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(cos)) {
                oos.writeObject(message);
            }
            data = bos.toByteArray();
        }

        if (debug) {
            System.out.println(DatatypeConverter.printHexBinary(data));
        }
        return data;
    }

    public static Object streamDecrypt(byte[] data, SecretKey key,
        MessageDigest digest) throws Exception {

        Cipher decCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        decCipher.init(Cipher.DECRYPT_MODE, key);
        digest.reset();
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
            DigestInputStream dis = new DigestInputStream(bis, digest);
            CipherInputStream cis = new CipherInputStream(dis, decCipher)) {

            try (ObjectInputStream ois = new ObjectInputStream(cis)) {
                return ois.readObject();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        SecretKeySpec key = new SecretKeySpec(
            DatatypeConverter.parseHexBinary(
            "12345678123456781234567812345678"), "AES");

        
        byte[] se = streamEncrypt(message, key, digest);
        
        byte[] sd = digest.digest();
        digest.reset();
        
        byte[] be = blockEncrypt(message, key);
        
        byte[] bd = digest.digest(be);
        
        if (!Arrays.equals(sd, bd)) {
            System.err.println("Stream: "+DatatypeConverter.printHexBinary(se)+
                "\t Digest: "+DatatypeConverter.printHexBinary(sd));
            System.err.println("Block : "+DatatypeConverter.printHexBinary(be)+
                "\t Digest: "+DatatypeConverter.printHexBinary(bd));
            throw new Exception("stream & block encryption does not match");
        }

        digest.reset();
        
        String bm = (String) blockDecrypt(be, key);
        if (message.compareTo(bm) != 0) {
            System.err.println("Expected: "+message+"\nBlock:    "+bm);
            throw new Exception("Block decryption does not match expected");
        }

        
        String sm = (String) streamDecrypt(se, key, digest);
        if (message.compareTo(sm) != 0) {
            System.err.println("Expected: "+message+"\nStream:   "+sm);
            throw new Exception("Stream decryption does not match expected.");
        }
    }
}
