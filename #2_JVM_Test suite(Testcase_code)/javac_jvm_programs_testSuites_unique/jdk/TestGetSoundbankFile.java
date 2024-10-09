



import java.io.File;

import javax.sound.midi.Patch;
import javax.sound.midi.Soundbank;

import com.sun.media.sound.SF2SoundbankReader;

public class TestGetSoundbankFile {

    private static void assertTrue(boolean value) throws Exception
    {
        if(!value)
            throw new RuntimeException("assertTrue fails!");
    }

    public static void main(String[] args) throws Exception {
        File file = new File(System.getProperty("test.src", "."), "ding.sf2");
        Soundbank sf2 = new SF2SoundbankReader().getSoundbank(file);
        assertTrue(sf2.getInstruments().length == 1);
        Patch patch = sf2.getInstruments()[0].getPatch();
        assertTrue(patch.getProgram() == 0);
        assertTrue(patch.getBank() == 0);
    }
}
