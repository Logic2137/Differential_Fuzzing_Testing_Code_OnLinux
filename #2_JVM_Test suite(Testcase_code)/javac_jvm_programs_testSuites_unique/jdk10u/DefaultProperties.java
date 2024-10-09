

import java.io.File;

import com.sun.media.sound.JDK13Services;


public class DefaultProperties {

    private static final Class[] lineTypeClasses = {
        javax.sound.midi.Receiver.class,
        javax.sound.midi.Transmitter.class,
        javax.sound.midi.Sequencer.class,
        javax.sound.midi.Synthesizer.class,
    };

    public static void main(String[] args) throws Exception {
        boolean allOk = true;
        File file = new File(System.getProperty("test.src", "."), "testdata");
        System.setProperty("java.home", file.getCanonicalPath());

        for (int i = 0; i < lineTypeClasses.length; i++) {
            Class cls = lineTypeClasses[i];
            String propertyName = cls.getName();
            String result;
            String provClassName;
            String instanceName;

            
            provClassName = "xyz";
            instanceName = "123";
            result = JDK13Services.getDefaultProviderClassName(cls);
            if (! provClassName.equals(result)) {
                out("type " + cls + " failed: provider class should be '" +
                    provClassName + "' but is '" + result + "'!");
                allOk = false;
            }
            result = JDK13Services.getDefaultInstanceName(cls);
            if (! instanceName.equals(result)) {
                out("type " + cls + " failed: instance name should be '" +
                    instanceName + "' but is '" + result + "'!");
                allOk = false;
            }

            
            provClassName = "abc";
            System.setProperty(propertyName, provClassName);
            result = JDK13Services.getDefaultProviderClassName(cls);
            if (! provClassName.equals(result)) {
                out("type " + cls + " failed: provider class should be '" +
                    provClassName + "' but is '" + result + "'!");
                allOk = false;
            }
            result = JDK13Services.getDefaultInstanceName(cls);
            if (result != null) {
                out("type " + cls + " failed: instance name should be " +
                    "null but is '" + result + "'!");
                allOk = false;
            }

            
            provClassName = "def";
            System.setProperty(propertyName, provClassName + "#");
            result = JDK13Services.getDefaultProviderClassName(cls);
            if (! provClassName.equals(result)) {
                out("type " + cls + " failed: provider class should be '" +
                    provClassName + "' but is '" + result + "'!");
                allOk = false;
            }
            result = JDK13Services.getDefaultInstanceName(cls);
            if (result != null) {
                out("type " + cls + " failed: instance name should be " +
                    "null but is '" + result + "'!");
                allOk = false;
            }

            
            instanceName = "ghi";
            System.setProperty(propertyName, "#" + instanceName);
            result = JDK13Services.getDefaultProviderClassName(cls);
            if (result != null) {
                out("type " + cls + " failed: provider class should be " +
                    "null but is '" + result + "'!");
                allOk = false;
            }
            result = JDK13Services.getDefaultInstanceName(cls);
            if (! instanceName.equals(result)) {
                out("type " + cls + " failed: instance name should be '" +
                    instanceName + "' but is '" + result + "'!");
                allOk = false;
            }

            
            provClassName = "jkl";
            instanceName = "mno";
            System.setProperty(propertyName, provClassName + "#" + instanceName);
            result = JDK13Services.getDefaultProviderClassName(cls);
            if (! provClassName.equals(result)) {
                out("type " + cls + "failed: provider class should be '" +
                    provClassName + "' but is '" + result + "'!");
                allOk = false;
            }
            result = JDK13Services.getDefaultInstanceName(cls);
            if (! instanceName.equals(result)) {
                out("type " + cls + "failed: instance name should be '" +
                    instanceName + "' but is '" + result + "'!");
                allOk = false;
            }

            
            System.setProperty(propertyName, "");
            result = JDK13Services.getDefaultProviderClassName(cls);
            if (result != null) {
                out("type " + cls + " failed: provider class should be " +
                    "null but is '" + result + "'!");
                allOk = false;
            }
            result = JDK13Services.getDefaultInstanceName(cls);
            if (result != null) {
                out("type " + cls + "failed: instance name should be " +
                    "null but is '" + result + "'!");
                allOk = false;
            }
        }
        if (! allOk) {
            throw new Exception("Test failed");
        } else {
            out("Test passed");
        }
    }

    private static void out(String message) {
        System.out.println(message);
    }
}

