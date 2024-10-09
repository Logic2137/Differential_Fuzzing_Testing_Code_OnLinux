



import sun.security.krb5.Config;

public class Semicolon {
    public static void main(String[] args) throws Throwable {
        System.setProperty("java.security.krb5.conf",
                System.getProperty("test.src", ".") + "/comments.conf");
        Config config = Config.getInstance();
        config.getBooleanObject("section", "value");
    }
}
