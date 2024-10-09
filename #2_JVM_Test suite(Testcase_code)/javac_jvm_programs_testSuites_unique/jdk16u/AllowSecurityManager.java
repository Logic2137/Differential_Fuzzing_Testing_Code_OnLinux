



public class AllowSecurityManager {

    public static void main(String args[]) throws Exception {
        String prop = System.getProperty("java.security.manager");
        boolean disallow = "disallow".equals(prop);
        try {
            System.setSecurityManager(new SecurityManager());
            if (disallow) {
                throw new Exception("System.setSecurityManager did not " +
                                    "throw UnsupportedOperationException");
            }
        } catch (UnsupportedOperationException uoe) {
            if (!disallow) {
                throw new Exception("UnsupportedOperationException " +
                                    "unexpectedly thrown by " +
                                    "System.setSecurityManager");
            }
        }
    }
}
