



import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.sampled.*;

import com.sun.media.sound.*;

public class Load9 {

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
        
        
        SoftTuning tuning = new SoftTuning();
        int[] msg = {0xf0,0x7f,0x7f,0x08,0x09,0x03,0x7f,0x7f,
                5,10,15,20,25,30,35,40,45,50,51,52,
                5,10,15,20,25,30,35,40,45,50,51,52,
                0xf7};
        int[] oct = {5,10,15,20,25,30,35,40,45,50,51,52,5,10,15,20,25,30,35,40,45,50,51,52};
        byte[] bmsg = new byte[msg.length];
        for (int i = 0; i < bmsg.length; i++)
            bmsg[i] = (byte)msg[i];
        tuning.load(bmsg);
        double[] tunings = tuning.getTuning();
        for (int i = 0; i < tunings.length; i++)
        {
            double c = (oct[(i%12)*2]*128 + oct[(i%12)*2+1] -8192)*(100.0/8192.0);
            assertTrue(Math.abs(tunings[i]-(i*100 + (c))) < 0.00001);
        }
    }
}
