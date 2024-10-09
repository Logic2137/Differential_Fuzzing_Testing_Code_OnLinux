



import com.sun.security.auth.callback.TextCallbackHandler;
import javax.security.auth.callback.*;

public class Password {
   public static void main(String args[]) throws Exception {
        TextCallbackHandler h = new TextCallbackHandler();
        PasswordCallback nc = new PasswordCallback("Invisible: ", false);
        PasswordCallback nc2 = new PasswordCallback("Visible: ", true);

        System.out.println("Two passwords will be prompted for. The first one " +
                "should have echo off, the second one on. Otherwise, this test fails");
        Callback[] callbacks = { nc, nc2 };
        h.handle(callbacks);
        System.out.println("You input " + new String(nc.getPassword()) +
                " and " + new String(nc2.getPassword()));
   }
}
