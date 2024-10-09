

import java.io.InputStream;
import java.net.URL;

public class GetContentType {
    public static void main(String args[]) throws Exception {
        URL url = ClassLoader.getSystemResource("foo/bar");
        InputStream is = (InputStream) url.getContent();
        if (is == null)
            throw new Exception("Failed to get content.");
    }
}
