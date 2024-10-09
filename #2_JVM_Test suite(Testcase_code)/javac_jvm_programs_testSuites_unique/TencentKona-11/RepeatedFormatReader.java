

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;


public final class RepeatedFormatReader {

    

    private static byte[] headerMIDI = {0x4d, 0x54, 0x68, 0x64, 
                                        0, 0, 0, 6, 
                                        0, 0, 
                                        0, 0, 
                                        0, 1, 
    };

    private static byte[] headerAU = {0x2e, 0x73, 0x6e, 0x64, 
                                      0, 0, 0, 24, 
                                      0, 0, 0, 0, 
                                      0, 0, 0, 1, 
                                      0, 0, 0, 1, 
                                      0, 0, 0, 1  
    };

    private static byte[] headerWAV = {0x52, 0x49, 0x46, 0x46, 
                                       1, 1, 1, 1, 
                                       0x57, 0x41, 0x56, 0x45, 
                                       0x66, 0x6d, 0x74, 0x20, 
                                       3, 0, 0, 0, 
                                       1, 0, 
                                       0, 1, 
                                       0, 0, 0, 0, 
                                       0, 0, 0, 0, 
                                       0, 0, 
                                       1, 0, 
                                       0x64, 0x61, 0x74, 0x61, 
                                       0, 0, 0, 0, 
    };

    private static final byte[][] data = {headerMIDI, headerAU, headerWAV};

    public static void main(final String[] args)
            throws IOException, UnsupportedAudioFileException {
        for (final byte[] bytes : data) {
            test(bytes);
        }
    }

    private static void test(final byte[] buffer)
            throws IOException, UnsupportedAudioFileException {
        final InputStream is = new ByteArrayInputStream(buffer);
        for (int i = 0; i < 10; ++i) {
            AudioSystem.getAudioFileFormat(is);
        }
    }
}
