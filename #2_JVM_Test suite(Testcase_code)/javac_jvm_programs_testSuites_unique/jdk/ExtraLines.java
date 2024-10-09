


import sun.security.krb5.Config;
import java.nio.file.*;
import java.util.Objects;

public class ExtraLines {
    public static void main(String[] args) throws Exception {
        Path base = Paths.get("krb5.conf");
        Path include = Paths.get("included.conf");
        String baseConf = "include " + include.toAbsolutePath().toString()
                + "\n[x]\na = b\n";
        String includeConf = "[y]\nc = d\n";
        Files.write(include, includeConf.getBytes());
        Files.write(base, baseConf.getBytes());

        System.setProperty("java.security.krb5.conf", base.toString());
        Config.refresh();

        if (!Objects.equals(Config.getInstance().get("x", "a"), "b")) {
            throw new Exception("Failed");
        }
    }
}
