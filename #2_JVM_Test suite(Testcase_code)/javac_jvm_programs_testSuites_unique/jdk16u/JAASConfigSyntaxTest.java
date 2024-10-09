

import javax.security.auth.login.LoginContext;


public class JAASConfigSyntaxTest {

    private static final String TEST_NAME = "JAASConfigSyntaxTest";

    public static void main(String[] args) throws Exception {
        try {
            LoginContext lc = new LoginContext(TEST_NAME);
            lc.login();
            throw new RuntimeException("Test Case Failed, did not get "
                    + "expected exception");
        } catch (Exception ex) {
            if (ex.getMessage().contains("java.io.IOException: "
                    + "Configuration Error:")) {
                System.out.println("Test case passed");
            } else {
                throw new RuntimeException(ex);
            }
        }
    }
}
