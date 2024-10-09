





import java.net.MalformedURLException;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerFactory;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

public class NotSerializableNotifTest {
    private static final MBeanServer mbeanServer = MBeanServerFactory.createMBeanServer();
    private static ObjectName emitter;

    private static String[] protocols = new String[] {"rmi", "iiop", "jmxmp"};

    private static final int sentNotifs = 10;

    public static void main(String[] args) throws Exception {
        System.out.println(">>> Test to send a not serializable notification");

        emitter = new ObjectName("Default:name=NotificationEmitter");
        mbeanServer.registerMBean(new NotificationEmitter(), emitter);

        for (int i = 0; i < protocols.length; i++) {
            test(protocols[i]);
        }

        System.out.println(">>> Test passed");
    }


    private static void test(String proto) throws Exception {
        System.out.println("\n>>> Test for protocol " + proto);

        JMXServiceURL url = new JMXServiceURL(proto, null, 0);

        System.out.println(">>> Create a server: "+url);

        JMXConnectorServer server = null;
        try {
            server = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbeanServer);
        } catch (MalformedURLException e) {
            System.out.println("System does not recognize URL: " + url +
                               "; ignoring");
            return;
        }

        server.start();

        url = server.getAddress();

        System.out.println(">>> Creating a client connectint to: "+url);
        JMXConnector conn = JMXConnectorFactory.connect(url, null);
        MBeanServerConnection client = conn.getMBeanServerConnection();

        
        Listener listener = new Listener();
        client.addNotificationListener(emitter, listener, null, null);

        
        Object[] params = new Object[] {new Integer(1)};
        String[] signatures = new String[] {"java.lang.Integer"};
        client.invoke(emitter, "sendNotserializableNotifs", params, signatures);

        
        client.removeNotificationListener(emitter, listener);
        listener = new Listener();
        client.addNotificationListener(emitter, listener, null, null);

        
        params = new Object[] {new Integer(sentNotifs)};
        client.invoke(emitter, "sendNotifications", params, signatures);

        
        synchronized (listener) {
            while (listener.received() < sentNotifs) {
                listener.wait(); 

            }
        }

        
        client.removeNotificationListener(emitter, listener);

        conn.close();
        server.stop();
    }





    private static class Listener implements NotificationListener {
        public void handleNotification(Notification notif, Object handback) {
            synchronized (this) {
                if(++receivedNotifs == sentNotifs) {
                    this.notifyAll();
                }
            }
        }

        public int received() {
            return receivedNotifs;
        }

        private int receivedNotifs = 0;
    }

    public static class NotificationEmitter extends NotificationBroadcasterSupport
        implements NotificationEmitterMBean {

        public MBeanNotificationInfo[] getNotificationInfo() {
            final String[] ntfTypes = {myType};

            final MBeanNotificationInfo[] ntfInfoArray  = {
                new MBeanNotificationInfo(ntfTypes,
                                          "javax.management.Notification",
                                          "Notifications sent by the NotificationEmitter")};

            return ntfInfoArray;
        }

        
        public void sendNotserializableNotifs(Integer nb) {

            Notification notif;
            for (int i=1; i<=nb.intValue(); i++) {
                notif = new Notification(myType, this, i);

                notif.setUserData(new Object());
                sendNotification(notif);
            }
        }

        
        public void sendNotifications(Integer nb) {
            Notification notif;
            for (int i=1; i<=nb.intValue(); i++) {
                notif = new Notification(myType, this, i);

                sendNotification(notif);
            }
        }

        private final String myType = "notification.my_notification";
    }

    public interface NotificationEmitterMBean {
        public void sendNotifications(Integer nb);

        public void sendNotserializableNotifs(Integer nb);
    }
}
