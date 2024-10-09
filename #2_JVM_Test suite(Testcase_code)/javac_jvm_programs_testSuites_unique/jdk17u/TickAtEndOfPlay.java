import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class TickAtEndOfPlay {

    static boolean WorkAround1 = false;

    static boolean WorkAround2 = false;

    public static void main(String[] args) throws Exception {
        System.out.println("This test should only be run on Windows.");
        System.out.println("Make sure that the speakers are connected and the volume is up.");
        System.out.println("Close all other programs that may use the soundcard.");
        System.out.println("You'll hear a 2-second tone. when the tone finishes,");
        System.out.println("  there should be no noise. If you hear a short tick/noise,");
        System.out.println("  the bug still applies.");
        System.out.println("Press ENTER to continue.");
        System.in.read();
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("1"))
                WorkAround1 = true;
            if (args[i].equals("2"))
                WorkAround2 = true;
        }
        if (WorkAround1)
            System.out.println("Using work around1: appending silence");
        if (WorkAround2)
            System.out.println("Using work around2: waiting before close");
        int zerolen = 0;
        if (WorkAround1)
            zerolen = 1000;
        int seconds = 2;
        int sampleRate = 8000;
        double frequency = 1000.0;
        double RAD = 2.0 * Math.PI;
        AudioFormat af = new AudioFormat((float) sampleRate, 8, 1, true, true);
        System.out.println("Format: " + af);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
        SourceDataLine source = (SourceDataLine) AudioSystem.getLine(info);
        System.out.println("Line: " + source);
        if (source.toString().indexOf("MixerSourceLine") >= 0) {
            System.out.println("This test only applies to non-Java Sound Audio Engine!");
            return;
        }
        System.out.println("Opening...");
        source.open(af);
        System.out.println("Starting...");
        source.start();
        int datalen = sampleRate * seconds;
        byte[] buf = new byte[datalen + zerolen];
        for (int i = 0; i < datalen; i++) {
            buf[i] = (byte) (Math.sin(RAD * frequency / sampleRate * i) * 127.0);
        }
        System.out.println("Writing...");
        source.write(buf, 0, buf.length);
        System.out.println("Draining...");
        source.drain();
        System.out.println("Stopping...");
        source.stop();
        if (WorkAround2) {
            System.out.println("Waiting 200 millis...");
            Thread.sleep(200);
        }
        System.out.println("Closing...");
        source.close();
        System.out.println("Done.");
    }
}
