



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class BlankLines {
    public static void main(String []args)
    {
        try {
            
            File file = new File("test.properties");

            
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(' ');
            fos.close();

            
            Properties prop1 = new Properties();

            
            
            
            
            
            Properties prop2 = new Properties();
            InputStream is = new FileInputStream(file);
            try {
                prop2.load(is);
            } finally {
                is.close();
            }
            if (!prop1.equals(prop2))
                throw new RuntimeException("Incorrect properties loading.");

            
            file.delete();
        }
        catch(IOException e) {}
    }
}
