

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;


public class ShowAudioFileTypes {

    public static void main(String[] args) throws Exception {
        AudioFileFormat.Type[]  aTypes = AudioSystem.getAudioFileTypes();
        System.out.println(aTypes.length+" supported target types:");
        for (int i = 0; i < aTypes.length; i++)
        {
            System.out.println("  "+(i+1)+". " + aTypes[i]+" with ext. '"+aTypes[i].getExtension()+"'");
        }
        if (aTypes.length<3) {
            throw new Exception("Test failed");
        } else {
            System.out.println("Test passed");
        }
    }
}
