import javax.security.auth.callback.Callback;
import javax.security.auth.callback.ConfirmationCallback;
import com.sun.security.auth.callback.TextCallbackHandler;
import java.io.ByteArrayInputStream;

public class Confirm {

    public static void main(String[] args) throws Exception {
        System.setIn(new ByteArrayInputStream("1\n".getBytes()));
        new TextCallbackHandler().handle(new Callback[] { new ConfirmationCallback("Prince", ConfirmationCallback.INFORMATION, new String[] { "To be", "Not to be" }, 0) });
    }
}
