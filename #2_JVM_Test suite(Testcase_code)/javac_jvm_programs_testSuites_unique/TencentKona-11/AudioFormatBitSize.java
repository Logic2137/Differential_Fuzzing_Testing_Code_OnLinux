



import javax.sound.sampled.AudioFormat;

public class AudioFormatBitSize {

    public static void main(String[] args) throws Exception {
        int bits = 18;
        AudioFormat format = new AudioFormat(44100.0f, bits, 1, true, false);
        if (format.getFrameSize() * 8 < bits) {
            System.out.println("bits = "+bits+" do not fit into a "+format.getFrameSize()+" bytes sample!");
            throw new Exception("Test failed");
        } else
            System.out.println("bits = "+bits+" fit OK into a "+format.getFrameSize()+" bytes sample!");
            System.out.println("Test passed");
    }
}
