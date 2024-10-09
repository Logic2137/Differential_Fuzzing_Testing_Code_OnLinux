import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Date;
import java.util.Set;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.monitor.CounterMonitor;
import javax.management.monitor.GaugeMonitor;
import javax.management.monitor.Monitor;
import javax.management.monitor.StringMonitor;
import javax.management.remote.JMXPrincipal;
import javax.security.auth.Subject;

public class ThreadPoolAccTest {

    public static class ObservedObject implements ObservedObjectMBean {

        public volatile String principal;

        public Integer getInteger() {
            setPrincipal();
            return 0;
        }

        public Double getDouble() {
            setPrincipal();
            return 0.0;
        }

        public String getString() {
            setPrincipal();
            return "";
        }

        private void setPrincipal() {
            Subject subject = Subject.getSubject(AccessController.getContext());
            Set<JMXPrincipal> principals = subject.getPrincipals(JMXPrincipal.class);
            principal = principals.iterator().next().getName();
        }
    }

    public interface ObservedObjectMBean {

        public Integer getInteger();

        public Double getDouble();

        public String getString();
    }

    public static void main(String[] args) throws Exception {
        ObjectName[] mbeanNames = new ObjectName[6];
        ObservedObject[] monitored = new ObservedObject[6];
        ObjectName[] monitorNames = new ObjectName[6];
        Monitor[] monitor = new Monitor[6];
        String[] principals = { "role1", "role2" };
        String[] attributes = { "Integer", "Double", "String" };
        try {
            echo(">>> CREATE MBeanServer");
            MBeanServer server = MBeanServerFactory.newMBeanServer();
            for (int i = 0; i < 6; i++) {
                mbeanNames[i] = new ObjectName(":type=ObservedObject,instance=" + i);
                monitored[i] = new ObservedObject();
                echo(">>> CREATE ObservedObject = " + mbeanNames[i].toString());
                server.registerMBean(monitored[i], mbeanNames[i]);
                switch(i) {
                    case 0:
                    case 3:
                        monitorNames[i] = new ObjectName(":type=CounterMonitor,instance=" + i);
                        monitor[i] = new CounterMonitor();
                        break;
                    case 1:
                    case 4:
                        monitorNames[i] = new ObjectName(":type=GaugeMonitor,instance=" + i);
                        monitor[i] = new GaugeMonitor();
                        break;
                    case 2:
                    case 5:
                        monitorNames[i] = new ObjectName(":type=StringMonitor,instance=" + i);
                        monitor[i] = new StringMonitor();
                        break;
                }
                echo(">>> CREATE Monitor = " + monitorNames[i].toString());
                server.registerMBean(monitor[i], monitorNames[i]);
                monitor[i].addObservedObject(mbeanNames[i]);
                monitor[i].setObservedAttribute(attributes[i % 3]);
                monitor[i].setGranularityPeriod(500);
                final Monitor m = monitor[i];
                Subject subject = new Subject();
                echo(">>> RUN Principal = " + principals[i / 3]);
                subject.getPrincipals().add(new JMXPrincipal(principals[i / 3]));
                PrivilegedAction<Void> action = new PrivilegedAction<Void>() {

                    public Void run() {
                        m.start();
                        return null;
                    }
                };
                Subject.doAs(subject, action);
            }
            while (!testPrincipals(monitored, monitorNames, monitor, principals)) ;
        } finally {
            for (int i = 0; i < 6; i++) if (monitor[i] != null)
                monitor[i].stop();
        }
    }

    private static boolean testPrincipals(ObservedObject[] monitored, ObjectName[] monitorNames, Monitor[] monitor, String[] principals) throws Exception {
        for (int i = 0; i < 6; i++) {
            String principal = monitored[i].principal;
            String expected = principals[i / 3];
            if (principal == null) {
                echo("Task not submitted " + new Date() + ". RETRY");
                return false;
            }
            echo(">>> Monitor = " + monitorNames[i]);
            echo(">>> ObservedObject = " + monitor[i].getObservedObject());
            echo(">>> ObservedAttribute = " + monitor[i].getObservedAttribute());
            echo(">>> Principal = " + principal);
            if (expected.equals(principal)) {
                echo("\tOK: Got Expected principal");
            } else {
                throw new Exception("Unexpected principal. Got: " + principal + " Expected: " + expected);
            }
        }
        return true;
    }

    private static void echo(String message) {
        System.out.println(message);
    }
}
