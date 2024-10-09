

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;


public class bug6416024 {

    boolean test() {
        Sequence sequence = null;
        Track track = null;
        MidiEvent event = null;

        log("creating sequence...");
        try {
            sequence = new Sequence(Sequence.PPQ, 10);
            log("  - OK: " + sequence);
        } catch(InvalidMidiDataException e ) {
            log("  - FAILED: got exception");
            e.printStackTrace(System.out);
            return false;
        }

        log("creating track...");
        track = sequence.createTrack();
        log("  - OK: " + track);
        log("initial track size=" + track.size());

        log("removing all track events...");
        while (track.size() > 0) {
            try {
                event = track.get(0);
                log("  ..removing event " + event);
                track.remove(event);
                log("    - OK, track size=" + track.size());
            } catch (Exception e) {
                log("  - FAILED: got exception");
                e.printStackTrace(System.out);
                return false;
            }
        }

        MetaMessage newMsg = new MetaMessage();
        MidiEvent newEvent = new MidiEvent(newMsg, 10);
        log("adding new event...");
        try {
            if (!track.add(newEvent)) {
                log("event hasn't been added");
                return false;
            }
            log("    - OK, track size=" + track.size());
        } catch (Exception e) {
            log("  - FAILED: got exception");
            e.printStackTrace(System.out);
            return false;
        }

        return true;
    }

    public static void main(String args[]) throws Exception {
        bug6416024 This = new bug6416024();
        if (This.test()) {
            log("Test passed sucessfully.");
        } else {
            log("Test FAILED!");
            delay(1000);
            throw new RuntimeException("Test failed!");
        }
    }

    
    static long startTime = currentTimeMillis();
    static long currentTimeMillis() {
        
        return System.currentTimeMillis();
    }
    static void log(String s) {
        long time = currentTimeMillis() - startTime;
        long ms = time % 1000;
        time /= 1000;
        long sec = time % 60;
        time /= 60;
        long min = time % 60;
        time /= 60;
        System.out.println(""
                + (time < 10 ? "0" : "") + time
                + ":" + (min < 10 ? "0" : "") + min
                + ":" + (sec < 10 ? "0" : "") + sec
                + "." + (ms < 10 ? "00" : (ms < 100 ? "0" : "")) + ms
                + " (" + Thread.currentThread().getName() + ") " + s);
    }
    static void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {}
    }
}
