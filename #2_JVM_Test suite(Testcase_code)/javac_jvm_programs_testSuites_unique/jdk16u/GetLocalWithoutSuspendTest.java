



public class GetLocalWithoutSuspendTest {

    public static final int M = 1 << 20;

    public static final int TEST_ITERATIONS = 200;

    
    public static native void notifyAgentToGetLocal(int depth, int waitTime);

    
    public static native void shutDown();

    
    public static native void setTargetThread(Thread target);

    public static void main(String[] args) throws Exception {
        new GetLocalWithoutSuspendTest().runTest();
    }

    
    public int waitCycles = 1;

    public void runTest() throws Exception {
        log("Set target thread for get local variable calls by agent.");
        setTargetThread(Thread.currentThread());

        log("Test how many frames fit on the stack by performing recursive calls until");
        log("StackOverflowError is thrown");
        int targetDepth = recursiveMethod(0, M);
        log("Testing with target depth: " + targetDepth);

        log("Begin Test.");
        long start = System.currentTimeMillis();
        for (int iterations = 0; iterations < TEST_ITERATIONS; iterations++) {
            long now = System.currentTimeMillis();
            log((now - start) + " ms  Iteration : " + iterations +
                "  waitTime : " + waitCycles);
            int newTargetDepth = recursiveMethod(0, targetDepth);
            if (newTargetDepth < targetDepth) {
                
                
                
                
                
                log("StackOverflowError during test.");
                log("Old target depth: " + targetDepth);
                log("Retry with new target depth: " + newTargetDepth);
                targetDepth = newTargetDepth;
            }
            iterations++;
            
            waitCycles = (waitCycles << 1) & (M - 1);
            waitCycles = waitCycles == 0 ? 1 : waitCycles;
        }

        
        shutDown();

        log("Successfully finished test");
    }

    
    public int recursiveMethod(int depth, int targetStackDepth) {
        int maxDepth = depth;
        try {
            if (depth == targetStackDepth) {
                notifyAgentToGetLocal(depth - 100, waitCycles);
            } else {
                maxDepth = recursiveMethod(depth + 1, targetStackDepth);
            }
        } catch (StackOverflowError e) {
            
        }
        return maxDepth;
    }

    public static void log(String m) {
        System.out.println("### Java-Test: " + m);
    }
}
