


public class TestSJIS0213_SM {
    public static void main(String[] args) throws Throwable {
        SecurityManager sm = System.getSecurityManager();
        if (sm == null) {
            System.setSecurityManager(new SecurityManager());
        }
        java.nio.charset.Charset.forName("SJIS_0213");
    }
}
