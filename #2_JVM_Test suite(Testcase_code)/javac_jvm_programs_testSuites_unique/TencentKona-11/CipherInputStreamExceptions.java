



import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.Exception;
import java.lang.RuntimeException;
import java.lang.Throwable;
import java.security.AlgorithmParameters;
import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.GCMParameterSpec;

public class CipherInputStreamExceptions {

    static SecretKeySpec key = new SecretKeySpec(new byte[16], "AES");
    static GCMParameterSpec gcmspec = new GCMParameterSpec(128, new byte[16]);
    static IvParameterSpec iv = new IvParameterSpec(new byte[16]);
    static boolean failure = false;

    

    static void gcm_AEADBadTag() throws Exception {
        Cipher c;
        byte[] read = new byte[200];

        System.out.println("Running gcm_AEADBadTag");

        
        byte[] ct = encryptedText("GCM", 100);
        
        ct = corruptGCM(ct);
        
        CipherInputStream in = getStream("GCM", ct);

        try {
            int size = in.read(read);
            throw new RuntimeException("Fail: CipherInputStream.read() " +
                    "returned " + size + " and didn't throw an exception.");
        } catch (IOException e) {
            Throwable ec = e.getCause();
            if (ec instanceof AEADBadTagException) {
                System.out.println("  Pass.");
            } else {
                System.out.println("  Fail: " + ec.getMessage());
                throw new RuntimeException(ec);
            }
        } finally {
            in.close();
        }
    }

    

    static void gcm_shortReadAEAD() throws Exception {
        Cipher c;
        byte[] read = new byte[100];

        System.out.println("Running gcm_shortReadAEAD");

        byte[] pt = new byte[600];
        pt[0] = 1;
        
        byte[] ct = encryptedText("GCM", pt);
        
        CipherInputStream in = getStream("GCM", ct);

        int size = 0;
        try {
            size = in.read(read);
            in.close();
            if (read.length != 100) {
                throw new RuntimeException("Fail: read size = " + read.length +
                        "should be 100.");
            }
            if (read[0] != 1) {
                throw new RuntimeException("Fail: The decrypted text does " +
                        "not match the plaintext: '" + read[0] +"'");
            }
        } catch (IOException e) {
            System.out.println("  Fail: " + e.getMessage());
            throw new RuntimeException(e.getCause());
        }
        System.out.println("  Pass.");
    }

    
    static void gcm_suppressUnreadCorrupt() throws Exception {
        Cipher c;
        byte[] read = new byte[200];

        System.out.println("Running supressUnreadCorrupt test");

        
        byte[] ct = encryptedText("GCM", 100);
        
        ct = corruptGCM(ct);
        
        CipherInputStream in = getStream("GCM", ct);

        try {
            in.close();
            System.out.println("  Pass.");
        } catch (IOException e) {
            System.out.println("  Fail: " + e.getMessage());
            throw new RuntimeException(e.getCause());
        }
    }

    
    static void gcm_oneReadByte() throws Exception {

        System.out.println("Running gcm_oneReadByte test");

        
        byte[] ct = encryptedText("GCM", 100);
        
        CipherInputStream in = getStream("GCM", ct);

        try {
            in.read();
            System.out.println("  Pass.");
        } catch (Exception e) {
            System.out.println("  Fail: " + e.getMessage());
            throw new RuntimeException(e.getCause());
        }
    }

    
    static void gcm_oneReadByteCorrupt() throws Exception {

        System.out.println("Running gcm_oneReadByteCorrupt test");

        
        byte[] ct = encryptedText("GCM", 100);
        
        ct = corruptGCM(ct);
        
        CipherInputStream in = getStream("GCM", ct);

        try {
            in.read();
            System.out.println("  Fail. No exception thrown.");
        } catch (IOException e) {
            Throwable ec = e.getCause();
            if (ec instanceof AEADBadTagException) {
                System.out.println("  Pass.");
            } else {
                System.out.println("  Fail: " + ec.getMessage());
                throw new RuntimeException(ec);
            }
        }
    }

    

