

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;


public class BogusMixers {

    public static void main(String[] args) throws Exception {
        try {
            out("4667064: Java Sound provides bogus SourceDataLine and TargetDataLine");

            Mixer.Info[]    aInfos = AudioSystem.getMixerInfo();
            out("  available Mixers:");
            for (int i = 0; i < aInfos.length; i++) {
                if (aInfos[i].getName().startsWith("Java Sound Audio Engine")) {
                    Mixer mixer = AudioSystem.getMixer(aInfos[i]);
                    Line.Info[] tlInfos = mixer.getTargetLineInfo();
                    for (int ii = 0; ii<tlInfos.length; ii++) {
                        if (tlInfos[ii].getLineClass() == DataLine.class) {
                            throw new Exception("Bogus TargetDataLine with DataLine info present!");
                        }
                    }
                }
                if (aInfos[i].getName().startsWith("WinOS,waveOut,multi threaded")) {
                    throw new Exception("Bogus mixer 'WinOS,waveOut,multi threaded' present!");
                }
                out(aInfos[i].getName());
            }
            if (aInfos.length == 0)
            {
                out("[No mixers available] - not a failure of this test case.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        out("Test passed");
    }

    static void out(String s) {
        System.out.println(s); System.out.flush();
    }
}
