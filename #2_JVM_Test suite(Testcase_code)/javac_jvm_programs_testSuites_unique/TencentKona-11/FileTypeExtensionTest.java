

import javax.sound.sampled.AudioFileFormat;


public class FileTypeExtensionTest {

    public static void main(String[] args) throws Exception {

        AudioFileFormat.Type[] types = { AudioFileFormat.Type.AIFC,
                                         AudioFileFormat.Type.AIFF,
                                         AudioFileFormat.Type.AU,
                                         AudioFileFormat.Type.SND,
                                         AudioFileFormat.Type.WAVE };

        boolean failed = false;

        System.out.println("\nDefined file types and extensions:");

        for (int i = 0; i < types.length; i++) {
            System.out.println("\n");
            System.out.println("  file type: " + types[i]);
            System.out.println("  extension: " + types[i].getExtension());
            if( types[i].getExtension().charAt(0) == '.' ) {
                failed = true;
            }
        }

        if (failed) {
            System.err.println("Failed!");
            throw new Exception("File type extensions begin with .");
        } else {
            System.err.println("Passed!");
        }
    }
}
