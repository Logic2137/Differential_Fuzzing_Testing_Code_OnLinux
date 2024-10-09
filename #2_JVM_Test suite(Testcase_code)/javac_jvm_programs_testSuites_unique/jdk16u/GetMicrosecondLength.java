

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


public class GetMicrosecondLength {

    public static boolean failed = false;
    

    public static void main(String[] args) throws Exception {
        
        for (int sec = 1; sec < 10; sec += 4) {
            for (int tempo=0; tempo < 1000; tempo+=120) {
                for (int resolution=1; resolution < 480; ) {
                    testSequence(sec, tempo, resolution);
                    if (resolution == 1) {
                        resolution = 120;
                    } else {
                        resolution += 120;
                    }
                }
            }
        }
        if (failed) throw new Exception("Test FAILED!");
        out("Test Passed.");
    }

    
    private static void testSequence(int lengthInSeconds, int tempoInBPM, int resolution) {
        Sequence sequence = null;
        long lengthInMicroseconds = lengthInSeconds * 1000000;
        boolean createTempoEvent = true;
        if (tempoInBPM == 0) {
            tempoInBPM = 120;
            createTempoEvent = false;
            System.out.print("Creating sequence: "+lengthInSeconds+"sec, "
                             +"resolution="+resolution+" ticks/beat...");
        } else {
            System.out.print("Creating sequence: "+lengthInSeconds+"sec, "
                             +tempoInBPM+" beats/min, "
                             +"resolution="+resolution+" ticks/beat...");
        }
        
        long lengthInTicks = (lengthInMicroseconds * tempoInBPM * resolution) / 60000000l;
        
        try {
            sequence = new Sequence(Sequence.PPQ, resolution);
            Track track = sequence.createTrack();
            if (createTempoEvent) {
                int tempoInMPQ = (int) (60000000l / tempoInBPM);
                MetaMessage tm = new MetaMessage();
                byte[] msg = new byte[3];
                msg[0] = (byte) (tempoInMPQ >> 16);
                msg[1] = (byte) ((tempoInMPQ >> 8) & 0xFF);
                msg[2] = (byte) (tempoInMPQ & 0xFF);

                tm.setMessage(0x51 , msg, msg.length);
                track.add(new MidiEvent(tm, 0));
                
                
            }
            ShortMessage mm = new ShortMessage();
            mm.setMessage(0xF6, 0, 0);
            MidiEvent me = new MidiEvent(mm, lengthInTicks);
            track.add(me);
            
        } catch (InvalidMidiDataException e) {
            out(e);
        }
        boolean thisFailed = false;
        long actualLengthInTicks = sequence.getTickLength();
        
        if (Math.abs(actualLengthInTicks - lengthInTicks) > lengthInTicks / 20) {
            out("FAILED:");
            out("  expected length in ticks: " + lengthInTicks);
            out("  actual length in ticks  : " + actualLengthInTicks);
            thisFailed = true;
        }
        long actualLengthInUs = sequence.getMicrosecondLength();
        
        if (Math.abs(actualLengthInUs - lengthInMicroseconds) > lengthInMicroseconds / 20) {
            if (!thisFailed) {
                out("FAILED:");
            }
            out("  expected length in microsecs: " + lengthInMicroseconds);
            out("  actual length in microsecs  : " + actualLengthInUs);
            thisFailed = true;
        }
        if (!thisFailed) {
            out("OK");
        }
        
        failed |= thisFailed;
    }


    private static void out(Throwable t) {
        t.printStackTrace(System.out);
    }

    private static void out(String message) {
        System.out.println(message);
    }
}
