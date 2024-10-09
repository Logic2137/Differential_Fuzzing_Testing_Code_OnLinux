



import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.jar.*;

public class JarURLConnectionUseCaches {
    public static void main( String[] args ) throws IOException {
        JarOutputStream out = new JarOutputStream(
                new FileOutputStream("usecache.jar"));
        out.putNextEntry(new JarEntry("test.txt"));
        out.write("Test txt file".getBytes());
        out.closeEntry();
        out.close();

        URL url = new URL("jar:"
            + new File(".").toURI().toString()
            + "/usecache.jar!/test.txt");

        JarURLConnection c1 = (JarURLConnection)url.openConnection();
        c1.setDefaultUseCaches( false );
        c1.setUseCaches( true );
        c1.connect();

        JarURLConnection c2 = (JarURLConnection)url.openConnection();
        c2.setDefaultUseCaches( false );
        c2.setUseCaches( true );
        c2.connect();

        c1.getInputStream().close();
        c2.getInputStream().read();
        c2.getInputStream().close();
    }
}
