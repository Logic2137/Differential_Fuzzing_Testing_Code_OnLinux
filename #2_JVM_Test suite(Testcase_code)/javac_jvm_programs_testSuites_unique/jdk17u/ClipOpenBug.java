import java.io.ByteArrayInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;

public class ClipOpenBug {

    public static void main(String[] args) throws Exception {
        boolean res = true;
        try {
            AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(new byte[2000]), new AudioFormat(8000.0f, 8, 1, false, false), 2000);
            AudioFormat format = ais.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format, ((int) ais.getFrameLength() * format.getFrameSize()));
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open();
            FloatControl rateControl = (FloatControl) clip.getControl(FloatControl.Type.SAMPLE_RATE);
            int c = 0;
            while (c++ < 10) {
                clip.stop();
                clip.setFramePosition(0);
                clip.start();
                for (float frq = 22000; frq < 44100; frq = frq + 100) {
                    try {
                        Thread.currentThread().sleep(20);
                    } catch (Exception e) {
                        break;
                    }
                    rateControl.setValue(frq);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            res = ex.getMessage().indexOf("This method should not have been invoked!") < 0;
        }
        if (res) {
            System.out.println("Test passed");
        } else {
            System.out.println("Test failed");
            throw new Exception("Test failed");
        }
    }
}
