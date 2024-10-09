



import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AllPlatforms {
    public static void main(String[] args) throws Exception {
        login("cross-platform",
                "UnixLoginModule", "optional",
                "NTLoginModule", "optional",
                "SolarisLoginModule", "optional");
        try {
            login("windows", "NTLoginModule", "required");
            login("unix", "UnixLoginModule", "required");
            login("solaris", "SolarisLoginModule", "required");
        } catch (Exception e) {
            e.printStackTrace(System.out);
            if (e.toString().contains("UnsatisfiedLinkError")) {
                throw new Exception("This is ugly");
            }
        }
    }

    static void login(String test, String... conf) throws Exception {
        System.out.println("Testing " + test + "...");

        StringBuilder sb = new StringBuilder();
        sb.append("hello {\n");
        for (int i=0; i<conf.length; i+=2) {
            sb.append("    com.sun.security.auth.module." + conf[i]
                    + " " + conf[i+1] + ";\n");
        }
        sb.append("};\n");
        Files.write(Paths.get(test), sb.toString().getBytes());

        
        Configuration.setConfiguration(null);
        System.setProperty("java.security.auth.login.config", test);

        LoginContext lc = new LoginContext("hello");
        lc.login();
        System.out.println(lc.getSubject());
    }
}
