

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


public class SMPTEDuration {

    public static void main(String args[]) throws Exception {
         int[][] dataMes =  { {ShortMessage.NOTE_ON, 10, 0x24, 0x50} ,
                 { ShortMessage.NOTE_OFF, 10, 0x24, 0x44 },
                 { ShortMessage.NOTE_ON, 10, 0x24, 0x50 },
                 { ShortMessage.NOTE_ON, 10, 0x26, 0x50 },
                 { ShortMessage.NOTE_OFF, 10, 0x26, 0x53 } };
         long[] ticks = { 0, 68, 240, 240, 286};
         int res = 240;
         ShortMessage msg;
         Sequence midiData = null;
         Track track;
         boolean failed = false;


         try {
             midiData = new Sequence(Sequence.SMPTE_24 , res);
         } catch (InvalidMidiDataException invMidiEx) {
             invMidiEx.printStackTrace(System.out);
             System.out.println("Unexpected InvalidMidiDataException: "
                     + invMidiEx.getMessage());
             failed = true;
         }
         track = midiData.createTrack();
         for (int i = 0; i < dataMes.length; i++) {
             msg = new ShortMessage();
             try {
                 msg.setMessage(dataMes[i][0], dataMes[i][1], dataMes[i][2],
                         dataMes[i][3]);
             } catch (InvalidMidiDataException invMidiEx) {
                 invMidiEx.printStackTrace(System.out);
                 System.out.println("Unexpected InvalidMidiDataException: "
                         + invMidiEx.getMessage());
                 failed = true;
             }
             track.add(new MidiEvent(msg, ticks[i]));
         }
         
         long micros = (long) ((midiData.getTickLength() * 1000000) / (res * Sequence.SMPTE_24));
         if (midiData.getMicrosecondLength() != micros) {
             failed = true;
             System.out.println("getMicrosecondLength() returns wrong length: "
                     + midiData.getMicrosecondLength());
             System.out.println("getMicrosecondLength() must return length: "
                     + micros);
         }
         if (midiData.getTickLength() != 286) {
             failed = true;
             System.out.println("getTickLength() returns wrong length: "
                     + midiData.getTickLength());
         }

         if( failed == true ) {
             throw new Exception("test failed");
         } else {
             System.out.println("Passed.");
         }
    }
}
