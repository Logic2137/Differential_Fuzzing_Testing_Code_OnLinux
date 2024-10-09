
package nsk.jdi.SelectedArgument.choices;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Iterator;
import java.util.NoSuchElementException;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.Connector.SelectedArgument;

public class choices001 {

    public static void main(String[] argv) {
        System.exit(run(argv, System.out) + 95);
    }

    public static int run(String[] argv, PrintStream out) {
        int exitCode = 0;
        int exitCode0 = 0;
        int exitCode2 = 2;
        String sErr1 = "WARNING\n" + "Method tested: " + "jdi.Connector.SelectedArgument.choices\n";
        String sErr2 = "ERROR\n" + "Method tested: " + "jdi.Connector.SelectedArgument.choices\n";
        VirtualMachineManager vmm = Bootstrap.virtualMachineManager();
        List connectorsList = vmm.allConnectors();
        Iterator connectorsListIterator = connectorsList.iterator();
        Connector.SelectedArgument argument = null;
        for (; ; ) {
            try {
                Connector connector = (Connector) connectorsListIterator.next();
                Map defaultArguments = connector.defaultArguments();
                Set keyset = defaultArguments.keySet();
                int keysetSize = defaultArguments.size();
                Iterator keysetIterator = keyset.iterator();
                for (; ; ) {
                    try {
                        String argName = (String) keysetIterator.next();
                        try {
                            argument = (Connector.SelectedArgument) defaultArguments.get(argName);
                            break;
                        } catch (ClassCastException e) {
                        }
                    } catch (NoSuchElementException e) {
                        break;
                    }
                }
                if (argument != null) {
                    break;
                }
            } catch (NoSuchElementException e) {
                out.println(sErr1 + "no Connector with SelectedArgument found\n");
                return exitCode0;
            }
        }
        List listofChoices = argument.choices();
        if (listofChoices.isEmpty()) {
            exitCode = exitCode2;
            out.println(sErr2 + "check: isEmpty\n" + "error: returned List of String is empty\n");
        } else {
            Iterator listIterator = listofChoices.iterator();
            for (; ; ) {
                try {
                    String choice = (String) listIterator.next();
                } catch (ClassCastException e1) {
                    exitCode = exitCode2;
                    out.println(sErr2 + "check: String\n" + "error: List contains non-String\n");
                    break;
                } catch (NoSuchElementException e2) {
                    break;
                }
            }
        }
        if (exitCode != exitCode0)
            out.println("TEST FAILED");
        return exitCode;
    }
}
