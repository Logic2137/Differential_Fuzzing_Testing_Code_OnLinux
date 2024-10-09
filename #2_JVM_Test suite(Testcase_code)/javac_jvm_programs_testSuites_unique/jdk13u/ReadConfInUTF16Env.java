import java.security.Security;

public class ReadConfInUTF16Env {

    public static void main(String[] argv) {
        String name = Security.getProvider("OracleUcrypto").getName();
        System.out.println(name);
    }
}
