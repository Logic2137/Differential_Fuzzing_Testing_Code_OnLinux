import java.security.*;

public class ExportPrivateKeyNoPwd {

    public static final void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new Exception("ExportPrivateKeyNoPwd: must supply name of a keystore entry");
        }
        String alias = args[0];
        KeyStore ks = KeyStore.getInstance("KeychainStore");
        System.out.println("ExportPrivateKeyNoPwd: loading keychains...");
        ks.load(null, null);
        System.out.println("ExportPrivateKeyNoPwd: exporting key...");
        Key key = ks.getKey(alias, null);
        if (key instanceof PrivateKey) {
            System.out.println("ExportPrivateKeyNoPwd: exported " + key.getAlgorithm() + " private key from '" + alias + "'");
        } else {
            throw new Exception("Error exporting private key from keychain");
        }
    }
}
