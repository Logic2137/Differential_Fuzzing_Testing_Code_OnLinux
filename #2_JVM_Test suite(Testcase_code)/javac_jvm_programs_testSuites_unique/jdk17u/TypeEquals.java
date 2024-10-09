import javax.sound.sampled.AudioFileFormat;

public class TypeEquals {

    public static void main(String[] argv) throws Exception {
        AudioFileFormat.Type myType = new AudioFileFormat.Type("WAVE", "wav");
        if (!myType.equals(AudioFileFormat.Type.WAVE)) {
            throw new Exception("Types do not equal!");
        }
    }
}
