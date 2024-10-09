

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;


public class SDLLinuxCrash implements Runnable {
    SourceDataLine sdl;
    int size;

    SDLLinuxCrash(SourceDataLine sdl, int size) {
        this.sdl = sdl;
        this.size = size - (size % 4);
    }

    public void run() {
        int written=0;
        
        byte[] buffer = data;
        out("    starting data line feed thread.");
        try {
            while (written<size) {
                int toWrite = buffer.length;
                if (toWrite+written > size) {
                    toWrite = size-written;
                }
                toWrite -= (toWrite % 4);
                
                int thisWritten = sdl.write(buffer, 0, toWrite);
                if (thisWritten<toWrite) {
                    out("    only wrote "+thisWritten+" bytes instead of "+toWrite);
                }
                if (thisWritten<=0) {
                    break;
                }
                written += thisWritten;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        out("    leaving data line feed thread.");
    }

    public static long bytes2Ms(long bytes, AudioFormat format) {
        return (long) (bytes/format.getFrameRate()*1000/format.getFrameSize());
    }

    static int staticLen=1000;
    static boolean addLen=true;

    public static SourceDataLine start() throws Exception {
        AudioFormat format = new AudioFormat(44100, 16, 2, true, false);
        if (addLen) {
            staticLen+=(int) (staticLen/5)+1000;
        } else {
            staticLen-=(int) (staticLen/5)+1000;
        }
        if (staticLen>8*44100*4) {
            staticLen = 8*44100*4;
            addLen=!addLen;
        }
        if (staticLen<1000) {
            staticLen = 1000;
            addLen=!addLen;
        }
        int len = staticLen;
        len -= (len % 4);
        out("    preparing to play back "+len+" bytes == "+bytes2Ms(len, format)+"ms audio...");

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine sdl = (SourceDataLine) AudioSystem.getLine(info);
        sdl.addLineListener(new LineListener() {
                public void update(LineEvent e) {
                    if (e.getType() == LineEvent.Type.STOP) {
                        out("    calling close() from event dispatcher thread");
                        ((SourceDataLine) e.getSource()).close();
                    }
                    else if (e.getType() == LineEvent.Type.CLOSE) {
                    }
                }
            });

        out("    opening...");
        sdl.open();
        out("    starting...");
        sdl.start();
        (new Thread(new SDLLinuxCrash(sdl, len))).start();
        return sdl;
    }

    public static void main(String[] args) throws Exception {
        if (!isSoundcardInstalled()) {
            return;
        }

        try {
            int COUNT=10;
            out();
            out("4498848 Sound causes crashes on Linux (testing with SourceDataLine)");
            if (args.length>0) {
                COUNT=Integer.parseInt(args[0]);
            }
            for (int i=0; i<COUNT; i++) {
                out("  trial "+(i+1)+"/"+COUNT);
                SourceDataLine sdl = start();
                int waitTime = 500+(1000*(i % 2)); 
                out("    waiting for "+waitTime+" ms for audio playback to stop...");
                Thread.sleep(waitTime);
                out("    calling close() from main thread");
                sdl.close();
                
                out("    waiting for 2 seconds...");
                Thread.sleep(2000);
                out();
            }
            out("  waiting for 1 second...");
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
            out("  waiting for 1 second");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {}
            
            if (!(e instanceof LineUnavailableException)) {
                throw e;
            }
        }
        out("Test passed");
    }

    static void out() {
        out("");
    }

    static void out(String s) {
        System.out.println(s); System.out.flush();
    }

    
    public static boolean isSoundcardInstalled() {
        boolean result = false;
        try {
            Mixer.Info[] mixers = AudioSystem.getMixerInfo();
            if (mixers.length > 0) {
                result = AudioSystem.getSourceDataLine(null) != null;
            }
        } catch (Exception e) {
            System.err.println("Exception occured: "+e);
        }
        if (!result) {
            System.err.println("Soundcard does not exist or sound drivers not installed!");
            System.err.println("This test requires sound drivers for execution.");
        }
        return result;
    }



    static final byte[] data = new byte[] {
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120,
        123, 110, 100, 60, 11, 10, 10, 10, 9, 9,
        9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 6,
        7, 7, 7, 7, 8, 8, 8, 8, 9, 9,
        9, 9, 10, 10, 10, 11, 11, 60, 100, 110, 120, 122, 122
    };

}
