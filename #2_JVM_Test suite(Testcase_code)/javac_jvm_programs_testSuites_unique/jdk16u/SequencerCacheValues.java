

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;


public class SequencerCacheValues {

    static boolean failed = false;

    public static void main(String args[]) throws Exception {
        Sequencer seq = null;
        int totalNumberOfSequencers = 0;

        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int device=0; device<infos.length; device++) {
            
            MidiDevice dev = MidiSystem.getMidiDevice(infos[device]);
            if (dev instanceof Sequencer) {
                seq = (Sequencer) dev;
                totalNumberOfSequencers++;
                System.out.println("Opening sequencer "+infos[device]);
                try {
                    seq.open();
                    try {
                        doTest(seq);
                    } finally {
                        if (seq != null) {
                            seq.close();
                            seq = null;
                        }
                    }
                } catch (MidiUnavailableException mue) {
                    System.err.println("MidiUnavailableException was thrown: " + mue);
                    System.err.println("could not test this sequencer.");
                }
            }
        }
        if (totalNumberOfSequencers == 0) {
            System.out.println("No sequencers installed!");
            failed = true;
        }
        if (failed) {
            throw new Exception("FAILED");
        } else {
            System.out.println("test OK");
        }
    }

    public static boolean equalsFloat(float f1, float f2) {
        return (f1-f2<0.0001) && (f2-f1<0.0001);
    }

    public static void doTest(Sequencer seq) throws Exception {
        seq.setTempoInMPQ(3.0f);
        System.out.println("Setting tempo in MPQ to "+3.0f);
        if (!equalsFloat(seq.getTempoInMPQ(), 3.0f)) {
            System.err.println("getTempoInMPQ() returns wrong value : "
                + seq.getTempoInMPQ());
            failed = true;
        }

        System.out.println("Setting tempo factor to "+2.0f);
        seq.setTempoFactor(2.0f);
        if (!equalsFloat(seq.getTempoFactor(), 2.0f)) {
            System.err.println("getTempoFactor() returns: " + seq.getTempoFactor());
            failed = true;
        }

        float bpmTempo = 120.0f;
        System.out.println("Setting tempo to "+120.0f+"bpm");
        seq.setTempoInBPM(bpmTempo);
        if (!equalsFloat(seq.getTempoInMPQ(), (60000000.0f/seq.getTempoInBPM()))) {
            System.err.println("getTempoInMPQ() returns: " + seq.getTempoInMPQ());
            System.err.println("getTempoInBPM() returns: " + seq.getTempoInBPM());
            failed = true;
        }
    }
}
