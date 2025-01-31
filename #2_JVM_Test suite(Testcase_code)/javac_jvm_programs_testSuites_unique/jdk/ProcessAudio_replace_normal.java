



import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.sampled.*;

import com.sun.media.sound.*;

public class ProcessAudio_replace_normal {

    private static void assertEquals(Object a, Object b) throws Exception
    {
        if(!a.equals(b))
            throw new RuntimeException("assertEquals fails!");
    }

    private static void assertTrue(boolean value) throws Exception
    {
        if(!value)
            throw new RuntimeException("assertTrue fails!");
    }

    public static void main(String[] args) throws Exception {
        SoftSynthesizer synth = new SoftSynthesizer();
        synth.openStream(null, null);

        SoftAudioBuffer in1 = new SoftAudioBuffer(250, synth.getFormat());
        SoftAudioBuffer in2 = new SoftAudioBuffer(250, synth.getFormat());
        SoftAudioBuffer out1 = new SoftAudioBuffer(250, synth.getFormat());
        SoftAudioBuffer out2 = new SoftAudioBuffer(250, synth.getFormat());

        float[] testdata1 = new float[in1.getSize()];
        float[] testdata2 = new float[in2.getSize()];
        float[] n1a = in1.array();
        float[] n2a = in2.array();
        float[] out1a = out1.array();
        float[] out2a = out2.array();
        for (int i = 0; i < n1a.length; i++) {
            testdata1[i] = (float)Math.sin(i*0.3)*0.9f;
            testdata2[i] = (float)Math.sin(i*0.4)*0.9f;
            n1a[i] = testdata1[i];
            n2a[i] = testdata2[i];
            out1a[i] = 1;
            out2a[i] = 1;
        }

        SoftLimiter limiter = new SoftLimiter();
        limiter.init(44100, 147);
        limiter.setMixMode(false);
        limiter.setInput(0, in1);
        limiter.setInput(1, in2);
        limiter.setOutput(0, out1);
        limiter.setOutput(1, out2);
        limiter.processControlLogic();
        limiter.processAudio();
        limiter.processControlLogic();
        limiter.processAudio();
        
        
        for (int i = 0; i < n1a.length; i++) {
            if(Math.abs(out1a[i] - testdata1[i]) > 0.00001)
                throw new Exception("input != output");
            if(Math.abs(out2a[i] - testdata2[i]) > 0.00001)
                throw new Exception("input != output");
        }

        synth.close();
    }
}
