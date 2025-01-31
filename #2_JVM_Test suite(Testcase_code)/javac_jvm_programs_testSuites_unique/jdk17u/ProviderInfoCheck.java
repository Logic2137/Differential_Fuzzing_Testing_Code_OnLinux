import java.io.*;
import java.security.*;
import java.util.*;

public class ProviderInfoCheck {

    public static void main(String[] args) throws Exception {
        Provider p = new SampleProvider();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(p);
        oos.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Provider p2 = (Provider) ois.readObject();
        ois.close();
        checkProviderInfoEntries(p2);
    }

    private static void checkProviderInfoEntries(Provider p) throws Exception {
        String value = (String) p.get("Provider.id name");
        if (!SampleProvider.NAME.equalsIgnoreCase(value) || !p.getName().equalsIgnoreCase(value)) {
            throw new Exception("Test Failed: incorrect name!");
        }
        value = (String) p.get("Provider.id info");
        if (!SampleProvider.INFO.equalsIgnoreCase(value) || !p.getInfo().equalsIgnoreCase(value)) {
            throw new Exception("Test Failed: incorrect info!");
        }
        value = (String) p.get("Provider.id className");
        if (!p.getClass().getName().equalsIgnoreCase(value)) {
            throw new Exception("Test Failed: incorrect className!");
        }
        value = (String) p.get("Provider.id version");
        if (!SampleProvider.VERSION.equalsIgnoreCase(value) || p.getVersionStr() != value) {
            throw new Exception("Test Failed: incorrect versionStr!");
        }
        System.out.println("Test Passed");
    }

    private static class SampleProvider extends Provider {

        static String NAME = "Sample";

        static String VERSION = "1.1";

        static String INFO = "Good for nothing";

        SampleProvider() {
            super(NAME, VERSION, INFO);
        }
    }
}
