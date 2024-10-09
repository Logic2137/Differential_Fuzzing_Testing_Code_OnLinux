



import java.security.Security;
import java.security.Provider;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Formatter;

import sun.security.internal.spec.TlsRsaPremasterSecretParameterSpec;

public class TestPremaster {

    public static void main(String[] args) throws Exception {
        Provider provider = Security.getProvider("SunJCE");

        KeyGenerator kg;

        kg = KeyGenerator.getInstance("SunTlsRsaPremasterSecret", provider);

        try {
            kg.generateKey();
            throw new Exception("no exception");
        } catch (IllegalStateException e) {
            System.out.println("OK: " + e);
        }

        int[] protocolVersions = {0x0300, 0x0301, 0x0302, 0x0400};
        for (int clientVersion : protocolVersions) {
            for (int serverVersion : protocolVersions) {
                test(kg, clientVersion, serverVersion);
                if (serverVersion >= clientVersion) {
                    break;
                }
            }
        }

        System.out.println("Done.");
    }

    private static void test(KeyGenerator kg,
            int clientVersion, int serverVersion) throws Exception {

        System.out.printf(
                "Testing RSA pre-master secret key generation between " +
                "client (0x%04X) and server(0x%04X)%n",
                clientVersion, serverVersion);
        kg.init(new TlsRsaPremasterSecretParameterSpec(
                                    clientVersion, serverVersion));

        SecretKey key = kg.generateKey();
        byte[] encoded = key.getEncoded();
        if (encoded != null) {  
            if (encoded.length != 48) {
                throw new Exception("length: " + encoded.length);
            }
            int v = versionOf(encoded[0], encoded[1]);
            if (clientVersion != v) {
                if (serverVersion != v || clientVersion >= 0x0302) {
                    throw new Exception(String.format(
                        "version mismatch: (0x%04X) rather than (0x%04X) " +
                        "is used in pre-master secret", v, clientVersion));
                }
                System.out.printf("Use compatible version (0x%04X)%n", v);
            }
            System.out.println("Passed, version matches!");
       } else {
            System.out.println("Raw key material is not extractable");
       }
    }

    private static int versionOf(int major, int minor) {
        return ((major & 0xFF) << 8) | (minor & 0xFF);
    }
}
