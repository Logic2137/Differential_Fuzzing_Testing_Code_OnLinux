



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;



public class SaveSeparator {

    public static void main(String[] argv) throws IOException {
        
        Properties myProps = new Properties();
        ByteArrayOutputStream myOut = new ByteArrayOutputStream(40);
        myProps.store(myOut, "A test");

        
        String theSeparator = System.getProperty("line.separator");
        String content = myOut.toString();
        if (!content.endsWith(theSeparator))
            throw new RuntimeException("Incorrect Properties line separator.");
    }
}
