import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class TrackAddSameTick {

    static boolean failed = false;

    static MidiEvent[] evs = new MidiEvent[10];

    public static void main(String[] argv) throws Exception {
        Sequence seq = new Sequence(Sequence.PPQ, 240);
        Track t = seq.createTrack();
        log("add 10 events in random order");
        t.add(createEvent(10, 5));
        t.add(createEvent(0, 0));
        t.add(createEvent(10, 6));
        t.add(createEvent(11, 8));
        t.add(createEvent(10, 7));
        t.add(createEvent(0, 1));
        t.add(createEvent(0, 2));
        t.add(createEvent(15, 9));
        t.add(createEvent(0, 3));
        t.add(createEvent(1, 4));
        long lastTick = 0;
        for (int i = 0; i < t.size(); i++) {
            MidiEvent ev = t.get(i);
            if (ev.getMessage() instanceof ShortMessage) {
                ShortMessage msg = (ShortMessage) ev.getMessage();
                log("" + i + ": ShortMessage at tick " + ev.getTick() + " with expected position " + msg.getData1());
                if (ev.getTick() < lastTick) {
                    log("  FAILED: last tick is larger than this event's tick!");
                    failed = true;
                }
                if (i != msg.getData1()) {
                    log("  FAILED: Track did not order correctly.");
                    failed = true;
                }
            }
        }
        if (failed)
            throw new Exception("Test FAILED!");
        log("Test passed.");
    }

    public static MidiEvent createEvent(long tick, int expectedPos) throws Exception {
        ShortMessage msg = new ShortMessage();
        msg.setMessage(0x90, (int) expectedPos, 00);
        MidiEvent ev = new MidiEvent(msg, tick);
        return ev;
    }

    public static void log(String s) {
        System.out.println(s);
    }

    public static void log(Exception e) {
        e.printStackTrace();
    }
}
