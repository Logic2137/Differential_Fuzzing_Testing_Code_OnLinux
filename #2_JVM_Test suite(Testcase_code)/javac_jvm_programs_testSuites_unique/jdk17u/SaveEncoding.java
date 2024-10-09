import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class SaveEncoding {

    public static void main(String[] argv) {
        int testSucceeded = 0;
        FileOutputStream myOutput;
        Properties myProperties = new Properties();
        myProperties.put("signal", "val\u0019");
        myProperties.put("ABC 10", "value0");
        myProperties.put("\uff10test", "value\u0020");
        myProperties.put("key with spaces", "value with spaces");
        myProperties.put(" special#=key ", "value3");
        try {
            File myFile = new File("testout");
            myFile.delete();
            myOutput = new FileOutputStream("testout");
            myProperties.store(myOutput, "A test");
            myOutput.close();
            FileInputStream inFile = new FileInputStream("testout");
            BufferedReader in = new BufferedReader(new InputStreamReader(inFile));
            String firstLine = "foo";
            while (!firstLine.startsWith("signal")) firstLine = in.readLine();
            inFile.close();
            if (firstLine.length() != 16)
                throw new RuntimeException("Incorrect storage of values < 32.");
            FileInputStream myIn = new FileInputStream("testout");
            Properties myNewProps = new Properties();
            try {
                myNewProps.load(myIn);
            } finally {
                myIn.close();
            }
            if (!myNewProps.equals(myProperties))
                throw new RuntimeException("Properties is not character encoding safe.");
        } catch (IOException e) {
        }
    }
}