    static void cbc_shortStream() throws Exception {
        Cipher c;
        AlgorithmParameters params;
        byte[] read = new byte[200];

        System.out.println("Running cbc_shortStream");

        
        byte[] ct = encryptedText("CBC", 97);
        
        CipherInputStream in = getStream("CBC", ct, 96);

        try {
            int size = in.read(read);
            in.close();
            if (size != 80) {
                throw new RuntimeException("Fail: CipherInputStream.read() " +
                        "returned " + size + ". Should have been 80");
            }
            System.out.println("  Pass.");
        } catch (IOException e) {
            System.out.println("  Fail:  " + e.getMessage());
            throw new RuntimeException(e.getCause());
        }
    }

    

    static void cbc_shortRead400() throws Exception {
        System.out.println("Running cbc_shortRead400");

        
        byte[] ct = encryptedText("CBC", 400);
        
        CipherInputStream in = getStream("CBC", ct);

        try {
            in.read();
            in.close();
            System.out.println("  Pass.");
        } catch (IOException e) {
            System.out.println("  Fail:  " + e.getMessage());
            throw new RuntimeException(e.getCause());
        }
    }

    

    static void cbc_shortRead600() throws Exception {
        System.out.println("Running cbc_shortRead600");

        
        byte[] ct = encryptedText("CBC", 600);
        
        CipherInputStream in = getStream("CBC", ct);

        try {
            in.read();
            in.close();
            System.out.println("  Pass.");
        } catch (IOException e) {
            System.out.println("  Fail:  " + e.getMessage());
            throw new RuntimeException(e.getCause());
        }
    }

    

    static void cbc_readAllIllegalBlockSize() throws Exception {
        byte[] read = new byte[200];

        System.out.println("Running cbc_readAllIllegalBlockSize test");

        
        byte[] ct = encryptedText("CBC", 96);
        
        CipherInputStream in = getStream("CBC", ct, 95);

        try {
            int s, size = 0;
            while ((s = in.read(read)) != -1) {
                size += s;
            }
            throw new RuntimeException("Fail: No IllegalBlockSizeException. " +
                    "CipherInputStream.read() returned " + size);

        } catch (IOException e) {
            Throwable ec = e.getCause();
            if (ec instanceof IllegalBlockSizeException) {
                System.out.println("  Pass.");
            } else {
                System.out.println("  Fail: " + ec.getMessage());
                throw new RuntimeException(ec);
            }
        }
    }

    
    static byte[] encryptedText(String mode, int length) throws Exception{
        return encryptedText(mode, new byte[length]);
    }

    
    static byte[] encryptedText(String mode, byte[] pt) throws Exception{
        Cipher c;
        if (mode.compareTo("GCM") == 0) {
            c = Cipher.getInstance("AES/GCM/PKCS5Padding", "SunJCE");
            c.init(Cipher.ENCRYPT_MODE, key, gcmspec);
        } else if (mode.compareTo("CBC") == 0) {
            c = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
            c.init(Cipher.ENCRYPT_MODE, key, iv);
        } else {
            return null;
        }

        return c.doFinal(pt);
    }

    
    static CipherInputStream getStream(String mode, byte[] ct) throws Exception {
        return getStream(mode, ct, ct.length);
    }

    
    static CipherInputStream getStream(String mode, byte[] ct, int length)
            throws Exception {
        Cipher c;

        if (mode.compareTo("GCM") == 0) {
            c = Cipher.getInstance("AES/GCM/PKCS5Padding", "SunJCE");
            c.init(Cipher.DECRYPT_MODE, key, gcmspec);
        } else if (mode.compareTo("CBC") == 0) {
            c = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
            c.init(Cipher.DECRYPT_MODE, key, iv);
        } else {
            return null;
        }

        return new CipherInputStream(new ByteArrayInputStream(ct, 0, length), c);

    }

    
    static byte[] corruptGCM(byte[] ct) {
        ct[ct.length - 1] = (byte) (ct[ct.length - 1] + 1);
        return ct;
    }

    public static void main(String[] args) throws Exception {
        gcm_AEADBadTag();
        gcm_shortReadAEAD();
        gcm_suppressUnreadCorrupt();
        gcm_oneReadByte();
        gcm_oneReadByteCorrupt();
        cbc_shortStream();
        cbc_shortRead400();
        cbc_shortRead600();
        cbc_readAllIllegalBlockSize();
    }
}
