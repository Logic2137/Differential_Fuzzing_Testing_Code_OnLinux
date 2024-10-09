



import java.net.*;

public class HashCode {

    public static void main(String[] args) throws Exception {
        URL url = new URL("http", null, 80, "test");
        int code = url.hashCode();
    }
}
