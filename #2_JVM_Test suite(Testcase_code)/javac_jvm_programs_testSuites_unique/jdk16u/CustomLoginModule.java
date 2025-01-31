

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.ChoiceCallback;
import javax.security.auth.callback.ConfirmationCallback;
import javax.security.auth.callback.LanguageCallback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

public class CustomLoginModule implements LoginModule {

    static final String HELLO = "Hello";

    private Subject subject;
    private CallbackHandler callbackHandler;
    private boolean loginSucceeded = false;
    private String username;
    private char[] password;

    
    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler,
            Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;

        
        if (options == null) {
            throw new RuntimeException("options is null");
        }

        
        Object o = options.get("username");
        if (o == null) {
            throw new RuntimeException("Custom parameter not passed");
        }
        if (!(o instanceof String)) {
            throw new RuntimeException("Password is not a string");
        }
        username = (String) o;

        o = options.get("password");
        if (o == null) {
            throw new RuntimeException("Custom parameter not passed");
        }
        if (!(o instanceof String)) {
            throw new RuntimeException("Password is not a string");
        }
        password = ((String) o).toCharArray();
    }

    
    @Override
    public boolean login() throws LoginException {
        
        if (callbackHandler == null) {
            throw new LoginException("No CallbackHandler available");
        }

        
        NameCallback name = new NameCallback("username: ", "default");
        PasswordCallback passwd = new PasswordCallback("password: ", false);

        LanguageCallback language = new LanguageCallback();

        TextOutputCallback error = new TextOutputCallback(
                TextOutputCallback.ERROR, "This is an error");
        TextOutputCallback warning = new TextOutputCallback(
                TextOutputCallback.WARNING, "This is a warning");
        TextOutputCallback info = new TextOutputCallback(
                TextOutputCallback.INFORMATION, "This is a FYI");

        TextInputCallback text = new TextInputCallback("Please type " + HELLO,
                "Bye");

        ChoiceCallback choice = new ChoiceCallback("Choice: ",
                new String[] { "pass", "fail" }, 1, true);

        ConfirmationCallback confirmation = new ConfirmationCallback(
                "confirmation: ", ConfirmationCallback.INFORMATION,
                ConfirmationCallback.YES_NO_OPTION, ConfirmationCallback.NO);

        CustomCallback custom = new CustomCallback();

        Callback[] callbacks = new Callback[] {
            choice, info, warning, error, name, passwd, text, language,
            confirmation, custom
        };

        boolean uce = false;
        try {
            callbackHandler.handle(callbacks);
        } catch (UnsupportedCallbackException e) {
            Callback callback = e.getCallback();
            if (custom.equals(callback)) {
                uce = true;
                System.out.println("CustomLoginModule: "
                        + "custom callback not supported as expected");
            } else {
                throw new LoginException("Unsupported callback: " + callback);
            }
        } catch (IOException ioe) {
            throw new LoginException(ioe.toString());
        }

        if (!uce) {
            throw new RuntimeException("UnsupportedCallbackException "
                    + "not thrown");
        }

        if (!HELLO.equals(text.getText())) {
            System.out.println("Text: " + text.getText());
            throw new FailedLoginException("No hello");
        }

        if (!Locale.GERMANY.equals(language.getLocale())) {
            System.out.println("Selected locale: " + language.getLocale());
            throw new FailedLoginException("Achtung bitte");
        }

        String readUsername = name.getName();
        char[] readPassword = passwd.getPassword();
        if (readPassword == null) {
            
            readPassword = new char[0];
        }
        passwd.clearPassword();

        
        if (!username.equals(readUsername)
                || !Arrays.equals(password, readPassword)) {
            loginSucceeded = false;
            throw new FailedLoginException("Username/password is not correct");
        }

        
        int[] selected = choice.getSelectedIndexes();
        if (selected == null || selected.length == 0) {
            throw new FailedLoginException("Nothing selected");
        }

        if (selected[0] != 0) {
            throw new FailedLoginException("Wrong choice: " + selected[0]);
        }

        
        if (confirmation.getSelectedIndex() != ConfirmationCallback.YES) {
            throw new FailedLoginException("Not confirmed: "
                    + confirmation.getSelectedIndex());
        }

        loginSucceeded = true;
        System.out.println("CustomLoginModule: authentication succeeded");
        return true;
    }

    
    @Override
    public boolean commit() throws LoginException {
        if (loginSucceeded) {
            
            Principal principal = new TestPrincipal(username);
            if (!subject.getPrincipals().contains(principal)) {
                subject.getPrincipals().add(principal);
            }
            return true;
        }

        return false;
    }

    
    @Override
    public boolean abort() throws LoginException {
        loginSucceeded = false;
        return true;
    }

    
    @Override
    public boolean logout() throws LoginException {
        loginSucceeded = false;
        boolean removed = subject.getPrincipals().remove(
                new TestPrincipal(username));
        if (!removed) {
            throw new LoginException("Coundn't remove a principal: "
                    + username);
        }
        return true;
    }

    static class TestPrincipal implements Principal {

        private final String name;

        public TestPrincipal(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return("TestPrincipal [name =" + name + "]");
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (!(o instanceof TestPrincipal)) {
                return false;
            }
            TestPrincipal other = (TestPrincipal) o;
            return name != null ? name.equals(other.name) : other.name == null;
        }
    }

    static class CustomCallback implements Callback {}
}
