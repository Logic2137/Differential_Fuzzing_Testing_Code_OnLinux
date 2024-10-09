

import java.io.ByteArrayInputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;


public final class EndlessLoopHugeLengthWave {

    
    private static byte[] headerWAV = {0x52, 0x49, 0x46, 0x46, 
            0x7, 0xF, 0xF, 0xF, 
            0x57, 0x41, 0x56, 0x45, 
            0x66, 0x6d, 0x74, 0x20, 
            1, 2, 3, 4, 
            3, 0,
            1, 0, 
            1, 1, 
            1, 0, 0, 0, 
            0, 1, 
            1, 0, 
            0x64, 0x61, 0x74, 0x61, 
    };

    public static void main(final String[] args) throws Exception {
        try {
            AudioSystem.getAudioFileFormat(new ByteArrayInputStream(headerWAV));
        } catch (final UnsupportedAudioFileException ignored) {
            
        }
    }
}
