

import java.util.LinkedList;
import java.util.List;

import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;


public final class TestDisposerLeak {

    private static final int COUNT = 30000;

    private static volatile boolean disposerComplete;

    private static volatile boolean readyForDispose;

    static int disposeCount = 0;

    public static void main(String[] args) throws Exception {
        TestDisposerLeak test = new TestDisposerLeak();
        test.testLeak();
        while (!disposerComplete) {}
        if (disposeCount != COUNT) {
            System.err.println("dispose called " + disposeCount);
            throw new RuntimeException("All objects are not disposed");
        }else {
            System.out.println("Test PASSED");
        }
    }

    public void testLeak() throws Exception {
        MyDisposerRec disposerRecord = new MyDisposerRec();
        for (int i = 0; i < 30000; i++) {
            Disposer.addObjectRecord(new Object(), disposerRecord);
        }
        generateOOME();
        readyForDispose = true;
        Disposer.addObjectRecord(new Object(), new EndRec());
        generateOOME();
    }

    class MyDisposerRec implements DisposerRecord, Disposer.PollDisposable {

        public void dispose() {
            while (!readyForDispose) {}
            disposeCount++;
            Disposer.pollRemove();
        }
    }

    class EndRec implements DisposerRecord, Disposer.PollDisposable {

        public void dispose() {
            disposerComplete = true;
        }
    }

    private static void generateOOME() throws Exception {
        List<Object> leak = new LinkedList<>();
        try {
            while (true) {
                leak.add(new byte[1024 * 1024]);
            }
        } catch (OutOfMemoryError e) {
        }
        
        Thread.sleep(2000);
    }
}
