import javax.naming.*;
import java.util.Hashtable;

public class NoApplet {

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws NamingException {
        Hashtable<Object, Object> env = new Hashtable<>();
        env.put(Context.APPLET, new Object());
        Context ctxt = new InitialContext(env);
        ctxt.close();
    }
}
