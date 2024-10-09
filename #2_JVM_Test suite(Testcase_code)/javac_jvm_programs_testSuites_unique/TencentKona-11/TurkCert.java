



import java.util.*;
import java.util.jar.*;
import java.security.cert.*;
import java.io.*;

public class TurkCert {
    public static void main(String[] args) throws Exception{
        Locale reservedLocale = Locale.getDefault();
        try {
            Locale.setDefault(new Locale("TR", "tr"));
            File f = new File(System.getProperty("test.src","."), "test.jar");
            try (JarFile jf = new JarFile(f, true)) {
                JarEntry je = (JarEntry)jf.getEntry("test.class");
                try (InputStream is = jf.getInputStream(je)) {
                    byte[] b = new byte[1024];
                    while (is.read(b) != -1) {
                    }
                }
                if (je.getCertificates() == null) {
                    throw new Exception("Null certificate for test.class.");
                }
            }
        } finally {
            
            Locale.setDefault(reservedLocale);
        }
    }
}
