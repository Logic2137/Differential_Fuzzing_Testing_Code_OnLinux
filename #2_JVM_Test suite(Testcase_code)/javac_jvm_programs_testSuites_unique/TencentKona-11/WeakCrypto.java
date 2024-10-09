


import java.io.File;
import java.lang.Exception;
import java.nio.file.Files;
import java.nio.file.Paths;

import sun.security.krb5.internal.crypto.EType;
import sun.security.krb5.EncryptedData;

public class WeakCrypto {
    public static void main(String[] args) throws Exception {
        String conf = "[libdefaults]\n" +
                (args.length > 0 ? ("allow_weak_crypto = " + args[0]) : "");
        Files.write(Paths.get("krb5.conf"), conf.getBytes());
        System.setProperty("java.security.krb5.conf", "krb5.conf");

        boolean expected = args.length != 0 && args[0].equals("true");
        int[] etypes = EType.getBuiltInDefaults();

        boolean found = false;
        for (int i=0, length = etypes.length; i<length; i++) {
            if (etypes[i] == EncryptedData.ETYPE_DES_CBC_CRC ||
                    etypes[i] == EncryptedData.ETYPE_DES_CBC_MD4 ||
                    etypes[i] == EncryptedData.ETYPE_DES_CBC_MD5) {
                found = true;
            }
        }
        if (expected != found) {
            throw new Exception();
        }
    }
}
