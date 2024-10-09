

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;


public class AISReadFraction {

    static int failed = 0;
    static byte[] testData = new byte[256];
    static boolean DEBUG = false;

    static AudioFormat[] formats = {
        new AudioFormat(44100.0f, 8, 1, false, false), 
        new AudioFormat(44100.0f, 8, 2, false, false), 
        new AudioFormat(44100.0f, 16, 1, true, false), 
        new AudioFormat(44100.0f, 24, 1, true, false), 
        new AudioFormat(44100.0f, 16, 2, true, false), 
        new AudioFormat(44100.0f, 8, 5, false, false), 
        new AudioFormat(44100.0f, 16, 3, true, false), 
        new AudioFormat(44100.0f, 8, 7, false, false), 
        new AudioFormat(44100.0f, 32, 2, true, false)  
    };


    public static void main(String args[]) throws Exception {
        for (int i = 0; i<testData.length; i++) {
                testData[i] = (byte) (i % 128);
        }

        for (int f = 0; f < formats.length; f++) {
                
                doTest(formats[f], false);
                
                doTest(formats[f], true);
        }

        out(""+failed+" failures.");
        if (failed>0) throw new Exception("Test FAILED!");
        out("Test passed.");
    }

    static void doTest(AudioFormat format, boolean doMark) {
        out("Test with"+(doMark?"":"out")+" marking. Audio format: "
            +"sampleSize="+format.getSampleSizeInBits()+"bits "
            +"channels="+format.getChannels()+" "
            +"frameSize="+format.getFrameSize()+"byte(s)");
        int maxReadBytes = (testData.length / format.getFrameSize()) * format.getFrameSize();
        InputStream is = new FractionalIS(testData, doMark);
        AudioInputStream ais = new AudioInputStream(is, format, AudioSystem.NOT_SPECIFIED);
        
        if (ais.markSupported() && !doMark) {
                out("  #AIS reports markSupported, but underlying stream cannot! FAILED");
                failed ++;
        }
        if (!ais.markSupported() && doMark) {
                out("  #AIS does not report markSupported, but underlying stream can mark! FAILED");
                failed++;
        }
        byte[] data = new byte[1000];
        int frameSize = format.getFrameSize();
        int counter = 5;
        int totalReadBytes = 0;
        boolean hasRead0 = false;
        boolean hasMarked = false;
        boolean hasReset = false;
        int markPos = 0;
        while (true) {
            try {
                int toBeRead = frameSize * counter;
                counter += 3;
                if (counter > 14) {
                        counter -= 14;
                }
                int read = ais.read(data, 0, toBeRead);
                if (DEBUG) out("  -> ais.read(data, 0, "+toBeRead+"): "+read+"  (frameSize="+frameSize+")");
                if ((totalReadBytes == maxReadBytes) && (read != -1)
                     && ((read > 0) || hasRead0)) {
                        if (read == 0) {
                            out("  #stream was read to the end ("+maxReadBytes+"), but ais.read returned repeatedly 0 bytes. FAILED");
                        } else {
                            out("  #stream was read to the end ("+maxReadBytes+"), but ais.read returned "+read+" bytes... FAILED");
                        }
                        failed++;
                        break;
                }
                if (read > 0) {
                        verifyReadBytes(data, totalReadBytes, read);
                        if ((read % frameSize) != 0) {
                                out("  #Read non-integral number of frames: "+read+" bytes, frameSize="+frameSize+" bytes. FAILED");
                                failed++;
                        }
                        totalReadBytes += read;
                        hasRead0 = false;
                }
                else if (read == 0) {
                        
                        if (hasRead0) {
                                out("  read 0 twice in a row! FAILED");
                                failed++;
                                break;
                        }
                        hasRead0 = true;
                } else {
                        
                        out("  End of stream reached. Total read bytes: "+totalReadBytes);
                        if (totalReadBytes != maxReadBytes) {
                                out("  #Failed: should have read "+maxReadBytes+" bytes! FAILED.");
                                failed++;
                        }
                        break;
                }

                
                if (totalReadBytes > 50 && !hasMarked && !hasReset && doMark) {
                        out("  Marking at position "+totalReadBytes);
                        hasMarked = true;
                        ais.mark(0);
                        markPos = totalReadBytes;
                }
                if (totalReadBytes > 100 && hasMarked && !hasReset && doMark) {
                        out("  Resetting at position "+totalReadBytes+" back to "+markPos);
                        hasReset = true;
                        ais.reset();
                        totalReadBytes = markPos;
                }

            } catch (IOException e) {
                out("  #caught unexpected exception:");
                e.printStackTrace();
                failed++;
            }
        }
    }

    static void verifyReadBytes(byte[] data, int offset, int len) {
        int firstWrongByte = -1;
        for (int i = 0; i < len; i++) {
                int expected = ((offset + i) % 128);
                if (data[i] != expected) {
                        out("  read data is not correct! offset="+offset+"  expected="+expected+"  actual="+data[i]);
                        failed++;
                        break;
                }
        }
    }


    public static void out(String s) {
        System.out.println(s);
    }


    static class FractionalIS extends InputStream {
        byte[] data;
        int pos = 0;
        boolean canMark;
        
        int missingBytes = 0;
        int markPos = -1;

        FractionalIS(byte[] data, boolean canMark) {
                this.data = data;
                this.canMark = canMark;
        }

        public int read() throws IOException {
                if (pos >= data.length) {
                        return -1;
                }
                return data[pos++] & 0xFF;
        }

        public int read(byte[] b, int off, int len) throws IOException {
                if (++missingBytes > 5) {
                        missingBytes = 0;
                }
                int reducedLen = len - missingBytes;
                if (reducedLen <= 0) reducedLen = 1;
                if (DEBUG) out("  FIS.read(data, 0, "+len+"): reducing len to "+reducedLen+" bytes.");
                int ret = super.read(b, off, reducedLen);
                if (DEBUG) out("                              returning "+ret+" bytes. Now at pos="+pos);
                return ret;
        }

        public void mark(int readlimit) {
                markPos = pos;
                if (DEBUG) out("  FIS.mark(): marking at "+pos);
        }

        public void reset() throws IOException {
                if (!canMark) {
                        throw new IOException("reset not supported!");
                }
                if (markPos == -1) {
                        throw new IOException("Mark position not set!");
                }
                pos = markPos;
                if (DEBUG) out("  FIS.reset(): now back at "+pos);
        }

        public boolean markSupported() {
                return canMark;
        }

    }

}
