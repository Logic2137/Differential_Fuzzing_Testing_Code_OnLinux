



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;

public class WrongStoreType {
    public static void main(String ... args) throws Exception {

        char[] password = "changeit".toCharArray();
        KeyStore ks = KeyStore.getInstance("PKCS12");

        ks.load(null, null);
        System.out.println(ks.getType());

        Files.createFile(Path.of("emptyfile"));
        try (InputStream in = new FileInputStream("emptyfile")) {
            ks.load(in, password);
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println(ks.getType());
        try (OutputStream out = new FileOutputStream("newfile")) {
            ks.store(out, password);
        }

        ks = KeyStore.getInstance(new File("newfile"), password);
        String type = ks.getType();
        if (!type.equalsIgnoreCase("PKCS12")) {
            throw new Exception("see storetype " + type);
        }
    }
}
