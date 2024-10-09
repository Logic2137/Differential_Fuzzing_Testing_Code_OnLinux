



import java.net.URL;
import java.security.CodeSigner;
import java.security.cert.Certificate;
import java.util.jar.*;


public class ScanSignedJar {

    private static final String JAR_LOCATION =
        "file:" +
        System.getProperty("test.src", ".") +
        System.getProperty("file.separator") +
        "signed.jar";

    public static void main(String[] args) throws Exception {

        System.out.println("Opening " + JAR_LOCATION + "...");
        JarInputStream inStream =
            new JarInputStream(new URL(JAR_LOCATION).openStream(), true);
        JarEntry entry;
        byte[] buffer = new byte[1024];

        while ((entry = inStream.getNextJarEntry()) != null) {

            
            while(inStream.read(buffer) != -1)
                ;

            String name = entry.getName();
            long size = entry.getSize();
            Certificate[] certificates = entry.getCertificates();
            CodeSigner[] signers = entry.getCodeSigners();

            if (signers == null && certificates == null) {
                System.out.println("[unsigned]\t" + name + "\t(" + size +
                    " bytes)");
                if (name.equals("Count.class")) {
                    throw new Exception("Count.class should be signed");
                }
            } else if (signers != null && certificates != null) {
                System.out.println("[" + signers.length +
                    (signers.length == 1 ? " signer" : " signers") + "]\t" +
                    name + "\t(" + size + " bytes)");
            } else {
                System.out.println("[*ERROR*]\t" + name + "\t(" + size +
                    " bytes)");
                throw new Exception("Cannot determine whether the entry is " +
                    "signed or unsigned (signers[] doesn't match certs[]).");
            }
        }
    }
}
