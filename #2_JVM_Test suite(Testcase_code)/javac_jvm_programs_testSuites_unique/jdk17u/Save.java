import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Save {

    public static void main(String[] argv) {
        int testSucceeded = 0;
        FileOutputStream myOutput;
        Properties myProperties = new Properties();
        String value = "   spacesfirst";
        myProperties.put("atest", value);
        try {
            myOutput = new FileOutputStream("testout");
            myProperties.store(myOutput, "A test");
            myOutput.close();
            FileInputStream myIn = new FileInputStream("testout");
            Properties myNewProps = new Properties();
            try {
                myNewProps.load(myIn);
            } finally {
                myIn.close();
            }
            String newValue = (String) myNewProps.get("atest");
            if (!newValue.equals(value))
                throw new RuntimeException("Properties does not save leading spaces in values correctly.");
        } catch (IOException e) {
        }
    }
}
