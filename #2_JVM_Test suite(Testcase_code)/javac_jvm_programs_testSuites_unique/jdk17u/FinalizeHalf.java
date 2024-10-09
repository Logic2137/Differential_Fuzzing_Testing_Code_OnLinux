import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.ProviderException;
import java.security.Security;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.List;

public class FinalizeHalf {

    static int failures = 0;

    public static void main(String[] args) throws Throwable {
        List<Consumer<Key>> methods = new ArrayList<>();
        methods.add((Key k) -> k.getEncoded());
        methods.add((Key k) -> k.toString());
        for (String algo : new String[] { "DiffieHellman", "DSA", "RSA" }) {
            for (Provider provider : Security.getProviders()) {
                for (boolean priv : new boolean[] { true, false }) {
                    for (Consumer<Key> method : methods) {
                        test(algo, provider, priv, method);
                    }
                }
            }
        }
        if (failures > 0) {
            throw new RuntimeException(failures + " test(s) failed.");
        }
    }

    static void test(String algo, Provider provider, boolean priv, Consumer<Key> method) throws Exception {
        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance(algo, provider);
        } catch (NoSuchAlgorithmException nsae) {
            return;
        }
        System.out.println("Checking " + provider.getName() + ", " + algo);
        KeyPair pair = generator.generateKeyPair();
        Key key = priv ? pair.getPrivate() : pair.getPublic();
        pair = null;
        for (int i = 0; i < 32; ++i) {
            System.gc();
        }
        try {
            method.accept(key);
        } catch (ProviderException pe) {
            failures++;
        }
    }
}
