import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.HashMap;

public class GCMLargeDataKAT {

    static final HashMap<Integer, String> results = new HashMap<>() {

        {
            put(65534, "1397b91c31ce793895edace4e175bfee");
            put(65535, "4ad101c9f450e686668b3f8f05db96f0");
            put(65536, "fbfaee3451acd3f603200d6be0f39b24");
            put(65537, "e7dfca4a71495c65d20982c3c9b9813f");
            put(67583, "c8ebdcb3532ec6c165de961341af7635");
            put(67584, "36559d108dfd25dd29da3fec3455b9e5");
            put(67585, "1d21b42d80ea179810744fc23dc228b6");
            put(102400, "0d1544fcab20bbd4c8103b9d273f2c82");
            put(102401, "f2d53ef65fd12d0a861368659b23ea2e");
            put(102402, "97f0f524cf63d2d9d23d81e64d416ee0");
            put(102403, "4a6b4af55b7d9016b64114d6813d639c");
            put(102404, "ba63cc131fcde2f12ddf2ac634201be8");
            put(102405, "673d05c7fe5e283e42e5c0d049fdcea6");
            put(102406, "76cc99a7850ce857eb3cb43049cf9877");
            put(102407, "65863f99072cf2eb7fce18bd78b33f4e");
            put(102408, "b9184f0f272682cc1f791fa7070eddd4");
            put(102409, "45fe36afef43cc665bf22a9ca200c3c2");
            put(102410, "67249e41646edcb37a78a61b0743cf11");
            put(102411, "ffdc611e29c8849842e81ec78f32c415");
            put(102412, "b7fde7fd52221057dccc1c181a140125");
            put(102413, "4b1d6c64d56448105e5613157e69c0ae");
            put(102414, "6d2c0b26c0c8785c8eec3298a5f0080c");
            put(102415, "1df2061b114fbe56bdf3717e3ee61ef9");
            put(102416, "a691742692c683ac9d1254df5fc5f768");
        }
    };

    static final int HIGHLEN = 102416;

    static final int GCM_TAG_LENGTH = 16;

    static final byte[] iv = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };

    static final byte[] key_code = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };

    static final GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);

    static final SecretKey key = new SecretKeySpec(key_code, "AES");

    static boolean testresult = true;

    static byte[] plaintext = new byte[HIGHLEN];

    static MessageDigest md5;

    Cipher cipher;

    GCMLargeDataKAT() {
    }

    byte[] encrypt(int inLen) {
        try {
            cipher = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            return cipher.doFinal(plaintext, 0, inLen);
        } catch (Exception e) {
            System.err.println("Encrypt Failure (length = " + inLen + ") : " + e.getMessage());
            e.printStackTrace();
        }
        return new byte[0];
    }

    static byte[] hash(byte[] data) {
        return md5.digest(data);
    }

    boolean decrypt(byte[] data) {
        byte[] result = null;
        int len = data.length - GCM_TAG_LENGTH;
        if (data.length == 0) {
            return false;
        }
        try {
            cipher = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            result = cipher.doFinal(data);
        } catch (Exception e) {
            System.err.println("Decrypt Failure (length = " + len + ") : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        if (result.length != len) {
            System.err.println("Decrypt Failure (length = " + len + ") : plaintext length invalid = " + result.length);
        }
        int i = 0;
        while (result.length > i) {
            if (result[i++] != 0) {
                System.err.println("Decrypt Failure (length = " + len + ") : plaintext invalid, char index " + i);
                return false;
            }
        }
        return true;
    }

    void test() throws Exception {
        for (int l : results.keySet()) {
            byte[] enc = new GCMLargeDataKAT().encrypt(l);
            String hashstr = toHex(hash(enc));
            boolean r = (hashstr.compareTo(results.get(l)) == 0);
            System.out.println("---------------------------------------------");
            System.out.println("Encrypt data size " + l + " \tResult: " + (r ? "Pass" : "Fail"));
            if (!r) {
                if (enc.length != 0) {
                    System.out.println("\tExpected: " + results.get(l));
                    System.out.println("\tReturned: " + hashstr);
                }
                testresult = false;
                continue;
            }
            r = decrypt(enc);
            System.out.println("Decrypt data size " + l + " \tResult: " + (r ? "Pass" : "Fail"));
            if (!r) {
                testresult = false;
            }
        }
        if (!testresult) {
            throw new Exception("Tests failed");
        }
    }

    public static void main(String[] args) throws Exception {
        md5 = MessageDigest.getInstance("MD5");
        if (args.length > 0) {
            int len = Integer.parseInt(args[0]);
            byte[] e = new GCMLargeDataKAT().encrypt(len);
            System.out.println(toHex(hash(e)));
            return;
        }
        new GCMLargeDataKAT().test();
    }

    static String toHex(byte[] bytes) {
        StringBuffer hexStringBuffer = new StringBuffer(32);
        for (int i = 0; i < bytes.length; i++) {
            hexStringBuffer.append(byteToHex(bytes[i]));
        }
        return hexStringBuffer.toString();
    }

    static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }
}
