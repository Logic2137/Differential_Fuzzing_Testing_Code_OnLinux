

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;


public final class OpenNonIntegralNumberOfSampleframes {

    
    private static final List<AudioFormat> formats = new ArrayList<>(2900);

    private static final Encoding[] encodings = {
            Encoding.ALAW, Encoding.ULAW, Encoding.PCM_SIGNED,
            Encoding.PCM_UNSIGNED, Encoding.PCM_FLOAT
    };

    private static final int[] sampleRates = {
            8000, 11025, 16000, 32000, 44100
    };

    private static final int[] sampleBits = {
            4, 8, 11, 16, 20, 24, 32, 48, 64, 128
    };

    private static final int[] channels = {
            1, 2, 3, 4, 5, 6
    };

    static {
        for (final Boolean end : new boolean[]{false, true}) {
            for (final int sampleSize : sampleBits) {
                for (final int sampleRate : sampleRates) {
                    for (final int channel : channels) {
                        final int frameSize = ((sampleSize + 7) / 8) * channel;
                        if (frameSize == 1) {
                            
                            continue;
                        }
                        for (final Encoding enc : encodings) {
                            formats.add(
                                    new AudioFormat(enc, sampleRate, sampleSize,
                                                    channel, frameSize,
                                                    sampleRate, end));
                        }
                    }
                }
            }
        }
    }

    public static void main(final String[] args) {
        for (final AudioFormat af : formats) {
            try (Clip clip = AudioSystem.getClip()) {
                final int bufferSize = af.getFrameSize() + 1;
                try {
                    clip.open(af, new byte[100], 0, bufferSize);
                } catch (final IllegalArgumentException ignored) {
                    
                    continue;
                } catch (final LineUnavailableException e) {
                    
                    e.printStackTrace();
                }
                System.err.println("af = " + af);
                System.err.println("bufferSize = " + bufferSize);
                throw new RuntimeException("Expected exception is not thrown");
            } catch (IllegalArgumentException
                    | LineUnavailableException ignored) {
                
            }
        }
    }
}
