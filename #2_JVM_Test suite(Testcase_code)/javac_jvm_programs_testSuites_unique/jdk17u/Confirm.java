import javax.security.auth.callback.Callback;
import javax.security.auth.callback.ConfirmationCallback;
import com.sun.security.auth.callback.TextCallbackHandler;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Confirm {

    public static void main(String[] args) throws Exception {
        InputStream in = System.in;
        try {
            System.setIn(new ByteArrayInputStream("1\n".getBytes()));
            new TextCallbackHandler().handle(new Callback[] { new ConfirmationCallback("Prince", ConfirmationCallback.INFORMATION, new String[] { "To be", "Not to be" }, 0) });
            System.setIn(new ByteArrayInputStream("-1\n".getBytes()));
            new TextCallbackHandler().handle(new Callback[] { new ConfirmationCallback(ConfirmationCallback.INFORMATION, ConfirmationCallback.OK_CANCEL_OPTION, ConfirmationCallback.OK) });
        } finally {
            System.setIn(in);
        }
    }
}
