import java.io.ByteArrayInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class SkipOnConvertSampleSize {

    private static final int TEST_FRAME_LENGTH = 20000;

    private static void testskipping(final Encoding encoding) throws Exception {
        int pcmBufSize = TEST_FRAME_LENGTH * 2;
        byte[] tempAudioBuf = new byte[pcmBufSize];
        for (int i = 0; i < TEST_FRAME_LENGTH; i++) {
            tempAudioBuf[i * 2] = (byte) ((Math.random() - 1) * Byte.MAX_VALUE);
            tempAudioBuf[i * 2 + 1] = (byte) ((Math.random() - 1) * Byte.MAX_VALUE);
        }
        final ByteArrayInputStream bis = new ByteArrayInputStream(tempAudioBuf);
        AudioFormat format = new AudioFormat(8000, 16, 1, true, false);
        final AudioInputStream testAis = new AudioInputStream(bis, format, TEST_FRAME_LENGTH);
        final AudioFormat lawFormat;
        final byte[] alawAudioBuf;
        try (AudioInputStream lawStream = AudioSystem.getAudioInputStream(encoding, testAis)) {
            lawFormat = lawStream.getFormat();
            int alawFrameSize = lawFormat.getFrameSize();
            int lawBufSize = TEST_FRAME_LENGTH * alawFrameSize;
            alawAudioBuf = new byte[lawBufSize];
            int r1 = 0;
            int totalRead = 0;
            while ((r1 = lawStream.read(alawAudioBuf, totalRead, lawBufSize - totalRead)) != -1) {
                totalRead += r1;
            }
        }
        ByteArrayInputStream alawBis = new ByteArrayInputStream(alawAudioBuf);
        AudioInputStream lawAis = new AudioInputStream(alawBis, lawFormat, TEST_FRAME_LENGTH);
        try (AudioInputStream convPcmStream = AudioSystem.getAudioInputStream(Encoding.PCM_SIGNED, lawAis)) {
            final AudioFormat convPcmAudioFormat = convPcmStream.getFormat();
            final int convPcmFrameSize = convPcmAudioFormat.getFrameSize();
            final long toSkip = (TEST_FRAME_LENGTH / 2) * convPcmFrameSize;
            long skipped = 0;
            do {
                skipped += convPcmStream.skip(toSkip - skipped);
            } while (skipped < toSkip);
            int r2 = convPcmStream.read(new byte[convPcmFrameSize]);
            if (r2 == -1) {
                throw new RuntimeException("Skip method of decoder not correctly implemented!");
            }
        }
    }

    public static void main(final String[] args) throws Exception {
        testskipping(Encoding.ALAW);
        testskipping(Encoding.ULAW);
    }
}
