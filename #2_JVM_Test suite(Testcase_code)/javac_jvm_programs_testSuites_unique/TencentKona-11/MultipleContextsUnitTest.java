import java.awt.AWTEvent;
import java.awt.event.InvocationEvent;
import java.lang.reflect.Constructor;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import sun.awt.AppContext;
import sun.awt.SunToolkit;

public final class MultipleContextsUnitTest {

    private static final int COUNT = 20;

    private static final AppContext[] apps = new AppContext[COUNT];

    private static final CountDownLatch go = new CountDownLatch(1);

    private static final CountDownLatch end = new CountDownLatch(COUNT);

    private static volatile int createSENumber = 0;

    private static volatile int dispatchSENumber = 0;

    public static void main(final String[] args) throws Exception {
        for (int i = 0; i < COUNT; i++) {
            Thread t = testThread(i);
            t.start();
            t.join();
        }
        for (AppContext app : apps) {
            SunToolkit.postEvent(app, new InvocationEvent(new Object(), () -> {
                try {
                    go.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }));
        }
        AWTEvent eventOne = getSequencedEvent();
        {
            AppContext app = apps[1];
            AWTEvent eventTwo = getSequencedEvent();
            AWTEvent eventThree = getSequencedEvent();
            SunToolkit.postEvent(app, eventThree);
            SunToolkit.postEvent(app, eventTwo);
            SunToolkit.postEvent(app, new InvocationEvent(new Object(), () -> {
                System.err.println(AppContext.getAppContext());
                end.countDown();
            }));
        }
        for (int i = 2; i < apps.length; i++) {
            AppContext app = apps[i];
            AWTEvent eventTwo = getSequencedEvent();
            SunToolkit.postEvent(app, eventTwo);
            AtomicReference<Boolean> called1 = new AtomicReference(false);
            AtomicReference<Boolean> called2 = new AtomicReference(false);
            int num1 = createSENumber;
            SunToolkit.postEvent(app, new InvocationEvent(new Object(), () -> {
                if (dispatchSENumber < num1) {
                    throw new RuntimeException("Dispatched too early");
                }
                called1.set(true);
                if (called2.get()) {
                    throw new RuntimeException("Second event is called before first");
                }
            }));
            AWTEvent eventThree = getSequencedEvent();
            SunToolkit.postEvent(app, eventThree);
            int num2 = createSENumber;
            SunToolkit.postEvent(app, new InvocationEvent(new Object(), () -> {
                if (dispatchSENumber < num2) {
                    throw new RuntimeException("Dispatched too early");
                }
                called2.set(true);
                if (!called1.get()) {
                    throw new RuntimeException("First event is not called before second");
                }
                System.err.println(AppContext.getAppContext());
                end.countDown();
            }));
        }
        SunToolkit.postEvent(apps[0], eventOne);
        SunToolkit.postEvent(apps[0], new InvocationEvent(new Object(), () -> {
            System.err.println(AppContext.getAppContext());
            end.countDown();
        }));
        go.countDown();
        System.err.println("Start to wait");
        end.await();
        System.err.println("End to wait");
    }

    private static Thread testThread(int index) {
        final ThreadGroup group = new ThreadGroup("TG " + index);
        return new Thread(group, () -> {
            apps[index] = SunToolkit.createNewAppContext();
        });
    }

    private static AWTEvent getSequencedEvent() {
        int num = createSENumber++;
        InvocationEvent wrapMe = new InvocationEvent(new Object(), () -> {
            if (num != dispatchSENumber++) {
                System.err.println("num: " + num);
                System.err.println("dispatchSENumber: " + dispatchSENumber);
                throw new RuntimeException("Wrong order");
            }
        });
        try {
            Class<? extends AWTEvent> seqClass = (Class<? extends AWTEvent>) Class.forName("java.awt.SequencedEvent");
            Constructor<? extends AWTEvent> seqConst = seqClass.getConstructor(AWTEvent.class);
            seqConst.setAccessible(true);
            return seqConst.newInstance(wrapMe);
        } catch (Throwable err) {
            throw new RuntimeException("Unable to instantiate SequencedEvent", err);
        }
    }
}
