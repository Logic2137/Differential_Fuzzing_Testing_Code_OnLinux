

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import java.util.Hashtable;


public class AppletIsNotUsed {

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws NamingException {

        testWith(Context.APPLET);
        testWith("java.naming.applet");

    }

    private static void testWith(String appletProperty) throws NamingException {
        Hashtable<Object, Object> env = new Hashtable<>();
        
        
        env.put(appletProperty, new Object());
        
        
        Context ctx = new InitialContext(env);
        boolean threw = true;
        try {
            ctx.lookup("whatever");
            threw = false;
        } catch (NoInitialContextException e) {
            String m = e.getMessage();
            if (m == null || m.contains("applet"))
                throw new RuntimeException("The exception message is incorrect", e);
        } catch (Throwable t) {
            throw new RuntimeException(
                    "The test was supposed to catch NoInitialContextException" +
                            " here, but caught: " + t.getClass().getName(), t);
        } finally {
            ctx.close();
        }

        if (!threw)
            throw new RuntimeException("The test was supposed to catch NoInitialContextException here");
    }
}
