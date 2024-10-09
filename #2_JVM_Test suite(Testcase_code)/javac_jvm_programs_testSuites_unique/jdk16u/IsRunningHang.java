

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;


public final class IsRunningHang {

    private static CountDownLatch go;

    
    private static final List<AudioFormat> formats = new ArrayList<>();

    private static final AudioFormat.Encoding[] encodings = {
            AudioFormat.Encoding.ALAW, AudioFormat.Encoding.ULAW,
            AudioFormat.Encoding.PCM_SIGNED, AudioFormat.Encoding.PCM_UNSIGNED,
            AudioFormat.Encoding.PCM_FLOAT
    };

    private static final int[] sampleRates = {8000, 16000, 48000};

    private static final int[] sampleBits = {8, 16, 24, 32, 64};

    private static final int[] channels = {1, 2, 3, 5};

    static {
        for (final Boolean end : new boolean[]{false, true}) {
            for (final int sampleSize : sampleBits) {
                for (final int sampleRate : sampleRates) {
                    for (final int channel : channels) {
                        for (final AudioFormat.Encoding enc : encodings) {
                            int s = ((sampleSize + 7) / 8) * channel;
                            formats.add(new AudioFormat(enc, sampleRate,
                                                        sampleSize, channel,
                                                        s, sampleRate, end));
                        }
                    }
                }
            }
        }
    }

    public static void main(final String[] args) throws Exception {
        for (final AudioFormat format : formats) {
            System.out.println("format = " + format);
            
            byte[] soundData = new byte[(int) (format.getFrameRate()
                                                       * format.getFrameSize()
                                                       / 2)];
            try {
                test(format, soundData);
            } catch (LineUnavailableException | IllegalArgumentException ignored) {
                
            }
        }
    }

    private static void test(final AudioFormat format, final byte[] data)
            throws Exception {
        final Line.Info info = new DataLine.Info(Clip.class, format);
        final Clip clip = (Clip) AudioSystem.getLine(info);

        go = new CountDownLatch(1);
        clip.addLineListener(event -> {
            if (event.getType().equals(LineEvent.Type.START)) {
                go.countDown();
            }
        });

        clip.open(format, data, 0, data.length);
        clip.start();
        go.await();
        while (clip.isRunning()) {
            
        }
        while (clip.isActive()) {
            
        }
        clip.close();
    }
}
