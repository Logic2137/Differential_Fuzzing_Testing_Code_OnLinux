import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class bug6188860 {

    public static void main(String[] args) throws Exception {
        byte[] testData = new byte[256];
        for (int i = 0; i < testData.length; i++) testData[i] = (byte) (i % 128);
        InputStream streamSrc = new TestInputStream(testData);
        AudioFormat format = new AudioFormat(44100.0f, 8, 1, false, false);
        AudioInputStream streamAudio = new AudioInputStream(streamSrc, format, AudioSystem.NOT_SPECIFIED);
        int nErrCount = 0;
        int nTotal = 0;
        int dataSrc, dataRead;
        while (nTotal < (testData.length - 1)) {
            dataRead = streamAudio.read();
            if (dataRead < 0) {
                System.out.println("end of stream");
                break;
            }
            dataSrc = testData[nTotal];
            if (dataRead != dataSrc) {
                System.out.println("" + nTotal + " - mismatch :" + dataRead + " <> " + dataSrc);
                nErrCount++;
            }
            nTotal++;
        }
        System.out.println("Total: " + nTotal + "; Mismatches: " + nErrCount);
        if (nErrCount > 0) {
            throw new RuntimeException("test failed: " + nErrCount + " mismatches of total " + nTotal + " bytes.");
        }
        System.out.println("Test sucessfully passed.");
    }

    static class TestInputStream extends InputStream {

        byte[] data;

        int pos = 0;

        TestInputStream(byte[] data) {
            this.data = data;
        }

        public int read() throws IOException {
            if (pos >= data.length) {
                return -1;
            }
            return data[pos++] & 0xFF;
        }
    }
}
