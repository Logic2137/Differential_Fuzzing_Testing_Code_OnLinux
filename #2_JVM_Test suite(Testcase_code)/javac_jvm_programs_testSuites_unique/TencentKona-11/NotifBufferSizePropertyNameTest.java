



import java.io.IOException;
import java.util.*;

import javax.management.*;
import javax.management.remote.*;


public class NotifBufferSizePropertyNameTest {

    private static ObjectName oname;
    private static JMXServiceURL url;
    private final static NotificationListener listener = new NotificationListener() {
                public void handleNotification(Notification n, Object hb) {
                    
                }
            };

    public static void main(String[] args) throws Exception {
        System.out.println(
           "Verify the property name specifying the server notification buffer size.");

        oname = new ObjectName ("Default:name=NotificationEmitter");
        url = new JMXServiceURL("rmi", null, 0);
        Map env = new HashMap(2);

        System.out.println("Test the new property name.");
        env.put("jmx.remote.x.notification.buffer.size", String.valueOf(bufferSize));
        test(env);

        System.out.println("Test the old property name.");
        env.remove("jmx.remote.x.notification.buffer.size");
        env.put("jmx.remote.x.buffer.size", String.valueOf(bufferSize));
        test(env);

        System.out.println("Test that the new property name overwrite the old one.");
        env.put("jmx.remote.x.notification.buffer.size", String.valueOf(bufferSize));
        env.put("jmx.remote.x.buffer.size", String.valueOf(bufferSize*6));
        test(env);

        System.out.println("Test the old property name on system.");
        System.setProperty("jmx.remote.x.buffer.size", String.valueOf(bufferSize));
        test(null);

        System.out.println(
             "Test that the new property name overwrite the old one on system.");
        System.setProperty("jmx.remote.x.notification.buffer.size",
                           String.valueOf(bufferSize));
        System.setProperty("jmx.remote.x.buffer.size", String.valueOf(bufferSize*6));
        test(null);
    }


    private static void test(Map env) throws Exception {
        final MBeanServer mbs = MBeanServerFactory.newMBeanServer();

        mbs.registerMBean(new NotificationEmitter(), oname);
        JMXConnectorServer server = JMXConnectorServerFactory.newJMXConnectorServer(
                                                                               url,
                                                                               env,
                                                                               mbs);
        server.start();

        JMXServiceURL addr = server.getAddress();
        JMXConnector client = JMXConnectorFactory.connect(addr);
        client.getMBeanServerConnection().addNotificationListener(oname,
                                                                  listener,
                                                                  null,
                                                                  null);

        Thread.sleep(10); 
        weakNotifs.clear();

        
        mbs.invoke(oname, "sendNotifications",
                   new Object[] {new Integer(toSend)},
                   new String[] {"java.lang.Integer"});

        client.close();
        client = null;

        
        for(int i=0; i<200; i++) {
            if (weakNotifs.keySet().size() > bufferSize) {
                Thread.sleep(10);
                System.gc();
            } else {
                break;
            }
        }

        
        if (weakNotifs.keySet().size() != bufferSize) {
            throw new RuntimeException("The buffer size is not correctly specified."+
                   "\nExpected to be <= "+bufferSize+", but got "+weakNotifs.keySet().size());
        }

        server.stop();
        server = null;
    }




    public static class NotificationEmitter extends NotificationBroadcasterSupport
        implements NotificationEmitterMBean {

        public void sendNotifications(Integer nb) {
            Notification notif;
            for (int i=1; i<=nb.intValue(); i++) {
                notif = new Notification("MyType", this, i);
                weakNotifs.put(notif, null);
                sendNotification(notif);
            }
        }
    }

    public interface NotificationEmitterMBean {
        public void sendNotifications(Integer nb);
    }

    private static final int toSend = 20;
    private static final int bufferSize = 10;
    private static WeakHashMap weakNotifs = new WeakHashMap(toSend);

}
