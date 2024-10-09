


import java.security.*;
import java.io.*;
import java.lang.reflect.*;

public class ConfigShortPath {

    private static final String[] winConfigNames = {
        "csp.cfg", "cspSpace.cfg", "cspQuotedPath.cfg"
    };
    private static final String[] solConfigNames = {
        "cspPlus.cfg"
    };

    public static void main(String[] args) throws Exception {
        Provider p = Security.getProvider("SunPKCS11");
        if (p == null) {
            System.out.println("Skipping test - no PKCS11 provider available");
            return;
        }

        String osInfo = System.getProperty("os.name", "");
        String[] configNames = (osInfo.contains("Windows")?
            winConfigNames : solConfigNames);

        String testSrc = System.getProperty("test.src", ".");
        for (int i = 0; i < configNames.length; i++) {
            String configFile = testSrc + File.separator + configNames[i];

            System.out.println("Testing against " + configFile);
            try {
                p.configure(configFile);
            } catch (InvalidParameterException ipe) {
                ipe.printStackTrace();
                Throwable cause = ipe.getCause();
                
                if (cause.getClass().getName().equals
                        ("sun.security.pkcs11.ConfigurationException")) {
                    
                    if (cause.getMessage().indexOf("Unexpected") != -1) {
                        throw (ProviderException) cause;
                    }
                }
            } catch (ProviderException pe) {
                pe.printStackTrace();
                if (pe.getCause() instanceof IOException) {
                    
                    System.out.println("Pass: config parsed ok");
                    continue;
                }
            }
        }
    }
}
