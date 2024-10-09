



import sun.awt.OSInfo;

public class TestOSVersion {

    private static final String WIN_VISTA_VERSION = "6.0";

    public static void main(String[] arg) {

        String oSVersion = System.getProperty("os.version");
        if (WIN_VISTA_VERSION.equals(oSVersion)) {
            if (OSInfo.getWindowsVersion().toString().equals("6.1") ) {
                throw new RuntimeException("Incorrect Windows VISTA OS version "
                        + "in OSInfo");
            }
        }
    }
}
