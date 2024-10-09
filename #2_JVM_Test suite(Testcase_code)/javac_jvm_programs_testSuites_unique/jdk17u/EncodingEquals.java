import javax.sound.sampled.AudioFormat;

public class EncodingEquals {

    public static void main(String[] argv) throws Exception {
        AudioFormat.Encoding myType = new AudioFormat.Encoding("PCM_SIGNED");
        if (!myType.equals(AudioFormat.Encoding.PCM_SIGNED)) {
            throw new Exception("Encodings do not equal!");
        }
    }
}
