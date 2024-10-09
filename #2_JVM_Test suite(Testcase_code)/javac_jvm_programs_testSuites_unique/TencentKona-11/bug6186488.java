

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;


public class bug6186488 {
    public static void main(String[] args) throws Exception {
        MidiDevice synth = null;

        try {
            synth = MidiSystem.getSynthesizer();
            

            System.out.println("Synthesizer: " + synth.getDeviceInfo());
            synth.open();
            MidiMessage msg = new GenericMidiMessage(0x90, 0x3C, 0x40);
            
            

            synth.getReceiver().send(msg, 0);
            Thread.sleep(2000);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            if (synth != null && synth.isOpen())
                synth.close();
        }
        System.out.print("Did you heard a note? (enter 'y' or 'n') ");
        int result = System.in.read();
        System.in.skip(1000);
        if (result == 'y' || result == 'Y')
        {
            System.out.println("Test passed sucessfully.");
        }
        else
        {
            System.out.println("Test FAILED.");
            throw new RuntimeException("Test failed.");
        }
    }

    private static class GenericMidiMessage extends MidiMessage {
        GenericMidiMessage(int... message) {
            super(new byte[message.length]);
            for (int i=0; i<data.length; i++) {
                data[i] = (byte)(0xFF & message[i]);
            }
        }

        GenericMidiMessage(byte... message) {
            super(message);
        }

        public Object clone() {
            return new GenericMidiMessage((byte[])data.clone());
        }
    }
}
