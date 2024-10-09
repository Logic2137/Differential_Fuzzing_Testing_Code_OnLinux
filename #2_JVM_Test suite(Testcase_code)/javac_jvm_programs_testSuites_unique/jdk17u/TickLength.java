import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class TickLength implements MetaEventListener {

    private Sequence theSequence;

    private Sequencer theSequencer;

    public TickLength() {
        this.initMidiCompoments();
        System.out.println("Got Sequencer " + theSequencer);
        theSequence = this.generateSequence();
        try {
            theSequencer.setSequence(theSequence);
        } catch (Exception e) {
            System.out.println(this.getClass() + "\tCannot set sequence to sequencer (" + e + ")");
            return;
        }
    }

    public void start() {
        theSequencer.start();
    }

    private boolean initMidiCompoments() {
        try {
            theSequencer = MidiSystem.getSequencer();
        } catch (Exception e) {
            System.out.println(this.getClass() + "\tSequencer Device not supported" + e + ")");
            return false;
        }
        try {
            theSequencer.open();
        } catch (Exception e) {
            System.out.println(this.getClass() + "Cannot open Sequencer Device");
            return false;
        }
        if (!theSequencer.addMetaEventListener(this)) {
            System.out.println(this.getClass() + "\tCould not register MetaEventListener - there will be problems with scrolling! ");
            return false;
        }
        return true;
    }

    static int lastTick = 0;

    private Sequence generateSequence() {
        MidiEvent dummyMidiEvent;
        ShortMessage dummyShortMessage;
        Sequence dummySequence = null;
        Track[] allTracks;
        Track theTrack;
        try {
            dummySequence = new Sequence(Sequence.PPQ, 1500);
        } catch (InvalidMidiDataException e) {
            System.out.println("O o " + e);
        }
        dummySequence.createTrack();
        allTracks = dummySequence.getTracks();
        theTrack = allTracks[0];
        lastTick = 0;
        for (int i = 0; i < 20; i++) {
            theTrack.add(this.createShortMidiEvent(ShortMessage.NOTE_ON, 2, 30 + i, 100, 100 + 1000 * i));
            theTrack.add(this.createMetaMidiEvent(1, "start", 100 + 1000 * i));
            lastTick = (1000 * i) + 600;
            theTrack.add(this.createShortMidiEvent(ShortMessage.NOTE_OFF, 2, 30 + i, 100, lastTick));
            theTrack.add(this.createMetaMidiEvent(1, "end", lastTick));
        }
        return dummySequence;
    }

    public MidiEvent createShortMidiEvent(int theCommand, int theChannel, int theData1, int theData2, long theTime) {
        ShortMessage dummyShortMessage;
        MidiEvent dummyMidiEvent;
        try {
            dummyShortMessage = new ShortMessage();
            dummyShortMessage.setMessage(theCommand, theChannel, theData1, theData2);
            dummyMidiEvent = new MidiEvent(dummyShortMessage, theTime);
        } catch (Exception e) {
            System.out.println(this.getClass() + "\t" + e);
            return null;
        }
        return dummyMidiEvent;
    }

    public MidiEvent createMetaMidiEvent(int theType, String theData1, long theTime) {
        MetaMessage dummyMetaMessage;
        MidiEvent dummyMidiEvent;
        try {
            dummyMetaMessage = new MetaMessage();
            dummyMetaMessage.setMessage(theType, theData1.getBytes(), theData1.length());
            dummyMidiEvent = new MidiEvent(dummyMetaMessage, theTime);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        return dummyMidiEvent;
    }

    public void meta(MetaMessage p1) {
        if (p1.getType() == 47) {
            return;
        }
        System.out.println("getTickPosition:\t" + theSequencer.getTickPosition() + "\t Sequencer.getTickLength:\t" + theSequencer.getTickLength() + "\tReal Length:\t" + lastTick + "\t Sequence.getTickLength:\t" + theSequence.getTickLength());
    }

    public void checkLengths() throws Exception {
        System.out.println("Sequencer.getTickLength() = " + theSequencer.getTickLength());
        System.out.println("Sequence.getTickLength() = " + theSequence.getTickLength());
        long diff = theSequencer.getTickLength() - theSequence.getTickLength();
        if (diff > 100 || diff < -100) {
            throw new Exception("Difference too large! Failed.");
        }
        System.out.println("Passed");
    }

    public static void main(String[] args) throws Exception {
        if (!hasSequencer()) {
            return;
        }
        TickLength tlt = new TickLength();
        tlt.checkLengths();
    }

    static boolean hasSequencer() {
        try {
            Sequencer seq = MidiSystem.getSequencer();
            if (seq != null) {
                seq.open();
                seq.close();
                return true;
            }
        } catch (Exception e) {
        }
        System.out.println("No sequencer available! Cannot execute test.");
        return false;
    }
}
