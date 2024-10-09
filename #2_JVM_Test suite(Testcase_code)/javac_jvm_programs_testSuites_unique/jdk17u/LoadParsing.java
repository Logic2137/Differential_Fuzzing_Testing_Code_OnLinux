import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class LoadParsing {

    public static void main(String[] argv) throws Exception {
        File f = new File(System.getProperty("test.src", "."), "input.txt");
        FileInputStream myIn = new FileInputStream(f);
        Properties myProps = new Properties();
        int size = 0;
        try {
            myProps.load(myIn);
        } finally {
            myIn.close();
        }
        if (!myProps.getProperty("key1").equals("value1"))
            throw new RuntimeException("Bad comment parsing");
        if (!myProps.getProperty("key2").equals("abc\\defg\\"))
            throw new RuntimeException("Bad slash parsing");
        if (!myProps.getProperty("key3").equals("value3"))
            throw new RuntimeException("Adds spaces to key");
        if (!myProps.getProperty("key4").equals(":value4"))
            throw new RuntimeException("Bad separator parsing");
        if (myProps.getProperty("#") != null)
            throw new RuntimeException("Does not recognize comments with leading spaces");
        if ((size = myProps.size()) != 4)
            throw new RuntimeException("Wrong number of keys in Properties; expected 4, got " + size + ".");
    }
}
