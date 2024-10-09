import java.io.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;

public class NullCharsetName {

    public static void main(String[] args) throws Exception {
        try {
            Charset.forName(null);
        } catch (Exception x) {
            if (x instanceof IllegalArgumentException) {
                System.err.println("Thrown as expected: " + x);
                return;
            }
            throw new Exception("Incorrect exception: " + x.getClass().getName(), x);
        }
        throw new Exception("No exception thrown");
    }
}
