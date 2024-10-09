import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public class ReceiverTransmitterAvailable {

    private static boolean isTestExecuted;

    private static boolean isTestPassed;

    public static void main(String[] args) throws Exception {
        out("#4616517: Receiver.send() does not work properly");
        doAllTests();
        if (isTestExecuted) {
            if (isTestPassed) {
                out("Test PASSED.");
            } else {
                throw new Exception("Test FAILED.");
            }
        } else {
            out("Test NOT FAILED");
        }
    }

    private static void doAllTests() {
        boolean problemOccured = false;
        boolean succeeded = true;
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
            MidiDevice device = null;
            try {
                device = MidiSystem.getMidiDevice(infos[i]);
                succeeded &= doTest(device);
            } catch (MidiUnavailableException e) {
                out("exception occured; cannot test");
                problemOccured = true;
            }
        }
        if (infos.length == 0) {
            out("Soundcard does not exist or sound drivers not installed!");
            out("This test requires sound drivers for execution.");
        }
        isTestExecuted = !problemOccured;
        isTestPassed = succeeded;
    }

    private static boolean doTest(MidiDevice device) {
        boolean succeeded = true;
        out("Testing: " + device);
        boolean expectingReceivers = (device.getMaxReceivers() != 0);
        boolean expectingTransmitters = (device.getMaxTransmitters() != 0);
        try {
            Receiver rec = device.getReceiver();
            rec.close();
            if (!expectingReceivers) {
                out("no exception on getting Receiver");
                succeeded = false;
            }
        } catch (MidiUnavailableException e) {
            if (expectingReceivers) {
                out("Exception on getting Receiver: " + e);
                succeeded = false;
            }
        }
        try {
            Transmitter trans = device.getTransmitter();
            trans.close();
            if (!expectingTransmitters) {
                out("no exception on getting Transmitter");
                succeeded = false;
            }
        } catch (MidiUnavailableException e) {
            if (expectingTransmitters) {
                out("Exception on getting Transmitter: " + e);
                succeeded = false;
            }
        }
        return succeeded;
    }

    private static void out(String message) {
        System.out.println(message);
    }
}
