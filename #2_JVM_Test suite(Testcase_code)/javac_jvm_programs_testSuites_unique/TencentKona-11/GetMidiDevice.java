



import javax.sound.midi.Receiver;

import com.sun.media.sound.AudioSynthesizer;
import com.sun.media.sound.SoftReceiver;
import com.sun.media.sound.SoftSynthesizer;

public class GetMidiDevice {

    public static void main(String[] args) throws Exception {

        AudioSynthesizer synth = new SoftSynthesizer();
        synth.openStream(null, null);
        Receiver recv = synth.getReceiver();
        if (((SoftReceiver) recv).getMidiDevice() != synth) {
            throw new Exception("SoftReceiver.getMidiDevice() doesn't return "
                    + "instance of the synthesizer");
        }
        synth.close();
    }
}

