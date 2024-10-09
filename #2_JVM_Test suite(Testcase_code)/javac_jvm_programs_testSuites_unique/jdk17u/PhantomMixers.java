import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class PhantomMixers {

    public static void main(String[] args) throws Exception {
        int SDLformats = 0;
        int TDLformats = 0;
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        for (int i = 0; i < mixerInfo.length; i++) {
            Mixer.Info thisMixerInfo = mixerInfo[i];
            System.out.println("Mixer #" + i + ": " + thisMixerInfo.getName() + ": " + thisMixerInfo.getDescription());
            Mixer mixer = AudioSystem.getMixer(thisMixerInfo);
            Line.Info[] srcLineInfo = mixer.getSourceLineInfo();
            Line.Info[] dstLineInfo = mixer.getTargetLineInfo();
            int count = srcLineInfo.length + dstLineInfo.length;
            System.out.print(" -> " + (srcLineInfo.length + dstLineInfo.length) + " line");
            switch(count) {
                case 0:
                    System.out.println("s");
                    break;
                case 1:
                    System.out.println("");
                    break;
                default:
                    System.out.println("s:");
                    break;
            }
            int l;
            for (l = 0; l < srcLineInfo.length; l++) {
                System.out.println("    " + srcLineInfo[l].toString());
                if (srcLineInfo[l].getLineClass() == SourceDataLine.class && (srcLineInfo[l] instanceof DataLine.Info)) {
                    SDLformats += ((DataLine.Info) srcLineInfo[l]).getFormats().length;
                }
            }
            for (l = 0; l < dstLineInfo.length; l++) {
                System.out.println("    " + dstLineInfo[l].toString());
                if (dstLineInfo[l].getLineClass() == TargetDataLine.class && (dstLineInfo[l] instanceof DataLine.Info)) {
                    TDLformats += ((DataLine.Info) dstLineInfo[l]).getFormats().length;
                }
            }
        }
        if (mixerInfo.length == 0) {
            System.out.println("[no mixers present]");
        }
        System.out.println("" + SDLformats + " total formats for SourceDataLines");
        System.out.println("" + TDLformats + " total formats for TargetDataLines");
        System.out.println("");
        System.out.println("If there are audio devices correctly installed on your");
        System.out.println("system, you should see at least one Mixer, and in total");
        System.out.println("at least each one SourceDataLine and TargetDataLine, both");
        System.out.println("providing at least one format.");
        System.out.println("");
        System.out.println("Now disable your soundcard and repeat the test.");
        System.out.println("The corresponding mixer(s) should not provide any formats");
        System.out.println("anymore. If you disable all available soundcards");
        System.out.println("on your computer, the number of formats above should be");
        System.out.println("0 for both line types (although mixers are allowed to exist).");
    }
}
