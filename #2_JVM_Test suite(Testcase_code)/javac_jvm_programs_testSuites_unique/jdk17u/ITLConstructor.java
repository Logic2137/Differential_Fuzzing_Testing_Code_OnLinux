public class ITLConstructor {

    static InheritableThreadLocal<Integer> n = new InheritableThreadLocal<>() {

        protected Integer initialValue() {
            return 0;
        }

        protected Integer childValue(Integer parentValue) {
            return parentValue + 1;
        }
    };

    static final int CHILD_THREAD_COUNT = 10;

    public static void main(String[] args) throws Exception {
        test(true);
        test(false);
    }

    static void test(boolean inherit) throws Exception {
        int[] x = new int[CHILD_THREAD_COUNT];
        Thread child = new Thread(Thread.currentThread().getThreadGroup(), new AnotherRunnable(0, x, inherit), "ITLConstructor-thread-" + (0), 0, inherit);
        child.start();
        child.join();
        for (int i = 0; i < CHILD_THREAD_COUNT; i++) {
            int expectedValue = 1;
            if (inherit)
                expectedValue = i + 1;
            if (x[i] != expectedValue)
                throw (new Exception("Got x[" + i + "] = " + x[i] + ", expected: " + expectedValue));
        }
    }

    static class AnotherRunnable implements Runnable {

        final int threadId;

        final int[] x;

        final boolean inherit;

        AnotherRunnable(int threadId, int[] x, boolean inherit) {
            this.threadId = threadId;
            this.x = x;
            this.inherit = inherit;
        }

        public void run() {
            int itlValue = n.get();
            if (threadId < CHILD_THREAD_COUNT - 1) {
                Thread child = new Thread(Thread.currentThread().getThreadGroup(), new AnotherRunnable(threadId + 1, x, inherit), "ITLConstructor-thread-" + (threadId + 1), 0, inherit);
                child.start();
                try {
                    child.join();
                } catch (InterruptedException e) {
                    throw (new RuntimeException("Interrupted", e));
                }
            }
            x[threadId] = itlValue + 1;
        }
    }
}
