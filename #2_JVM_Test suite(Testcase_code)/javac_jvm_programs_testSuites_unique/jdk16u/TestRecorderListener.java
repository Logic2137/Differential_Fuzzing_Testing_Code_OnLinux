
package jdk.jfr.api.recorder;

import java.util.concurrent.CountDownLatch;

import jdk.jfr.FlightRecorder;
import jdk.jfr.FlightRecorderListener;
import jdk.jfr.Recording;
import jdk.jfr.RecordingState;


public class TestRecorderListener {

    static class Listener implements FlightRecorderListener {

        private final CountDownLatch latch = new CountDownLatch(1);
        private final RecordingState waitFor;

        public Listener(RecordingState state) {
            waitFor = state;
        }

        @Override
        public void recordingStateChanged(Recording recording) {
            System.out.println("Listener: recording=" + recording.getName() + " state=" + recording.getState());
            RecordingState rs = recording.getState();
            if (rs == waitFor) {
                latch.countDown();
            }
        }

        public void waitFor() throws InterruptedException {
            latch.await();
        }
    }

    public static void main(String... args) throws Exception {
        Listener recordingListener = new Listener(RecordingState.RUNNING);
        FlightRecorder.addListener(recordingListener);

        Listener stoppedListener = new Listener(RecordingState.STOPPED);
        FlightRecorder.addListener(stoppedListener);

        Listener finishedListener = new Listener(RecordingState.CLOSED);
        FlightRecorder.addListener(finishedListener);

        Recording recording = new Recording();
        if (recording.getState() != RecordingState.NEW) {
            recording.close();
            throw new Exception("New recording should be in NEW state");
        }

        recording.start();
        recordingListener.waitFor();

        recording.stop();
        stoppedListener.waitFor();

        recording.close();
        finishedListener.waitFor();

        testDefaultrecordingStateChangedListener();

    }

    private static class DummyListener implements FlightRecorderListener {

    }

    private static void testDefaultrecordingStateChangedListener() {
        FlightRecorder.addListener(new DummyListener());
        Recording recording = new Recording();
        recording.start();
        recording.stop();
        recording.close();
    }
}
