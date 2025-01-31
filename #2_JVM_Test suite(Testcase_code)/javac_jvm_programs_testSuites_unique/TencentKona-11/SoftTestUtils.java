

import java.io.IOException;

import javax.sound.midi.*;
import javax.sound.sampled.*;

import com.sun.media.sound.*;

public class SoftTestUtils {

    public AudioSynthesizer synth = new SoftSynthesizer();
    public AudioInputStream stream;
    public byte[] tmpbuffer = new byte[1024];

    public static SF2Soundbank createTestSoundBank()
    {
        SF2Soundbank sf2 = new SF2Soundbank();
        AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
        float[] data = new float[44100+1000];
        float fr = 440/format.getSampleRate();
        for (int i = 0; i < data.length; i++)
            data[i] = (float)Math.sin(i*fr*2*Math.PI);
        byte[] bdata = new byte[data.length*format.getFrameSize()];
        AudioFloatConverter.getConverter(format).toByteArray(data, bdata);
        SF2Sample sample = new SF2Sample(sf2);
        sample.setName("Test Sample");
        sample.setData(bdata);
        sample.setStartLoop(500);
        sample.setEndLoop(data.length - 500);
        sample.setSampleRate((long) format.getSampleRate());
        sample.setOriginalPitch(69);
        sf2.addResource(sample);
        SF2Layer layer = new SF2Layer(sf2);
        layer.setName("Test Layer");
        sf2.addResource(layer);
        SF2LayerRegion region = new SF2LayerRegion();
        region.putInteger(SF2Region.GENERATOR_SAMPLEMODES, 1);
        region.setSample(sample);
        layer.getRegions().add(region);
        SF2Instrument ins = new SF2Instrument(sf2);
        ins.setName("Test Instrument");
        sf2.addInstrument(ins);
        SF2InstrumentRegion insregion = new SF2InstrumentRegion();
        insregion.setLayer(layer);
        ins.getRegions().add(insregion);

        return sf2;
    }

    public SoftTestUtils() throws Exception {
        stream = synth.openStream(null, null);
        synth.unloadAllInstruments(synth.getDefaultSoundbank());
        synth.loadAllInstruments(createTestSoundBank());
    }

    public void close() throws Exception {
        stream.close();
        stream = null;
        synth.close();
        synth = null;
    }

    public void read(double seconds) throws IOException
    {
        int bufflen =
           stream.getFormat().getFrameSize() *
           (int)(stream.getFormat().getFrameRate() * seconds);
        while(bufflen != 0)
        {
            if(bufflen > 1024)
                bufflen -= stream.read(tmpbuffer,0,1024);
            else
                bufflen -= stream.read(tmpbuffer,0, bufflen);
        }
    }

    public VoiceStatus findVoice(int channel, int note) {
        VoiceStatus[] v = synth.getVoiceStatus();
        for (int k = 0; k < v.length; k++)
            if(v[k].active)
                if(v[k].channel == channel)
                    if(v[k].note == note)
                        return v[k];
        return null;
    }

}
