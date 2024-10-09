

import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;


public class MidiDeviceGetReceivers {

    private static boolean executed = false;
    private static boolean failed = false;

    public static void main(String[] args) throws Exception {
        out("unit test 4931387: Add methods to MidiDevice to get list of Transmitters and Receivers");
        doAllTests();
        if (executed) {
            if (failed) throw new Exception("Test FAILED!");
            out("Test PASSED.");
        } else {
            out("Test NOT failed.");
        }
    }

    private static void doAllTests() {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
            MidiDevice device = null;
            try {
                device = MidiSystem.getMidiDevice(infos[i]);
                doTest(device);
            } catch (MidiUnavailableException e) {
                out("Exception occured when retrieving device "+infos[i]+": "+e);
            }
        }
        if (infos.length == 0) {
            out("No MIDI devices exist or sound drivers not installed!");
        }
    }

    private static boolean containsReceiver(MidiDevice dev, Receiver rec) {
        List<Receiver> recvs = dev.getReceivers();
        return recvs.contains(rec);
    }

    private static boolean containsTransmitter(MidiDevice dev, Transmitter tra) {
        List<Transmitter> tras = dev.getTransmitters();
        return tras.contains(tra);
    }

    private static void doTest(MidiDevice device) {
        boolean thisFailed = false;
        out1("Testing: " + device+"...");
        try {
            device.open();
        } catch (Exception e) {
            out2("device.open threw exception: "+e);
            out2("cannot test this device.");
            return;
        }
        if (device.getMaxReceivers() != 0) {
            
            try {
                List<Receiver> origList = device.getReceivers();
                Receiver rec = device.getReceiver();
                if (!containsReceiver(device, rec)) {
                    out2("Getting a receiver did not add it to device list!");
                    thisFailed = true;
                }
                if (origList.contains(rec)) {
                    out2("Original unmodifiable list was modified by adding a receiver!");
                    thisFailed = true;
                }
                rec.close();
                if (containsReceiver(device, rec)) {
                    out2("Closing a receiver did not remove it from device list!");
                    thisFailed = true;
                }
                
                
                rec = device.getReceiver();
                if (!containsReceiver(device, rec)) {
                    out2("Getting a receiver again did not add it to device list!");
                    thisFailed = true;
                }
            } catch (MidiUnavailableException e) {
                out2("Exception on getting Receiver: " + e);
            }
        }
        if (device.getMaxTransmitters() != 0) {
            
            try {
                List<Transmitter> origList = device.getTransmitters();
                Transmitter tra = device.getTransmitter();
                if (!containsTransmitter(device, tra)) {
                    out2("Getting a transmitter did not add it to device list!");
                    thisFailed = true;
                }
                if (origList.contains(tra)) {
                    out2("Original unmodifiable list was modified by adding a transmitter!");
                    thisFailed = true;
                }
                tra.close();
                if (containsTransmitter(device, tra)) {
                    out2("Closing a transmitter did not remove it from device list!");
                    thisFailed = true;
                }
                tra = device.getTransmitter();
                if (!containsTransmitter(device, tra)) {
                    out2("Getting a transmitter again did not add it to device list!");
                    thisFailed = true;
                }
            } catch (MidiUnavailableException e) {
                out("Exception on getting Transmitter: " + e);
            }
        }
        try {
            device.close();
            if (device.getTransmitters().size() > 0) {
                out2(" Device still has transmitters after close() was called!");
                thisFailed = true;
            }
            if (device.getReceivers().size() > 0) {
                out2(" Device still has receivers after close() was called!");
                thisFailed = true;
            }
        } catch (Exception e) {
            out2("device.close threw exception: "+e);
        }
        if (!thisFailed) {
            out("OK");
        } else {
            failed = true;
        }
        executed = true;
    }

    static boolean lfMissing = false;

    private static void out(String message) {
        lfMissing = true;
        System.out.println(message);
    }

    
    private static void out1(String message) {
        System.out.print(message);
        lfMissing = true;
    }

    
    private static void out2(String message) {
        if (lfMissing) {
            System.out.println();
            lfMissing = false;
        }
        System.out.println("  "+message);
    }
}
