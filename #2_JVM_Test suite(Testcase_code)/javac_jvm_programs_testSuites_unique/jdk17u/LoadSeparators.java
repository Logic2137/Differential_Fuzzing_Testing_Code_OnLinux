import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class LoadSeparators {

    public static void main(String[] argv) throws Exception {
        try {
            File propFile = new File("testout");
            propFile.delete();
            FileOutputStream myOut = new FileOutputStream(propFile);
            String testProperty = "Test3==";
            myOut.write(testProperty.getBytes());
            myOut.close();
            FileInputStream myIn = new FileInputStream("testout");
            Properties myNewProps = new Properties();
            try {
                myNewProps.load(myIn);
            } finally {
                myIn.close();
            }
            String equalSign = myNewProps.getProperty("Test3");
            propFile.delete();
            if (!equalSign.equals("="))
                throw new Exception("Cannot read key-value separators.");
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
