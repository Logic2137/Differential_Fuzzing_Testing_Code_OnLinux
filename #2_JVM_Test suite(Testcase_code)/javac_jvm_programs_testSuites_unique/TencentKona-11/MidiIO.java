

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;


public class MidiIO {

    public static void main(String[] args) throws Exception {
        out("4356787: MIDI device I/O is not working (windows)");

        if (System.getProperty("os.name").startsWith("Windows")) {
            boolean forInput=true;
            boolean forOutput=true;
            int outOnlyCount=0;
            int inOnlyCount=0;
            out("  available MIDI devices:");
            MidiDevice.Info[] aInfos = MidiSystem.getMidiDeviceInfo();
            for (int i = 0; i < aInfos.length; i++) {
                try {
                    MidiDevice      device = MidiSystem.getMidiDevice(aInfos[i]);
                    boolean         bAllowsInput = (device.getMaxTransmitters() != 0);
                    boolean         bAllowsOutput = (device.getMaxReceivers() != 0);
                    if (bAllowsInput && !bAllowsOutput) {
                        inOnlyCount++;
                    }
                    if (!bAllowsInput && bAllowsOutput) {
                        outOnlyCount++;
                    }
                    if ((bAllowsInput && forInput) || (bAllowsOutput && forOutput)) {
                        out(""+i+"  "
                                +(bAllowsInput?"IN ":"   ")
                                +(bAllowsOutput?"OUT ":"    ")
                                +aInfos[i].getName()+", "
                                +aInfos[i].getVendor()+", "
                                +aInfos[i].getVersion()+", "
                                +aInfos[i].getDescription());
                    }
                }
                catch (MidiUnavailableException e) {
                    
                }
            }
            if (aInfos.length == 0) {
                out("No devices available. Test should be run on systems with MIDI drivers installed.");
            } else {
                if (outOnlyCount>1) {
                    if (inOnlyCount==0) {
                        
                        out("System provides out devices, but no input devices. This means either");
                        out("a bug in Java Sound, or the drivers are not set up correctly.");
                    }
                    out("Test passed.");
                } else {
                    out("no MIDI I/O installed. Test should be run on systems with MIDI drivers installed.");
                }
            }
        } else {
            out("  -- not on Windows. Test doesn't apply.");
        }
    }

    static void out(String s) {
        System.out.println(s); System.out.flush();
    }
}
