



import java.io.IOException;
import java.nio.channels.Pipe;


public class PipeInterrupt {

    private Exception exc = null;

    public static void main(String[] args) throws Exception {
        PipeInterrupt instance = new PipeInterrupt();
        instance.test();
    }

    public void test() throws Exception {

        Thread tester = new Thread("PipeTester") {
            private Pipe testPipe = null;

            @Override
            public void run() {
                for (;;) {
                    boolean interrupted = this.isInterrupted();
                    try {
                        testPipe = Pipe.open();
                        close();
                        if (interrupted) {
                            if (!this.isInterrupted())
                               exc = new RuntimeException("interrupt status reset");
                            break;
                        }
                    } catch (IOException ioe) {
                        exc = ioe;
                    }
                }
            }

            private void close() throws IOException {
                if (testPipe != null) {
                    testPipe.sink().close();
                    testPipe.source().close();
                }
            }
        };

        tester.start();
        Thread.sleep(200);
        tester.interrupt();
        tester.join();

        if (exc != null)
            throw exc;
    }
}
