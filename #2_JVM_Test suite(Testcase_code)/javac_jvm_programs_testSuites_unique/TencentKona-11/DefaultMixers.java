

import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.spi.MixerProvider;

import com.sun.media.sound.JDK13Services;


public class DefaultMixers {

    private static final String ERROR_PROVIDER_CLASS_NAME = "abc";
    private static final String ERROR_INSTANCE_NAME = "def";

    private static final Class[] lineClasses = {
        SourceDataLine.class,
        TargetDataLine.class,
        Clip.class,
        Port.class,
    };

    public static void main(String[] args) throws Exception {
        boolean allOk = true;
        Mixer.Info[] infos;

        out("Testing Mixers retrieved via AudioSystem");
        infos = AudioSystem.getMixerInfo();
        allOk &= testMixers(infos, null);

        out("Testing MixerProviders");
        List providers = JDK13Services.getProviders(MixerProvider.class);
        for (int i = 0; i < providers.size(); i++) {
            MixerProvider provider = (MixerProvider) providers.get(i);
            infos = provider.getMixerInfo();
            allOk &= testMixers(infos, provider.getClass().getName());
        }

        if (! allOk) {
            throw new Exception("Test failed");
        } else {
            out("Test passed");
        }
    }

    private static boolean testMixers(Mixer.Info[] infos,
                                      String providerClassName) {
        boolean allOk = true;

        for (int i = 0; i < infos.length; i++) {
            Mixer mixer = null;
            try {
                mixer = AudioSystem.getMixer(infos[i]);
            } catch (NullPointerException e) {
                out("Exception thrown; Test NOT failed.");
                e.printStackTrace();
            }
            for (int j = 0; j < lineClasses.length; j++) {
                if (mixer.isLineSupported(new Line.Info(lineClasses[j]))) {
                    allOk &= testMixer(mixer, lineClasses[j],
                                       providerClassName);
                }
            }
        }
        return allOk;
    }

    private static boolean testMixer(Mixer mixer, Class lineType,
                                      String providerClassName) {
        boolean allOk = true;
        String instanceName = mixer.getMixerInfo().getName();

        
        allOk &= testMixer(mixer, lineType,
                           providerClassName, instanceName);

        
        allOk &= testMixer(mixer, lineType,
                           ERROR_PROVIDER_CLASS_NAME, instanceName);

        
        allOk &= testMixer(mixer, lineType,
                           ERROR_PROVIDER_CLASS_NAME, "");

        
        allOk &= testMixer(mixer, lineType,
                           ERROR_PROVIDER_CLASS_NAME, ERROR_INSTANCE_NAME);

        return allOk;
    }

    private static boolean testMixer(Mixer mixer, Class lineType,
                                     String providerClassName,
                                     String instanceName) {
        boolean allOk = true;

        try {
            String propertyValue = (providerClassName != null) ? providerClassName: "" ;
            propertyValue += "#" + instanceName;
            out("property value: " + propertyValue);
            System.setProperty(lineType.getName(), propertyValue);
            Line line = null;
            Line.Info info = null;
            Line.Info[] infos;
            AudioFormat format = null;
            if (lineType == SourceDataLine.class || lineType == Clip.class) {
                infos = mixer.getSourceLineInfo();
                format = getFirstLinearFormat(infos);
                info = new DataLine.Info(lineType, format);
            } else if (lineType == TargetDataLine.class) {
                infos = mixer.getTargetLineInfo();
                format = getFirstLinearFormat(infos);
                info = new DataLine.Info(lineType, format);
            } else if (lineType == Port.class) {
                
                infos = mixer.getSourceLineInfo();
                for (int i = 0; i < infos.length; i++) {
                    if (infos[i] instanceof Port.Info) {
                        info = infos[i];
                        break;
                    }
                }
            }
            out("Line.Info: " + info);
            line = AudioSystem.getLine(info);
            out("line: " + line);
            if (! lineType.isInstance(line)) {
                out("type " + lineType + " failed: class should be '" +
                    lineType + "' but is '" + line.getClass() + "'!");
                allOk = false;
            }
        } catch (Exception e) {
            out("Exception thrown; Test NOT failed.");
            e.printStackTrace();
        }
        return allOk;
    }

    private static AudioFormat getFirstLinearFormat(Line.Info[] infos) {
        for (int i = 0; i < infos.length; i++) {
            if (infos[i] instanceof DataLine.Info) {
                AudioFormat[] formats = ((DataLine.Info) infos[i]).getFormats();
                for (int j = 0; j < formats.length; j++) {
                    AudioFormat.Encoding encoding = formats[j].getEncoding();
                    int sampleSizeInBits = formats[j].getSampleSizeInBits();
                    if (encoding.equals(AudioFormat.Encoding.PCM_SIGNED) &&
                        sampleSizeInBits == 16 ||
                        encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED) &&
                        sampleSizeInBits == 16) {
                        return formats[j];
                    }
                }
            }
        }
        return null;
    }

    private static void out(String message) {
        System.out.println(message);
    }
}
