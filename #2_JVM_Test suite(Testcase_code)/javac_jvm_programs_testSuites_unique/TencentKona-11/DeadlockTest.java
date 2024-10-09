



import javax.management.*;
import javax.management.timer.*;

public class DeadlockTest extends StandardMBean {
    public <T> DeadlockTest(T implementation, Class<T> mbeanInterface)
    throws NotCompliantMBeanException {
        super(implementation, mbeanInterface);
    }

    public MBeanInfo getCachedMBeanInfo() {
        return super.getCachedMBeanInfo();
    }

    public void cacheMBeanInfo(MBeanInfo mi) {
        super.cacheMBeanInfo(mi);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("main: No deadlock please.");

        System.out.println("main: Create a BadBay to hold the lock forever.");
        DeadlockTest dt = new DeadlockTest(new Timer(), TimerMBean.class);

        BadBoy bb = new BadBoy(dt);
        bb.start();

        synchronized(bb) {
            while(!bb.gotLock) {
                bb.wait(); 
            }
        }

        System.out.println("main: The BadBay is holding the lock forever.");

        System.out.println("main: Create a WorkingBoy to see blocking ...");
        WorkingBoy wb = new WorkingBoy(dt);

        synchronized(wb) {
            wb.start();

            while(!wb.done) {
                wb.wait(); 
            }
        }

        System.out.println("main: OK, bye bye.");
    }

    private static class BadBoy extends Thread {
        public BadBoy(Object o) {
            setDaemon(true);

            this.o = o;
        }

        public void run() {
            System.out.println("BadBoy-run: keep synchronization lock forever!");

            synchronized(o) {
                synchronized(this) {
                    gotLock = true;

                    this.notify();
                }

                try {
                    Thread.sleep(10000000);
                } catch (Exception e) {
                    
                }
            }
        }

        final Object o;
        public boolean gotLock;
    }

    private static class WorkingBoy extends Thread {
        public WorkingBoy(DeadlockTest sm) {
            setDaemon(true);

            this.sm = sm;
        }

        public void run() {
            try {
                System.out.println("WorkingBoy-run: calling StandardMBean methods ...");

                System.out.println("WorkingBoy-run: calling setImplementation ...");
                sm.setImplementation(new Timer());

                System.out.println("WorkingBoy-run: calling getImplementation ...");
                sm.getImplementation();

                System.out.println("WorkingBoy-run: calling getMBeanInterface ...");
                sm.getMBeanInterface();

                System.out.println("WorkingBoy-run: calling getImplementationClass ...");
                sm.getImplementationClass();

                System.out.println("WorkingBoy-run: calling cacheMBeanInfo ...");
                sm.cacheMBeanInfo(null);

                System.out.println("WorkingBoy-run: calling getCachedMBeanInfo ...");
                sm.getCachedMBeanInfo();

                System.out.println("WorkingBoy-run: All done!");

                synchronized(this) {
                    done = true;

                    this.notifyAll();
                }
            } catch (NotCompliantMBeanException ne) {
                
                throw new RuntimeException(ne);
            }
        }

        final DeadlockTest sm;
        public boolean done;
    }
}
