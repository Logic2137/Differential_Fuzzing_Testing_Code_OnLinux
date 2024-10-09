import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class OtherResources {

    public static void main(String[] args) throws IOException {
        try {
            Class.forName("java.awt.Component");
            throw new RuntimeException("Need to run with --limit-modules java.base");
        } catch (ClassNotFoundException expected) {
        }
        URL url = new URL("jrt:/java.desktop/java/awt/Component.class");
        URLConnection uc = url.openConnection();
        System.out.println(uc.getInputStream());
    }
}
