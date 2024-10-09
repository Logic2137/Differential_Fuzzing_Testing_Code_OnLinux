


import sun.security.krb5.Config;

public class ParseConfig {
    public static void main(String[] args) throws Exception {
        System.setProperty("java.security.krb5.conf",
                System.getProperty("test.src", ".") + "/krb5.conf");
        Config config = Config.getInstance();
        config.listTable();

        String sample = "kdc.example.com kdc2.example.com";
        for ( int i = 0; i < 4; i++ ) {
            String expected = config.getAll("realms", "EXAMPLE_" + i + ".COM", "kdc");
            if (!sample.equals(expected)) {
                throw new Exception("krb5.conf: unexpected kdc value \"" +
                        expected + "\"");
            }
        }

        
        config.get("empty1", "NOVAL.COM");
        config.get("empty2", "NOVAL.COM");
        config.get("quote1", "NOVAL.COM");
        config.get("quote2", "NOVAL.COM");
    }
}
