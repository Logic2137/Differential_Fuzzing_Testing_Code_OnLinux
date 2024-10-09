

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;


public class UnexpectedIAE {

    public static void main(String argv[]) throws Exception {
        boolean success = true;

        Mixer.Info [] infos = AudioSystem.getMixerInfo();

        for (int i=0; i<infos.length; i++) {
            Mixer mixer = AudioSystem.getMixer(infos[i]);
            System.out.println("Mixer is: " + mixer);
            Line.Info [] target_line_infos = mixer.getTargetLineInfo();
            for (int j = 0; j < target_line_infos.length; j++) {
                try {
                    System.out.println("Trying to get:" + target_line_infos[j]);
                    mixer.getLine(target_line_infos[j]);
                } catch (IllegalArgumentException iae) {
                    System.out.println("Unexpected IllegalArgumentException raised:");
                    iae.printStackTrace();
                    success = false;
                } catch (LineUnavailableException lue) {
                    System.out.println("Unexpected LineUnavailableException raised:");
                    lue.printStackTrace();
                    success = false;
                }
            }
        }
        if (success) {
            System.out.println("Test passed");
        } else {
            throw new Exception("Test FAILED");
        }
    }
}
