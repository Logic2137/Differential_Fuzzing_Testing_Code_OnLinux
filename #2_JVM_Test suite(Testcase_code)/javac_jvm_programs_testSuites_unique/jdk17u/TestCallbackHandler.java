
package handler;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class TestCallbackHandler implements CallbackHandler {

    private static final String USER_NAME = "testUser";

    private static final String PASSWORD = "testPassword";

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        System.out.println("TestCallbackHandler will get resolved through" + " auth.login.defaultCallbackHandler property.");
        for (Callback callback : callbacks) {
            if (callback instanceof NameCallback) {
                ((NameCallback) callback).setName(USER_NAME);
            } else if (callback instanceof PasswordCallback) {
                ((PasswordCallback) callback).setPassword(PASSWORD.toCharArray());
            } else {
                throw new UnsupportedCallbackException(callback);
            }
        }
    }
}
