import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivilegedAction;
import java.util.List;

public class UnixPrincipalHashCode {

    public static void main(java.lang.String[] args) throws Exception {
        Files.write(Path.of("uphc.conf"), List.of("entry {", "    com.sun.security.auth.module.UnixLoginModule required;", "};"));
        LoginContext lc = new LoginContext("entry");
        lc.login();
        Subject subject = lc.getSubject();
        PrivilegedAction action = () -> {
            System.out.println(subject);
            return null;
        };
        Subject.doAsPrivileged(subject, action, null);
    }
}
