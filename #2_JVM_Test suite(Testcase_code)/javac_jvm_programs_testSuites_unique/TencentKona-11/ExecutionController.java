
package nsk.share.test;

public interface ExecutionController {
        public void start(long stdIterations);
        public boolean iteration();
        public boolean continueExecution();
        public long getIteration();
        public void finish();
}
