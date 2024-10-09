import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;

public class FrameSizeTest {

    public static void main(String[] args) throws Exception {
        boolean res = true;
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        for (int i = 0; i < mixerInfo.length; i++) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo[i]);
            System.out.println(mixer);
            Line.Info[] lineinfo = mixer.getSourceLineInfo();
            for (int j = 0; j < lineinfo.length; j++) {
                System.out.println("  " + lineinfo[j]);
                try {
                    AudioFormat[] formats = ((DataLine.Info) lineinfo[j]).getFormats();
                    for (int k = 0; k < formats.length; k++) {
                        if ((formats[k].getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) || formats[k].getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) && (formats[k].getFrameSize() != AudioSystem.NOT_SPECIFIED) && ((formats[k].getSampleSizeInBits() == 16) || (formats[k].getSampleSizeInBits() == 8)) && ((((formats[k].getSampleSizeInBits() + 7) / 8) * formats[k].getChannels()) != formats[k].getFrameSize())) {
                            System.out.println(" # " + formats[k] + ", getFrameSize() wrongly returns" + formats[k].getFrameSize());
                            res = false;
                        }
                    }
                } catch (ClassCastException e) {
                }
            }
        }
        if (res) {
            System.out.println("Test passed");
        } else {
            System.out.println("Test failed");
            throw new Exception("Test failed");
        }
    }
}
