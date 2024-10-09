import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

public class MidiOutGetMicrosecondPositionBug {

    static int successfulTests = 0;

    private static void testDevice(MidiDevice device) throws Exception {
        boolean timestampsAvailable = false;
        boolean timestampPrecisionOk = false;
        try {
            device.open();
            long timestamp = device.getMicrosecondPosition();
            timestampsAvailable = (timestamp != -1);
            long systemTime1 = System.currentTimeMillis();
            long deviceTime1 = device.getMicrosecondPosition();
            Thread.sleep(5000);
            long systemTime2 = System.currentTimeMillis();
            long deviceTime2 = device.getMicrosecondPosition();
            long systemDuration = systemTime2 - systemTime1;
            long deviceDuration = (deviceTime2 - deviceTime1) / 1000;
            long delta = Math.abs(systemDuration - deviceDuration);
            timestampPrecisionOk = (delta <= 500);
        } catch (Throwable t) {
            System.out.println("  - Caught exception. Not failed.");
            System.out.println("  - " + t.toString());
            return;
        } finally {
            device.close();
        }
        if (!timestampsAvailable) {
            throw new Exception("timestamps are not supported");
        }
        if (!timestampPrecisionOk) {
            throw new Exception("device timer not precise enough");
        }
        successfulTests++;
    }

    private static void doAll() throws Exception {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
            MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
            if ((!(device instanceof Sequencer)) && (!(device instanceof Synthesizer)) && (device.getMaxReceivers() > 0 || device.getMaxReceivers() == -1)) {
                System.out.println("--------------");
                System.out.println("Testing MIDI device: " + infos[i]);
                testDevice(device);
            }
            if (infos.length == 0) {
                System.out.println("No MIDI devices available!");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (!isMidiInstalled()) {
            return;
        }
        doAll();
        if (successfulTests == 0) {
            System.out.println("Could not execute any of the tests. Test NOT failed.");
        } else {
            System.out.println("Test PASSED.");
        }
    }

    public static boolean isMidiInstalled() {
        boolean result = false;
        MidiDevice.Info[] devices = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < devices.length; i++) {
            try {
                MidiDevice device = MidiSystem.getMidiDevice(devices[i]);
                result = !(device instanceof Sequencer) && !(device instanceof Synthesizer);
            } catch (Exception e1) {
                System.err.println(e1);
            }
            if (result)
                break;
        }
        if (!result) {
            System.err.println("Soundcard does not exist or sound drivers not installed!");
            System.err.println("This test requires sound drivers for execution.");
        }
        return result;
    }
}
