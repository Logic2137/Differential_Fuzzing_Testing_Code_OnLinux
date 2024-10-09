import java.io.IOException;
import javax.security.sasl.*;
import java.util.*;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

public class Conformance {

    public static void main(String[] args) throws Exception {
        try {
            Sasl.createSaslClient(new String[] { "NTLM" }, "abc", "ldap", "server", new HashMap<String, Object>(), null);
        } catch (SaslException se) {
            System.out.println(se);
        }
        try {
            Sasl.createSaslServer("NTLM", "ldap", "server", new HashMap<String, Object>(), null);
        } catch (SaslException se) {
            System.out.println(se);
        }
        try {
            Sasl.createSaslClient(new String[] { "NTLM" }, "abc", "ldap", "server", null, new CallbackHandler() {

                @Override
                public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                }
            });
        } catch (SaslException se) {
            System.out.println(se);
        }
        try {
            SaslServer saslServer = Sasl.createSaslServer("NTLM", "ldap", "abc", null, new CallbackHandler() {

                @Override
                public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                }
            });
            System.err.println("saslServer = " + saslServer);
            System.err.println("saslServer.isComplete() = " + saslServer.isComplete());
            saslServer.getNegotiatedProperty("prop");
            System.err.println("No IllegalStateException");
        } catch (IllegalStateException se) {
            System.out.println(se);
        }
        try {
            SaslServer saslServer = Sasl.createSaslServer("NTLM", "ldap", "abc", null, new CallbackHandler() {

                @Override
                public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                }
            });
            System.err.println("saslServer = " + saslServer);
            System.err.println("saslServer.isComplete() = " + saslServer.isComplete());
            saslServer.getAuthorizationID();
            System.err.println("No IllegalStateException");
        } catch (IllegalStateException se) {
            System.out.println(se);
        }
        try {
            SaslServer saslServer = Sasl.createSaslServer("NTLM", "ldap", "abc", null, new CallbackHandler() {

                @Override
                public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                }
            });
            System.err.println("saslServer = " + saslServer);
            System.err.println("saslServer.isComplete() = " + saslServer.isComplete());
            saslServer.wrap(new byte[0], 0, 0);
            System.err.println("No IllegalStateException");
        } catch (IllegalStateException se) {
            System.out.println(se);
        }
    }
}
