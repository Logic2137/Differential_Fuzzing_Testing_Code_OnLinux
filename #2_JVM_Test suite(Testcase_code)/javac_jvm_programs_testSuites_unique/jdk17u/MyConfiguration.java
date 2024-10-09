import java.util.HashMap;
import javax.security.auth.login.AppConfigurationEntry;
import static javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag.OPTIONAL;
import static javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag.REQUIRED;
import static javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag.SUFFICIENT;
import javax.security.auth.login.Configuration;

public class MyConfiguration extends Configuration {

    private static final AppConfigurationEntry[] ptAE = new AppConfigurationEntry[2];

    private static final HashMap<String, String> map = new HashMap<>();

    private boolean optionOrder = false;

    public MyConfiguration() {
        setupConfiguration();
    }

    public MyConfiguration(boolean optionOrder) {
        this.optionOrder = optionOrder;
        setupConfiguration();
    }

    private void setupConfiguration() {
        ptAE[0] = new AppConfigurationEntry("SmartLoginModule", optionOrder ? OPTIONAL : REQUIRED, map);
        ptAE[1] = new AppConfigurationEntry("DummyLoginModule", optionOrder ? SUFFICIENT : REQUIRED, map);
    }

    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(String applicationName) {
        if (applicationName.equals("PT")) {
            return ptAE;
        } else {
            return null;
        }
    }
}
