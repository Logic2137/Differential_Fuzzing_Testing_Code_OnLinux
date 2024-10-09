import sun.security.krb5.internal.ccache.FileCredentialsCache;

public class DefaultFile {

    public static void main(String[] args) throws Exception {
        if (System.getenv("KRB5CCNAME") != null) {
            return;
        }
        if (System.getProperty("os.name").startsWith("Windows")) {
            return;
        }
        String name = FileCredentialsCache.getDefaultCacheName();
        if (!name.startsWith("/tmp/krb5cc_")) {
            throw new Exception("default name is " + name);
        }
    }
}
