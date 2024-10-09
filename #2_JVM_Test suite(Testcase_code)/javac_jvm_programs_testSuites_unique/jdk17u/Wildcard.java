import java.lang.RuntimePermission;
import java.net.NetPermission;
import java.sql.SQLPermission;
import java.util.PropertyPermission;
import javax.net.ssl.SSLPermission;

public class Wildcard {

    public static void main(String[] args) throws Exception {
        wildcard("*java");
        wildcard("java*");
        wildcard("ja*va");
    }

    private static void wildcard(String wildcard) throws Exception {
        new RuntimePermission(wildcard);
        new NetPermission(wildcard);
        new SQLPermission(wildcard);
        new PropertyPermission(wildcard, "read");
        new SSLPermission(wildcard);
    }
}
