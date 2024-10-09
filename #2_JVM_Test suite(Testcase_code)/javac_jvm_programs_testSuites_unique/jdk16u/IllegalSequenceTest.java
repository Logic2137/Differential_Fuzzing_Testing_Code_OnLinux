


import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class IllegalSequenceTest {
    static final byte[][] illegalSequences = {
        {(byte)0xc0, (byte)0xaf}, 
        {(byte)0xc2, (byte)0xe0}, 
        {(byte)0xc2, (byte)0x80, (byte)0x80}, 
        {(byte)0xe0, (byte)0x80}, 
        {(byte)0xf4, (byte)0x90, (byte)0x80, (byte)0x80}, 
    };

    public static void main(String[] args) throws IOException {
        for (byte[] illegalSec: illegalSequences) {
            try (InputStream is = new ByteArrayInputStream(illegalSec)) {
                ResourceBundle rb = new PropertyResourceBundle(is);
                rb.getString("key");
            } catch (MalformedInputException |
                    UnmappableCharacterException e) {
                
                continue;
            }
            throw new RuntimeException("Excepted exception was not thrown.");
        }
    }
}
