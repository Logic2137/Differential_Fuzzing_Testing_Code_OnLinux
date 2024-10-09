

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;


public class Reopen {

    private static boolean isTestExecuted;
    private static boolean isTestPassed;

    
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            doAllTests();
        } else if (args.length == 2) {
            int numIterations = Integer.parseInt(args[0]);
            if (args[1].equals("in")) {
                doTest(numIterations, true);
            } else {
                doTest(numIterations, false);
            }
        } else {
            out("usage: java Reopen <iterations> in|out");
        }
    }

    private static void doAllTests() throws Exception {
        out("#4914667: Closing and reopening MIDI IN device on Linux throws MidiUnavailableException");
        boolean success = true;
        try {
            success &= doTest(20, true); 
            success &= doTest(20, false); 
            isTestExecuted = true;
        } catch (Exception e) {
            out(e);
            isTestExecuted = false;
        }
        isTestPassed = success;
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

    private static boolean doTest(int numIterations, boolean input) throws Exception {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        MidiDevice outDevice = null;
        MidiDevice inDevice = null;
        for (int i = 0; i < infos.length; i++) {
            MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
            if (! (device instanceof Sequencer) &&
                ! (device instanceof Synthesizer)) {
                if (device.getMaxReceivers() != 0) {
                    outDevice = device;
                }
                if (device.getMaxTransmitters() != 0) {
                    inDevice = device;
                }
            }
        }
        MidiDevice testDevice = null;
        if (input) {
            testDevice = inDevice;
        } else {
            testDevice = outDevice;
        }
        if (testDevice == null) {
            out("Cannot test: device not available.");
            return true;
        }
        out("Using Device: " + testDevice);

        for (int i = 0; i < numIterations; i++) {
            out("@@@ ITERATION: " + i);
            testDevice.open();
            
            sleep(50);
            testDevice.close();
        }
        return true;
    }

    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
        }
    }

    private static void out(Throwable t) {
        t.printStackTrace(System.out);
    }

    private static void out(String message) {
        System.out.println(message);
    }
}
