import java.io.*;
import java.security.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class StorePasswords {

    private static final String[] PBE_ALGORITHMS = new String[] { "default PBE algorithm", "PBEWithMD5AndDES", "PBEWithSHA1AndDESede", "PBEWithSHA1AndRC2_40", "PBEWithSHA1AndRC2_128", "PBEWithSHA1AndRC4_40", "PBEWithSHA1AndRC4_128", "PBEWithHmacSHA1AndAES_128", "PBEWithHmacSHA224AndAES_128", "PBEWithHmacSHA256AndAES_128", "PBEWithHmacSHA384AndAES_128", "PBEWithHmacSHA512AndAES_128", "PBEWithHmacSHA1AndAES_256", "PBEWithHmacSHA224AndAES_256", "PBEWithHmacSHA256AndAES_256", "PBEWithHmacSHA384AndAES_256", "PBEWithHmacSHA512AndAES_256" };

    private static final String KEYSTORE = "mykeystore.p12";

    private static final char[] KEYSTORE_PWD = "changeit".toCharArray();

    private static final char[] ENTRY_PWD = "protectit".toCharArray();

    private static final char[] USER_PWD = "hello1".toCharArray();

    public static void main(String[] args) throws Exception {
        new File(KEYSTORE).delete();
        int storeCount = store();
        int recoverCount = recover();
        if (recoverCount != storeCount) {
            throw new Exception("Stored " + storeCount + " user passwords, " + "recovered " + recoverCount + " user passwords");
        }
        System.out.println("\nStored " + storeCount + " user passwords, " + "recovered " + recoverCount + " user passwords");
        new File(KEYSTORE).delete();
    }

    private static int store() throws Exception {
        int count = 0;
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        System.out.println("\nLoading PKCS#12 keystore...");
        keystore.load(null, null);
        PBEKeySpec keySpec = new PBEKeySpec(USER_PWD);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBE");
        SecretKey key = factory.generateSecret(keySpec);
        PBEParameterSpec specWithEightByteSalt = new PBEParameterSpec("NaClNaCl".getBytes(), 1024);
        for (String algorithm : PBE_ALGORITHMS) {
            try {
                System.out.println("Storing user password '" + new String(USER_PWD) + "' (protected by " + algorithm + ")");
                if (algorithm.equals("default PBE algorithm")) {
                    keystore.setKeyEntry("this entry is protected by " + algorithm, key, ENTRY_PWD, null);
                } else {
                    keystore.setEntry("this entry is protected by " + algorithm, new KeyStore.SecretKeyEntry(key), new KeyStore.PasswordProtection(ENTRY_PWD, algorithm, null));
                }
                count++;
            } catch (KeyStoreException e) {
                Throwable inner = e.getCause();
                if (inner instanceof UnrecoverableKeyException) {
                    Throwable inner2 = inner.getCause();
                    if (inner2 instanceof InvalidAlgorithmParameterException) {
                        System.out.println("...re-trying due to: " + inner2.getMessage());
                        keystore.setEntry("this entry is protected by " + algorithm, new KeyStore.SecretKeyEntry(key), new KeyStore.PasswordProtection(ENTRY_PWD, algorithm, specWithEightByteSalt));
                        count++;
                    } else if (inner2 instanceof InvalidKeyException) {
                        System.out.println("...skipping due to: " + inner2.getMessage());
                        continue;
                    }
                } else {
                    throw e;
                }
            }
        }
        System.out.println("Storing PKCS#12 keystore to: " + KEYSTORE);
        try (FileOutputStream out = new FileOutputStream(KEYSTORE)) {
            keystore.store(out, KEYSTORE_PWD);
        }
        return count;
    }

    private static int recover() throws Exception {
        int count = 0;
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        System.out.println("\nLoading PKCS#12 keystore from: " + KEYSTORE);
        try (FileInputStream in = new FileInputStream(KEYSTORE)) {
            keystore.load(in, KEYSTORE_PWD);
        }
        SecretKey key;
        SecretKeyFactory factory;
        PBEKeySpec keySpec;
        for (String algorithm : PBE_ALGORITHMS) {
            key = (SecretKey) keystore.getKey("this entry is protected by " + algorithm, ENTRY_PWD);
            if (key != null) {
                count++;
                factory = SecretKeyFactory.getInstance(key.getAlgorithm());
                keySpec = (PBEKeySpec) factory.getKeySpec(key, PBEKeySpec.class);
                char[] pwd = keySpec.getPassword();
                System.out.println("Recovered user password '" + new String(pwd) + "' (protected by " + algorithm + ")");
                if (!Arrays.equals(USER_PWD, pwd)) {
                    throw new Exception("Failed to recover the user password " + "protected by " + algorithm);
                }
            }
        }
        return count;
    }
}
