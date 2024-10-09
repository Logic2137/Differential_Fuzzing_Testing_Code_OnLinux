import java.beans.EventHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import javax.swing.SwingUtilities;

public class Test6277266 {

    public static void main(String[] args) {
        System.setSecurityManager(new SecurityManager());
        try {
            SwingUtilities.invokeAndWait((Runnable) Proxy.newProxyInstance(null, new Class[] { Runnable.class }, new EventHandler(Test6277266.class, "getProtectionDomain", null, null)));
            throw new Error("SecurityException expected");
        } catch (SecurityException exception) {
            return;
        } catch (InvocationTargetException exception) {
            if (exception.getCause() instanceof SecurityException) {
                return;
            }
            throw new Error("unexpected exception", exception);
        } catch (InterruptedException exception) {
            throw new Error("unexpected exception", exception);
        }
    }
}
