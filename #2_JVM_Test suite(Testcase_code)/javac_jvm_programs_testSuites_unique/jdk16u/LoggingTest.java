

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.util.List;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public class LoggingTest {

    static class TestStream extends PrintStream {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        private volatile boolean recording;
        public TestStream(PrintStream wrapped) {
            super(wrapped);
        }

        void startRecording() {
            recording = true;
        }

        void stopRecording() {
            recording = false;
        }

        @Override
        public void write(int b) {
            if (recording) {
                bos.write(b);
            }
            super.write(b);
        }

        @Override
        public void write(byte[] buf, int off, int len) {
            if (recording) {
                bos.write(buf, off, len);
            }
            super.write(buf, off, len);
        }

        @Override
        public void write(byte[] buf) throws IOException {
            if (recording) {
                bos.write(buf);
            }
            super.write(buf);
        }

    }

    public void run(TestStream ts) {

        
        
        
        
        ts.startRecording();
        MBeanServer platform = ManagementFactory.getPlatformMBeanServer();
        ts.stopRecording();
        String printed = ts.bos.toString();
        ts.bos.reset();

        
        
        
        
        
        
        
        List<String> checkTraces =
                List.of("ObjectName = %s", "name = %s", "JMX.mbean.registered %s");

        for (ObjectName o : platform.queryNames(ObjectName.WILDCARD, null)) {
            String n = o.toString();
            System.out.println("Checking log for: " + n);
            for (String check : checkTraces) {
                String s = String.format(check, n);
                if (!printed.contains(s)) {
                    throw new RuntimeException("Trace not found: " + s);
                }
            }
        }
    }

}
