



import com.sun.security.auth.callback.TextCallbackHandler;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.security.auth.callback.*;

public class NPE {
    public static void main(String args[]) throws Exception {
        System.setIn(new ByteArrayInputStream(new byte[0]));

        try {
            new TextCallbackHandler().handle(new Callback[]  {
                new NameCallback("Name: ") }
            );
        } catch (IOException ioe) {
            
        }
   }
}
