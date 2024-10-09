

import java.security.Principal;
import java.util.Arrays;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;


public class SmartLoginModule implements LoginModule {

    
    private Subject subject;
    private CallbackHandler callbackHandler;

    
    private boolean succeeded = false;
    private boolean commitSucceeded = false;

    
    private String username;
    private char[] password;

    
    
    private String myUsername;
    private char[] myPassword;
    private String header;

    
    private SamplePrincipal userPrincipal;

    public SmartLoginModule() {
        this("testUser",
                new char[]{'t', 'e', 's', 't', 'P', 'a', 's', 's',
                    'w', 'o', 'r', 'd', '1'},
                "SmartLoginModule1: ");
    }

    public SmartLoginModule(String userName, char[] password, String header) {
        myUsername = userName;
        myPassword = password;
        this.header = header;
    }

    @Override
    public boolean abort() throws LoginException {
        if (!succeeded) {
            return false;
        } else if (succeeded && !commitSucceeded) {
            
            succeeded = false;
            username = null;
            password = null;
            userPrincipal = null;
        } else {
            
            
            logout();
        }
        return true;
    }

    @Override
    public boolean commit() throws LoginException {
        if (!succeeded) {
            return false;
        } else {
            
            
            userPrincipal = new SamplePrincipal(username);
            if (!subject.getPrincipals().contains(userPrincipal)) {
                subject.getPrincipals().add(userPrincipal);
            }
            
            username = null;
            password = null;
            commitSucceeded = true;
            return true;
        }
    }

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler,
            Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
    }

    @Override
    public boolean login() throws LoginException {
        if (callbackHandler == null) {
            throw new LoginException("Error: no CallbackHandler available to "
                    + "garner authentication information from the user");
        }

        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback(header + "user name: ");
        callbacks[1] = new PasswordCallback(header + "password: ", false);

        try {
            callbackHandler.handle(callbacks);
            username = ((NameCallback) callbacks[0]).getName();
            char[] tmpPassword
                    = ((PasswordCallback) callbacks[1]).getPassword();
            if (tmpPassword == null) {
                tmpPassword = new char[0];
            }
            password = new char[tmpPassword.length];
            System.arraycopy(tmpPassword, 0, password, 0, tmpPassword.length);
            ((PasswordCallback) callbacks[1]).clearPassword();
        } catch (java.io.IOException ioe) {
            throw (LoginException) new LoginException().initCause(ioe);
        } catch (UnsupportedCallbackException uce) {
            throw new LoginException("Error: " + header
                    + uce.getCallback().toString()
                    + " not available to garner authentication information "
                    + "from the user");
        }

        
        if (username.equals(myUsername)
                && Arrays.equals(password, myPassword)) {
            System.out.println("\t\t" + header + " authentication succeeded");
            succeeded = true;
            return true;
        } else {
            
            System.out.println("\t\t" + header + " authentication failed");
            printDebugInfo();
            succeeded = false;
            username = null;
            password = null;
            throw new FailedLoginException("User Name or Password Incorrect");
        }
    }

    @Override
    public boolean logout() throws LoginException {
        subject.getPrincipals().remove(userPrincipal);
        succeeded = false;
        succeeded = commitSucceeded;
        username = null;
        password = null;
        userPrincipal = null;
        return true;
    }

    
    private void printDebugInfo() {
        System.out.println("\t\t" + header + " correct user name: "
                + myUsername);
        System.out.println("\t\t" + header + " user entered user name: "
                + username);
        System.out.print("\t\t" + header + " correct password: ");
        for (char c : myPassword) {
            System.out.print(c);
        }
        System.out.println();
        System.out.print("\t\t" + header + " user entered password: ");
        for (char c : password) {
            System.out.print(c);
        }
        System.out.println();
    }
}

class SamplePrincipal implements Principal, java.io.Serializable {

    
    private String name;

    
    public SamplePrincipal(String name) {
        if (name == null) {
            throw new NullPointerException("illegal null input");
        }

        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "SamplePrincipal:  " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (this == o) {
            return true;
        }

        if (!(o instanceof SamplePrincipal)) {
            return false;
        }
        SamplePrincipal that = (SamplePrincipal) o;

        return this.getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
