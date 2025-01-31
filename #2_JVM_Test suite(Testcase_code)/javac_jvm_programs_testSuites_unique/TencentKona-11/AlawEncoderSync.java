



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class AlawEncoderSync {

    static final int THREAD_COUNT   = 20;

    static final AudioFormat pcmFormat = new AudioFormat(8000f, 16, 2, true, false);
    static final int STREAM_LENGTH = 10;    
    static byte[] pcmBuffer;
    static final AudioFormat alawFormat
            = new AudioFormat(AudioFormat.Encoding.ALAW, 8000f, 8, 2, 2, 8000f, false);

    static final ConversionThread[] threads = new ConversionThread[THREAD_COUNT];

    public static void main(String[] args) {
        preparePCMBuffer();
        log("pcmStream size: " + pcmBuffer.length);

        for (int i=0; i<THREAD_COUNT; i++) {
            threads[i] = new ConversionThread(i);
            threads[i].start();
        }

        for (int i=0; i<THREAD_COUNT; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ex) {
                log("Main thread was interrupted, exiting.");
                return;
            }
        }

        int failed = 0;
        log("comparing result arrays...");
        for (int i=1; i<THREAD_COUNT; i++) {
            if (!Arrays.equals(threads[0].resultArray, threads[i].resultArray)) {
                failed++;
                log("NOT equals: 0 and " + i);
            }
        }
        if (failed > 0) {
            throw new RuntimeException("test FAILED");
        }
        log("test PASSED.");
    }


    static void preparePCMBuffer() {
        pcmBuffer = new byte[STREAM_LENGTH * (int)pcmFormat.getSampleRate()
                * (pcmFormat.getSampleSizeInBits() / 8) * pcmFormat.getChannels()];
        for (int i=0; i<pcmBuffer.length; i++) {
            pcmBuffer[i] = (byte)(Math.random() * 256.0 - 128.0);
        }
    }

    static AudioInputStream createPCMStream() {
        InputStream byteStream = new ByteArrayInputStream(pcmBuffer);
        return new AudioInputStream(byteStream, pcmFormat, AudioSystem.NOT_SPECIFIED);
    }

    static class ConversionThread extends Thread {
        public final int num;
        public byte[] resultArray = null;
        public ConversionThread(int num) {
            this.num = num;
        }
        @Override
        public void run() {
            log("ConversionThread[" + num + "] started.");
            try {
                InputStream inStream = new ByteArrayInputStream(pcmBuffer);

                AudioInputStream pcmStream = new AudioInputStream(
                        inStream, pcmFormat, AudioSystem.NOT_SPECIFIED);
                AudioInputStream alawStream = AudioSystem.getAudioInputStream(alawFormat, pcmStream);

                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                int read = 0;
                byte[] data = new byte[4096];
                while((read = alawStream.read(data)) != -1) {
                    outStream.write(data, 0, read);
               }
               alawStream.close();
               resultArray = outStream.toByteArray();
            } catch (Exception ex) {
                log("ConversionThread[" + num + "] exception:");
                log(ex);
            }
            log("ConversionThread[" + num + "] completed.");
        }
    }

    static void log(String s) {
        System.out.println(s);
    }

    static void log(Exception ex) {
        ex.printStackTrace(System.out);
    }
}
