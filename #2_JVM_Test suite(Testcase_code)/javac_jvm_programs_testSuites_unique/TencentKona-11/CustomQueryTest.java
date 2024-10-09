



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.QueryEval;
import javax.management.QueryExp;

public class CustomQueryTest {
    public static interface CountMBean {
        public int getCount();
        public void increment();
    }

    public static class Count implements CountMBean {
        private AtomicInteger count = new AtomicInteger();

        public int getCount() {
            return count.get();
        }

        public void increment() {
            count.incrementAndGet();
        }

    }

    public static final ObjectName countName;
    static {
        try {
            countName = new ObjectName("d:type=Count");
        } catch (MalformedObjectNameException e) {
            throw new AssertionError(e);
        }
    }

    
    public static class IncrQuery extends QueryEval implements QueryExp {
        public boolean apply(ObjectName name) {
            try {
                getMBeanServer().invoke(countName, "increment", null, null);
                return true;
            } catch (Throwable t) {
                t.printStackTrace();
                System.exit(1);
                throw new AssertionError(); 
            }
        }
    }

    public static void main(String[] args) throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        boolean isSecondAttempt = false;
        
        
        while (true) {
            mbs.registerMBean(new Count(), countName);
            int mbeanCount = mbs.getMBeanCount();
            QueryExp query = new IncrQuery();
            Set<ObjectName> names = mbs.queryNames(null, query);
            assertEquals(mbeanCount, names.size());
            assertEquals(mbeanCount, mbs.getAttribute(countName, "Count"));
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream oout = new ObjectOutputStream(bout);
            oout.writeObject(query);
            oout.close();
            ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
            ObjectInputStream oin = new ObjectInputStream(bin);
            query = (QueryExp) oin.readObject();
            names = mbs.queryNames(null, query);
            int counterCount = (int)mbs.getAttribute(countName, "Count");
            if (mbeanCount * 2 == counterCount) {
                break;
            }
            if (isSecondAttempt) {
                assertEquals(mbeanCount * 2, counterCount);
                break;
            }
            isSecondAttempt = true;
            System.out.println("New MBean was registered. Retrying...");
            mbs.unregisterMBean(countName);
        }
    }

    private static void assertEquals(Object expected, Object actual)
            throws Exception {
        if (!expected.equals(actual)) {
            String failure = "FAILED: expected " + expected + ", got " + actual;
            throw new Exception(failure);
        }
    }
}
