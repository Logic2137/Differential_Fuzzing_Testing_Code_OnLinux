import java.util.zip.*;

public class Bounds {

    public static void main(String[] args) throws Exception {
        byte[] b = new byte[0];
        int offset = 4;
        int length = Integer.MAX_VALUE - 3;
        try {
            (new CRC32()).update(b, offset, length);
            throw new RuntimeException("Expected exception not thrown");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
        }
        try {
            (new Deflater()).setDictionary(b, offset, length);
            throw new RuntimeException("Expected exception not thrown");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
        }
        try {
            (new Adler32()).update(b, offset, length);
            throw new RuntimeException("Expected exception not thrown");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
        }
        try {
            (new Deflater()).deflate(b, offset, length);
            throw new RuntimeException("Expected exception not thrown");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
        }
        try {
            (new Inflater()).inflate(b, offset, length);
            throw new RuntimeException("Expected exception not thrown");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
        }
    }
}
