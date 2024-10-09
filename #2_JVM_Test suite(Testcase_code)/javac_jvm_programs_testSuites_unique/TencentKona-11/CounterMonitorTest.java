



import javax.management.*;
import javax.management.monitor.*;

public class CounterMonitorTest implements NotificationListener {

    
    private Number threshold = new Integer(2);

    
    private Number modulus = new Integer(7);

    
    private boolean differenceModeFlag = true;

    
    private boolean notifyFlag = true;

    
    private int granularityperiod = 10;

    
    private volatile int derivedGauge = 2;

    
    private volatile boolean messageReceived = false;

    private volatile Object observedValue = null;

    
    public class StdObservedObject implements StdObservedObjectMBean {
        public Object getNbObjects() {
            echo(">>> StdObservedObject.getNbObjects: " + count);
            synchronized(CounterMonitorTest.class) {
                observedValue = count;
                CounterMonitorTest.class.notifyAll();
            }
            return observedValue;
        }
        public void setNbObjects(Object n) {
            echo(">>> StdObservedObject.setNbObjects: " + n);
            count = n;
        }
        private volatile Object count= null;
    }

    
    public interface StdObservedObjectMBean {
        public Object getNbObjects();
        public void setNbObjects(Object n);
    }

    
    public void handleNotification(Notification notification,
                                   Object handback) {
        MonitorNotification n = (MonitorNotification) notification;
        echo("\tInside handleNotification...");
        String type = n.getType();
        try {
            if (type.equals(MonitorNotification.THRESHOLD_VALUE_EXCEEDED)) {
                echo("\t\t" + n.getObservedAttribute() +
                     " has reached or exceeded the threshold");
                echo("\t\tDerived Gauge = " + n.getDerivedGauge());

                synchronized (this) {
                    messageReceived = true;
                    notifyAll();
                }
            } else {
                echo("\t\tSkipping notification of type: " + type);
            }
        } catch (Exception e) {
            echo("\tError in handleNotification!");
            e.printStackTrace(System.out);
        }
    }

    
    public void thresholdNotification() throws Exception {

        CounterMonitor counterMonitor = new CounterMonitor();
        try {
            MBeanServer server = MBeanServerFactory.newMBeanServer();

            String domain = server.getDefaultDomain();

            
            
            echo(">>> CREATE a new CounterMonitor MBean");
            ObjectName counterMonitorName = new ObjectName(
                            domain + ":type=" + CounterMonitor.class.getName());
            server.registerMBean(counterMonitor, counterMonitorName);

            echo(">>> ADD a listener to the CounterMonitor");
            counterMonitor.addNotificationListener(this, null, null);

            
            
            

            echo(">>> CREATE a new StdObservedObject MBean");

            ObjectName stdObsObjName =
                new ObjectName(domain + ":type=StdObservedObject");
            StdObservedObject stdObsObj = new StdObservedObject();
            server.registerMBean(stdObsObj, stdObsObjName);

            echo(">>> SET the attributes of the CounterMonitor:");

            counterMonitor.addObservedObject(stdObsObjName);
            echo("\tATTRIBUTE \"ObservedObject\"    = " + stdObsObjName);

            counterMonitor.setObservedAttribute("NbObjects");
            echo("\tATTRIBUTE \"ObservedAttribute\" = NbObjects");

            counterMonitor.setNotify(notifyFlag);
            echo("\tATTRIBUTE \"Notify\"            = " + notifyFlag);

            counterMonitor.setInitThreshold(threshold);
            echo("\tATTRIBUTE \"Threshold\"         = " + threshold);

            counterMonitor.setGranularityPeriod(granularityperiod);
            echo("\tATTRIBUTE \"GranularityPeriod\" = " + granularityperiod);

            counterMonitor.setModulus(modulus);
            echo("\tATTRIBUTE \"Modulus\"           = " + modulus);

            counterMonitor.setDifferenceMode(differenceModeFlag);
            echo("\tATTRIBUTE \"DifferenceMode\"    = " + differenceModeFlag);

            echo(">>> START the CounterMonitor");
            counterMonitor.start();

            
            
            Integer data = new Integer(0);
            echo(">>> Set data = " + data.intValue());

            Attribute attrib = new Attribute("NbObjects", data);
            server.setAttribute(stdObsObjName, attrib);

            waitObservation(data);

            
            
            while (derivedGauge++ < 10) {
                System.out.print(">>> Set data from " + data.intValue());
                data = new Integer(data.intValue() + derivedGauge);
                echo(" to " + data.intValue());

                attrib = new Attribute("NbObjects", data);
                server.setAttribute(stdObsObjName, attrib);
                waitObservation(data);

                echo("\tdoWait in Counter Monitor");
                doWait();

                
                
                if (messageReceived) {
                    echo("\tOKAY: Notification received");
                } else {
                    echo("\tError: notification missed or not emitted");
                    throw new IllegalStateException("Notification lost");
                }
                messageReceived = false;
            }
        } finally {
            counterMonitor.stop();
        }

        echo(">>> Bye! Bye!");
    }

    
    synchronized void doWait() {
        while (!messageReceived) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println("Got unexpected exception: " + e);
                e.printStackTrace();
                break;
            }
        }
    }

    private void waitObservation(Object value) {
        synchronized (CounterMonitorTest.class) {
            while (value != observedValue) {
                try {
                    CounterMonitorTest.class.wait();
                } catch (InterruptedException e) {
                    System.err.println("Got unexpected exception: " + e);
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    
    void echo(String message) {
        System.out.println(message);
    }

    
    public static void main (String args[]) throws Exception {
        CounterMonitorTest test = new CounterMonitorTest();
        test.thresholdNotification();
    }
}
