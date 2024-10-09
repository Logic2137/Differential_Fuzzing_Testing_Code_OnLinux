import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class SeqRecordDoesNotCopy {

    public static void main(String[] argv) {
        Sequencer s = null;
        try {
            s = MidiSystem.getSequencer();
            s.open();
        } catch (final MidiUnavailableException ignored) {
            return;
        }
        try {
            Sequence seq = new Sequence(Sequence.PPQ, 384, 2);
            s.setSequence(seq);
            Track t = seq.getTracks()[0];
            ShortMessage msg = new ShortMessage();
            msg.setMessage(0x90, 0x40, 0x7F);
            t.add(new MidiEvent(msg, 11000));
            msg.setMessage(0x90, 0x40, 0x00);
            t.add(new MidiEvent(msg, 12000));
            t = seq.getTracks()[1];
            s.recordEnable(t, -1);
            System.out.println("Started recording...");
            s.startRecording();
            Receiver r = s.getReceiver();
            Thread.sleep(100);
            System.out.println("Recording a normal NOTE ON message...");
            msg.setMessage(0x90, 0x40, 0x6F);
            r.send(msg, -1);
            Thread.sleep(100);
            System.out.println("Recording a normal NOTE OFF message...");
            msg.setMessage(0x90, 0x40, 0x00);
            r.send(msg, -1);
            Thread.sleep(100);
            s.stop();
            System.out.println("Recorded messages:");
            int sameMessage = 0;
            for (int i = 0; i < t.size(); i++) {
                System.out.print(" " + (i + 1) + ". ");
                printEvent(t.get(i));
                if (t.get(i).getMessage() == msg) {
                    System.out.println("## Failed: Same Message reference!");
                    sameMessage++;
                }
            }
            if (sameMessage > 0) {
                System.out.println("## Failed: The same instance was recorded!");
                throw new Exception("Test FAILED!");
            }
            System.out.println("Did not detect any duplicate messages.");
            System.out.println("Test passed.");
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e);
            throw new RuntimeException("Test FAILED!");
        } finally {
            s.close();
        }
    }

    public static void printEvent(MidiEvent event) {
        MidiMessage message = event.getMessage();
        long tick = event.getTick();
        byte[] data = message.getMessage();
        StringBuffer sb = new StringBuffer((data.length * 3) - 1);
        for (int i = 0; i < data.length; i++) {
            sb.append(toHexByteString(data[i]));
            if (i < data.length - 1)
                sb.append(' ');
        }
        System.out.printf("%5d: %s%n", tick, sb);
    }

    private static String toHexByteString(int n) {
        if (n < 0)
            n &= 0xFF;
        String s = Integer.toHexString(n).toUpperCase();
        if (s.length() == 1)
            s = '0' + s;
        return s;
    }
}
