

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


public class MetaCallback implements MetaEventListener {

    static ShortMessage MidiMsg3(int a, int b, int c) {
        try {
            ShortMessage msg = new ShortMessage();
            msg.setMessage((byte)a,(byte)b,(byte)c);
            return msg;
        } catch(InvalidMidiDataException ex) {
            throw new RuntimeException();
        }
    }

    
    Instrument[] instruments;
    Sequencer sequencer;
    Sequence sequence;
    Track track;

    public static int TOTAL_COUNT = 100;

    int metaCount = 0;
    boolean finished = false;

    MetaCallback() throws Exception {

        sequencer=MidiSystem.getSequencer();
        sequence=new Sequence(Sequence.PPQ,240);
        track=sequence.createTrack();
        sequencer.addMetaEventListener(this);

        byte[] data = new byte[1];

        track.add(new MidiEvent(MidiMsg3(ShortMessage.NOTE_ON+0,45,100),0));
        track.add(new MidiEvent(MidiMsg3(ShortMessage.NOTE_ON+0,45,0),0 + 240));
        int c;
        for(c=0; c < TOTAL_COUNT; c++) {
            data[0]=(byte)(c+1);
            MetaMessage meta = new MetaMessage();
            meta.setMessage(1, data, 1); 
            track.add(new MidiEvent(meta,c*20));
        }
        track.add(new MidiEvent(MidiMsg3(ShortMessage.NOTE_ON+9,45,100),c*20));
        track.add(new MidiEvent(MidiMsg3(ShortMessage.NOTE_ON+9,45,0),c*20 + 10));

        sequencer.setSlaveSyncMode(Sequencer.SyncMode.INTERNAL_CLOCK);
        sequencer.setMasterSyncMode(Sequencer.SyncMode.INTERNAL_CLOCK);
        sequencer.open();
        sequencer.setSequence(sequence);
        sequencer.setTempoInBPM(100);
        System.out.println("Starting playback...");
        this.start();
        while (!finished && sequencer.getTickPosition() < sequencer.getTickLength()) {
            System.out.println("Tick "+sequencer.getTickPosition()+"...");
            Thread.sleep(1000);
        }
        System.out.println("Stopping playback...");
        this.stop();
        if (metaCount != TOTAL_COUNT) {
            throw new Exception("Expected "+TOTAL_COUNT+" callbacks, but got "+metaCount+"!");
        }
    }
    void start() {sequencer.start();}
    void stop() {sequencer.stop();}

    public void meta(MetaMessage msg) {
        System.out.println(""+metaCount+": got "+msg);
        if (msg.getType() == 0x2F) {
            finished = true;
        } else if (msg.getData().length > 0 && msg.getType() == 1) {
            metaCount++;
        }
    }

    public static void main(String[] argv) throws Exception {
        if (hasSequencer()) {
            new MetaCallback();
            System.out.println("Test passed");
        }
    }

    static boolean hasSequencer() {
        try {
            Sequencer seq = MidiSystem.getSequencer();
            if (seq != null) {
                seq.open();
                seq.close();
                return true;
            }
        } catch (Exception e) {}
        System.out.println("No sequencer available! Cannot execute test.");
        return false;
    }
}
