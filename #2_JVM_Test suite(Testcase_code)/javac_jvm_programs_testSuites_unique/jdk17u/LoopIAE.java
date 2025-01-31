import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class LoopIAE {

    static ShortMessage MidiMsg3(int a, int b, int c) {
        try {
            ShortMessage msg = new ShortMessage();
            msg.setMessage((byte) a, (byte) b, (byte) c);
            return msg;
        } catch (InvalidMidiDataException ex) {
            throw new RuntimeException();
        }
    }

    static boolean failed = false;

    public static void main(String[] argv) throws Exception {
        if (!hasSequencer()) {
            return;
        }
        Sequencer sequencer = MidiSystem.getSequencer();
        Sequence sequence = new Sequence(Sequence.PPQ, 240);
        Track track = sequence.createTrack();
        track.add(new MidiEvent(MidiMsg3(ShortMessage.NOTE_ON + 0, 45, 100), 0));
        track.add(new MidiEvent(MidiMsg3(ShortMessage.NOTE_ON + 0, 45, 0), 0 + 240));
        track.add(new MidiEvent(MidiMsg3(ShortMessage.NOTE_ON + 9, 45, 100), 10 * 20));
        track.add(new MidiEvent(MidiMsg3(ShortMessage.NOTE_ON + 9, 45, 0), 10 * 20 + 10));
        try {
            sequencer.open();
            sequencer.setSequence(sequence);
            sequencer.setTempoInBPM(100);
            System.out.println("Setting loop end point to 1");
            sequencer.setLoopEndPoint(1);
            System.out.println("  -> effectively: " + sequencer.getLoopEndPoint());
            System.out.println("Setting loop start point to 2 -- should throw IAE");
            sequencer.setLoopStartPoint(2);
            System.out.println("  -> effectively: " + sequencer.getLoopStartPoint());
            System.out.println("No IllegalArgumentException was thrown!");
            failed = true;
        } catch (IllegalArgumentException iae) {
            System.out.println("IAE was thrown correctly.");
        } catch (MidiUnavailableException mue) {
            System.out.println("MidiUnavailableException was thrown: " + mue);
            System.out.println("Cannot execute test.");
        } catch (InvalidMidiDataException imEx) {
            System.out.println("InvalidMidiDataException was thrown.");
            imEx.printStackTrace();
            System.out.println("Cannot execute test.");
        } finally {
            if (sequencer != null && sequencer.isOpen()) {
                sequencer.close();
            }
        }
        if (failed) {
            throw new Exception("Test FAILED!");
        }
        System.out.println("test passed.");
    }

    static boolean hasSequencer() {
        try {
            Sequencer seq = MidiSystem.getSequencer();
            if (seq != null) {
                if (seq.isOpen()) {
                    seq.close();
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("No sequencer available! Cannot execute test.");
        return false;
    }
}
