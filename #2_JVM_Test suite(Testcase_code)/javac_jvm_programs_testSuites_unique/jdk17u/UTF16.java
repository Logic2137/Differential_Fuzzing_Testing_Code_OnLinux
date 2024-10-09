import java.io.*;

public class UTF16 {

    private static byte[] array = new byte[20000];

    private static void go(String cs) throws Exception {
        InputStream is = new ByteArrayInputStream(array);
        Reader r = new InputStreamReader(is, "UTF-16LE");
        r.read();
    }

    public static void main(String[] args) throws Exception {
        go("UTF-16");
        go("UTF-16LE");
        go("UTF-16BE");
    }
}
