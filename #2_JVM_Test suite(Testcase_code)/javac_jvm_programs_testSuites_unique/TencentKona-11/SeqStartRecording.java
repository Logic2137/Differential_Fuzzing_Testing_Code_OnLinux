

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;


public class SeqStartRecording {

    public static void main(String argv[]) {
        Sequencer seq = null;
        try {
            seq = MidiSystem.getSequencer();
            seq.open();
        } catch (final MidiUnavailableException ignored) {
            
            return;
        }
        try {
            seq.startRecording();
            System.out.println("Test passed.");
        } catch (NullPointerException npe) {
            System.out.println("Caught NPE: "+npe);
            npe.printStackTrace();
            throw new RuntimeException("Test FAILED!");
        } catch (Exception e) {
            System.out.println("Unexpected Exception: "+e);
            e.printStackTrace();
            System.out.println("Test NOT failed.");
        } finally {
            seq.close();
        }
    }
}
