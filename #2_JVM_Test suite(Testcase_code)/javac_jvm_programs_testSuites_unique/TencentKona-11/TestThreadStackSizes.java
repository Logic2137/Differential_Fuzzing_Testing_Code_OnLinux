



public class TestThreadStackSizes extends Thread {
    static final int K = 1024;

    public TestThreadStackSizes(long stackSize) {
        super(null, null, "TestThreadStackSizes" + stackSize, stackSize);
    }

    @Override
    public void run() {
    }

    public static void createThread(long stackSize) {
        System.out.println("StackSize: " + stackSize);
        try {
            TestThreadStackSizes testThreadStackSize = new TestThreadStackSizes(stackSize);
            testThreadStackSize.start();
            try {
                testThreadStackSize.join();
            } catch (InterruptedException e) {
                throw new Error("InterruptedException in main thread", e);
            }
        } catch (Error e) {  
            System.out.println("Got exception for stack size " + stackSize  + ": " + e);
        }
    }

    public static void main(String[] args) throws Error {
        
        for (int i = 0; i <= 320; i++) {
            createThread(i * K);
        }
        
        createThread(500*K);
        createThread(501*K);
        createThread(-1);
        createThread(500*K*K);
        createThread(9223372036854774784L);
    }
}
