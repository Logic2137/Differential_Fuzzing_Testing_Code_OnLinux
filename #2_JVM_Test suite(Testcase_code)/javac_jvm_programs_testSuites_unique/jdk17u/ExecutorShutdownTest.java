import java.util.*;
import java.util.concurrent.*;
import javax.management.*;
import javax.management.remote.*;

public class ExecutorShutdownTest {

    private static final String EXECUTOR_PROPERTY = "jmx.remote.x.fetch.notifications.executor";

    private static final String NOTIF_TYPE = "test.type";

    public static void main(String[] args) throws Exception {
        JMXServiceURL url = new JMXServiceURL("rmi", null, 0);
        MBeanServer mbs = MBeanServerFactory.newMBeanServer();
        ObjectName emitName = new ObjectName("blah:type=Emitter");
        mbs.registerMBean(new Emitter(), emitName);
        JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
        cs.start();
        ExecutorService executor = Executors.newCachedThreadPool();
        Map<String, Executor> env = new HashMap<>();
        env.put(EXECUTOR_PROPERTY, executor);
        JMXServiceURL addr = cs.getAddress();
        try (JMXConnector cc = JMXConnectorFactory.connect(addr, env)) {
            MBeanServerConnection mbsc = cc.getMBeanServerConnection();
            EmitterMBean emitter = (EmitterMBean) MBeanServerInvocationHandler.newProxyInstance(mbsc, emitName, EmitterMBean.class, false);
            SemaphoreListener listener = new SemaphoreListener();
            NotificationFilterSupport filter = new NotificationFilterSupport();
            filter.enableType(NOTIF_TYPE);
            mbsc.addNotificationListener(emitName, listener, filter, null);
            final int NOTIF_COUNT = 3;
            for (int i = 0; i < NOTIF_COUNT; i++) {
                emitter.emit();
                listener.await();
            }
            Thread.sleep(1);
            listener.checkUnavailable();
            System.out.println("Got notifications with client provided executor");
            executor.shutdown();
            for (int i = 0; i < NOTIF_COUNT; i++) {
                emitter.emit();
                listener.await();
            }
            Thread.sleep(1);
            listener.checkUnavailable();
            System.out.println("Got notifications with linear executor");
        }
        cs.stop();
        System.out.println("TEST PASSED !!!");
    }

    public static interface EmitterMBean {

        public void emit();
    }

    public static class Emitter extends NotificationBroadcasterSupport implements EmitterMBean {

        public void emit() {
            sendNotification(new Notification(NOTIF_TYPE, this, seq++));
        }

        private long seq = 1;
    }

    private static class SemaphoreListener implements NotificationListener {

        void await() throws InterruptedException {
            semaphore.acquire();
        }

        void checkUnavailable() throws Exception {
            if (semaphore.tryAcquire()) {
                throw new Exception("Got extra notifications!");
            }
        }

        public void handleNotification(Notification n, Object h) {
            semaphore.release();
        }

        private final Semaphore semaphore = new Semaphore(0);
    }
}
