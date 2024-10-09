import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.TerminalFactory;
import javax.smartcardio.TerminalFactorySpi;

public class TerminalFactorySpiTest {

    static boolean callMethod = false;

    public static void main(String[] args) throws Exception {
        Provider myProvider = new MyProvider();
        Security.addProvider(myProvider);
        System.out.println(Arrays.asList(Security.getProviders()));
        TerminalFactory.getInstance("MyType", new Object()).terminals();
        if (!callMethod) {
            throw new RuntimeException("Expected engineTerminals() not called");
        }
    }

    public static class MyProvider extends Provider {

        MyProvider() {
            super("MyProvider", 1.0d, "smart Card Example");
            put("TerminalFactory.MyType", "TerminalFactorySpiTest$MyTerminalFactorySpi");
        }
    }

    public static class MyTerminalFactorySpi extends TerminalFactorySpi {

        public MyTerminalFactorySpi(Object ob) {
        }

        protected CardTerminals engineTerminals() {
            System.out.println("MyTerminalFactory.engineTerminals()");
            callMethod = true;
            return null;
        }
    }
}
