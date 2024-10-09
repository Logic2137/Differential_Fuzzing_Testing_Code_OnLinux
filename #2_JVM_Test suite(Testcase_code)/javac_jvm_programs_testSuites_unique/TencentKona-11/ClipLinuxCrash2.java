

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;


public class ClipLinuxCrash2 implements LineListener{
    Clip clip;
    int stopOccured;
    static final Object lock = new Object();

    public static long bytes2Ms(long bytes, AudioFormat format) {
        return (long) (bytes/format.getFrameRate()*1000/format.getFrameSize());
    }

    static int staticLen=1000;
    static boolean addLen=true;

    ClipLinuxCrash2() {
    }

    public void update(LineEvent e) {
        if (e.getType() == LineEvent.Type.STOP) {
            stopOccured++;
            out("  Test program: receives STOP event for clip="+clip.toString()+" no."+stopOccured);
            out("  Test program: Calling close() in event dispatcher thread");
            clip.close();
            synchronized (lock) {
                lock.notifyAll();
            }
        }
        else if (e.getType() == LineEvent.Type.CLOSE) {
            out("  Test program: receives CLOSE event for "+clip.toString());
            synchronized (lock) {
                lock.notifyAll();
            }
        }
        else if (e.getType() == LineEvent.Type.START) {
            out("  Test program: receives START event for "+clip.toString());
        }
        else if (e.getType() == LineEvent.Type.OPEN) {
            out("  Test program: receives OPEN event for "+clip.toString());
        }
    }

    public long start() throws Exception {
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
        out("  Test program: preparing to play back "+len+" bytes == "+bytes2Ms(len, format)+"ms audio...");

        byte[] fakedata=new byte[len];
        InputStream is = new ByteArrayInputStream(fakedata);
        AudioInputStream ais = new AudioInputStream(is, format, fakedata.length/format.getFrameSize());

        DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
        clip = (Clip) AudioSystem.getLine(info);
        clip.addLineListener(this);

        out("  Test program: opening clip="+((clip==null)?"null":clip.toString()));
        clip.open(ais);
        ais.close();
        out("  Test program: starting clip="+((clip==null)?"null":clip.toString()));
        clip.start();
        return bytes2Ms(fakedata.length, format);
    }

    public static void main(String[] args) throws Exception {
        if (!isSoundcardInstalled()) {
            return;
        }

        try {
            int COUNT=10;
            out();
            out("4498848 Sound causes crashes on Linux");
            if (args.length>0) {
                COUNT=Integer.parseInt(args[0]);
            }
            for (int i=0; i<COUNT; i++) {
                out("trial "+(i+1)+"/"+COUNT);
                ClipLinuxCrash2 t = new ClipLinuxCrash2();
                t.start();
                int waitTime = 300+(1300*(i % 2)); 
                out("  Test program: waiting for "+waitTime+" ms for audio playback to stop...");
                Thread.sleep(waitTime);
                out("  Test program: calling close() from main thread");
                t.clip.close();
                
                
                
                
            }
            out("  Test program: waiting for 1 second...");
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
            out("  Test program: waiting for 1 second");
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

}
