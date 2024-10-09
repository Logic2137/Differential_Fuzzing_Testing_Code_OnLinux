import javax.security.auth.login.*;
import com.sun.security.auth.login.*;

public class Override {

    public static void main(String[] args) throws Exception {
        ConfigFile c = new ConfigFile();
        AppConfigurationEntry[] good = c.getAppConfigurationEntry("good");
        AppConfigurationEntry[] bad = c.getAppConfigurationEntry("bad");
        if (good != null && bad == null) {
            System.out.println("test passed");
        } else {
            if (good == null) {
                throw new SecurityException("could not get good entries");
            } else {
                throw new SecurityException("incorrectly got bad entries");
            }
        }
    }
}
