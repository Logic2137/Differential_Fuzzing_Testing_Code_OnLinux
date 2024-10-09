

import java.io.*;
import java.util.*;
import java.security.*;
import javax.security.auth.callback.*;


public class LoginISE {

    public static void main(String[] args) throws Exception {

        Provider p = Security.getProvider("SunPKCS11");
        if (p == null) {
            System.out.println("No un-initialized PKCS11 provider available; skip");
            return;
        }
        if (!(p instanceof AuthProvider)) {
            throw new RuntimeException("Error: expect AuthProvider!");
        }
        AuthProvider ap = (AuthProvider) p;
        if (ap.isConfigured()) {
            throw new RuntimeException("Fail: isConfigured() should return false");
        }
        try {
            ap.login(null, null);
            throw new RuntimeException("Fail: expected ISE not thrown!");
        } catch (IllegalStateException ise) {
            System.out.println("Expected ISE thrown for login call");
        }
        try {
            ap.logout();
            throw new RuntimeException("Fail: expected ISE not thrown!");
        } catch (IllegalStateException ise) {
            System.out.println("Expected ISE thrown for logout call");
        }
        try {
            ap.setCallbackHandler(new PasswordCallbackHandler());
            throw new RuntimeException("Fail: expected ISE not thrown!");
        } catch (IllegalStateException ise) {
            System.out.println("Expected ISE thrown for logout call");
        }

        System.out.println("Test Passed");
    }

    public static class PasswordCallbackHandler implements CallbackHandler {
        public void handle(Callback[] callbacks)
                throws IOException, UnsupportedCallbackException {
        }
    }
}
