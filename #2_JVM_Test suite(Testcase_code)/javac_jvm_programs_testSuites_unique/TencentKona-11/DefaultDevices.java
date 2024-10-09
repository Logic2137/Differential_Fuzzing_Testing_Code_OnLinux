

import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;
import javax.sound.midi.spi.MidiDeviceProvider;

import com.sun.media.sound.JDK13Services;



public class DefaultDevices {

    private static final String ERROR_PROVIDER_CLASS_NAME = "abc";
    private static final String ERROR_INSTANCE_NAME = "def";

    private static final Class RECEIVER_CLASS = javax.sound.midi.Receiver.class;
    private static final Class TRANSMITTER_CLASS = javax.sound.midi.Transmitter.class;
    private static final Class SEQUENCER_CLASS = javax.sound.midi.Sequencer.class;
    private static final Class SYNTHESIZER_CLASS = javax.sound.midi.Synthesizer.class;

    public static void main(String[] args) throws Exception {
        boolean allOk = true;
        MidiDevice.Info[] infos;

        out("\nTesting MidiDevices retrieved via MidiSystem");
        infos = MidiSystem.getMidiDeviceInfo();
        allOk &= testDevices(infos, null);

        out("\nTesting MidiDevices retrieved from MidiDeviceProviders");
        List providers = JDK13Services.getProviders(MidiDeviceProvider.class);
        for (int i = 0; i < providers.size(); i++) {
            MidiDeviceProvider provider = (MidiDeviceProvider)providers.get(i);
            infos = provider.getDeviceInfo();
            allOk &= testDevices(infos, provider.getClass().getName());
        }

        if (!allOk) {
            throw new Exception("Test failed");
        } else {
            out("Test passed");
        }
    }

    private static boolean testDevices(MidiDevice.Info[] infos,
            String providerClassName) {
        boolean allOk = true;

        for (int i = 0; i < infos.length; i++) {
            MidiDevice device = null;
            try {
                device = MidiSystem.getMidiDevice(infos[i]);
            } catch (MidiUnavailableException e) {
                out("Exception thrown; Test NOT failed.");
                e.printStackTrace(System.out);
                out("");
            }
            out("\nTesting device: " + device);
            if (device instanceof Sequencer) {
                allOk &= testDevice(device, SEQUENCER_CLASS, providerClassName, true, true);
                
                allOk &= testDevice(device, SYNTHESIZER_CLASS, providerClassName, false, false);
                allOk &= testDevice(device, RECEIVER_CLASS, providerClassName, false, false);
                allOk &= testDevice(device, TRANSMITTER_CLASS, providerClassName, false, false);
            }
            if (device instanceof Synthesizer) {
                allOk &= testDevice(device, SYNTHESIZER_CLASS, providerClassName, true, true);
                allOk &= testDevice(device, RECEIVER_CLASS, providerClassName, false, true);
                
                allOk &= testDevice(device, TRANSMITTER_CLASS, providerClassName, false, false);
                allOk &= testDevice(device, SEQUENCER_CLASS, providerClassName, false, false);
            }
            if (device instanceof Receiver) {
                allOk &= testDevice(device, RECEIVER_CLASS, providerClassName, true, true);
                
                allOk &= testDevice(device, TRANSMITTER_CLASS, providerClassName, false, false);
                allOk &= testDevice(device, SYNTHESIZER_CLASS, providerClassName, false, false);
                allOk &= testDevice(device, SEQUENCER_CLASS, providerClassName, false, false);
            }
            if (device instanceof Transmitter) {
                allOk &= testDevice(device, TRANSMITTER_CLASS, providerClassName, true, true);
                
                allOk &= testDevice(device, RECEIVER_CLASS, providerClassName, false, false);
                allOk &= testDevice(device, SYNTHESIZER_CLASS, providerClassName, false, false);
                allOk &= testDevice(device, SEQUENCER_CLASS, providerClassName, false, false);
            }
        }
        return allOk;
    }

    private static boolean testDevice(MidiDevice device, Class type,
            String providerClassName, boolean testWrong, boolean expectedResult) {
        boolean allOk = true;
        String instanceName = device.getDeviceInfo().getName();

        
        allOk &= testDevice(device, type, providerClassName,
                            instanceName, expectedResult);

        if (testWrong) {
            
            allOk &= testDevice(device, type, ERROR_PROVIDER_CLASS_NAME,
                                instanceName, expectedResult);

            
            
            allOk &= testDevice(device, type, providerClassName,
                                ERROR_INSTANCE_NAME, expectedResult);
        }

        return allOk;
    }

    private static boolean testDevice(MidiDevice device, Class type,
            String providerClassName, String instanceName,
            boolean expectedResult) {
        boolean allOk = true;

        try {
            String propertyName = type.getName();
            String propertyValue = (providerClassName != null) ? providerClassName: "" ;
            propertyValue += "#" + instanceName;
            out("property: " + propertyName + "="+ propertyValue);
            System.setProperty(propertyName, propertyValue);
            Object reference = null;
            Object result = null;
            if (type == SEQUENCER_CLASS) {
                reference = device;
                result = MidiSystem.getSequencer();
            } else if (type == SYNTHESIZER_CLASS) {
                reference = device;
                result = MidiSystem.getSynthesizer();
            } else if (type == RECEIVER_CLASS) {
                reference = device.getReceiver();
                result = MidiSystem.getReceiver();
            } else if (type == TRANSMITTER_CLASS) {
                reference = device.getTransmitter();
                result = MidiSystem.getTransmitter();
            }
            out("result: " + result);
            boolean rightDevice = (reference.getClass() == result.getClass());
            if (rightDevice != expectedResult) {
                out("\nERROR: type " + type + " failed:"
                        + " class should" + (expectedResult ? "" : " NOT") + " be '"
                        + reference.getClass()
                        + "' but is '" + result.getClass() + "'!\n");
                allOk = false;
            }
            if (expectedResult
                    && reference instanceof MidiDevice
                    && result instanceof MidiDevice) {
                MidiDevice referenceDevice = (MidiDevice)reference;
                MidiDevice resultDevice = (MidiDevice)result;
                if (!referenceDevice.getDeviceInfo().getName().equals(
                                    resultDevice.getDeviceInfo().getName())) {
                    out("\nERROR: type " + type + " failed: name should be '"
                            + referenceDevice.getDeviceInfo().getName()
                            + "' but is '"
                            + resultDevice.getDeviceInfo().getName() + "'!\n");
                    allOk = false;
                }
            }
            if (result instanceof Receiver) {
                ((Receiver)result).close();
            } else if (result instanceof Transmitter) {
                ((Transmitter)result).close();
            } else if (result instanceof Synthesizer) {
                ((Synthesizer)result).close();
            } else if (result instanceof Sequencer) {
                ((Sequencer)result).close();
            }
        } catch (Exception e) {
            out("Exception thrown; Test NOT failed.");
            e.printStackTrace(System.out);
            out("");
        }
        return allOk;
    }

    private static void out(String message) {
        System.out.println(message);
    }
}
