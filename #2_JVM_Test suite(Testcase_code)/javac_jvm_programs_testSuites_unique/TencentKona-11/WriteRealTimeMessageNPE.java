

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


public class WriteRealTimeMessageNPE {

    public static void main(String args[]) throws Exception {
        System.out.println("5048381: NullPointerException when saving a MIDI sequence");
        boolean npeThrown = false;
        boolean noEx = false;

        Sequence seq = new Sequence(Sequence.PPQ, 384, 1);
        Track t = seq.getTracks()[0];
        ShortMessage msg = new ShortMessage();
        msg.setMessage(0xF8, 0, 0);
        t.add(new MidiEvent(msg, 0));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            MidiSystem.write(seq, 0, out);
            noEx = true;
        } catch (NullPointerException npe) {
            npeThrown = true;
            System.out.println("## Failed: Threw unexpected NPE: "+npe);
            throw new Exception("Test FAILED!");
        } catch (Exception e) {
            System.out.println("Threw unexpected Exception: "+e);
            System.out.println("But at least did not throw NPE...");
        }
        if (noEx) {
            InputStream is = new ByteArrayInputStream(out.toByteArray());
            seq = MidiSystem.getSequence(is);
            System.out.println("Sequence has "+seq.getTracks().length+" tracks.");
            if (seq.getTracks().length > 0) {
                System.out.println("Track 0 has "+seq.getTracks()[0].size()+" events.");
            }
        }
        System.out.println("Test passed.");
    }
}
