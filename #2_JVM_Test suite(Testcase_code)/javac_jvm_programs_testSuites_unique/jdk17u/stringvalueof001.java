
package nsk.jdi.IntegerArgument.stringValueOf;

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
import com.sun.jdi.connect.Connector.IntegerArgument;

public class stringvalueof001 {

    public static void main(String[] argv) {
        System.exit(run(argv, System.out) + 95);
    }

    public static int run(String[] argv, PrintStream out) {
        int exitCode = 0;
        int exitCode0 = 0;
        int exitCode2 = 2;
        String sErr1 = "WARNING\n" + "Method tested: " + "jdi.Connector.IntegerArgument.intValue\n";
        String sErr2 = "ERROR\n" + "Method tested: " + "jdi.Connector.IntegerArgument.intValue()\n";
        VirtualMachineManager vmm = Bootstrap.virtualMachineManager();
        List connectorsList = vmm.allConnectors();
        Iterator connectorsListIterator = connectorsList.iterator();
        Connector.IntegerArgument intArgument = null;
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
                            intArgument = (Connector.IntegerArgument) defaultArguments.get(argName);
                            break;
                        } catch (ClassCastException e) {
                        }
                    } catch (NoSuchElementException e) {
                        break;
                    }
                }
                if (intArgument != null) {
                    break;
                }
            } catch (NoSuchElementException e) {
                out.println(sErr1 + "no Connector with IntegerArgument found\n");
                return exitCode0;
            }
        }
        Integer intI = null;
        int i;
        i = intArgument.min();
        intArgument.setValue(intArgument.stringValueOf(i));
        if (intArgument.intValue() != i) {
            exitCode = exitCode2;
            out.println(sErr2 + "check: stringValueOf(min())\n" + "result: inequality\n");
        }
        i = intArgument.max();
        intArgument.setValue(intArgument.stringValueOf(i));
        if (intArgument.intValue() != i) {
            exitCode = exitCode2;
            out.println(sErr2 + "check: stringValueOf(max())\n" + "result: inequality\n");
        }
        if (intArgument.min() < intArgument.max()) {
            i = intArgument.min() + 1;
            intArgument.setValue(intArgument.stringValueOf(i));
            if (intArgument.intValue() != i) {
                exitCode = exitCode2;
                out.println(sErr2 + "check: stringValueOf(min()+1)\n" + "result: inequality\n");
            }
        }
        if (intArgument.min() > intI.MIN_VALUE) {
            i = intArgument.min() - 1;
            intArgument.setValue(intArgument.stringValueOf(i));
            if (intArgument.intValue() != i) {
                exitCode = exitCode2;
                out.println(sErr2 + "check: stringValueOf(min()-1)\n" + "result: inequality\n");
            }
        }
        if (intArgument.max() < intI.MAX_VALUE) {
            i = intArgument.max() + 1;
            intArgument.setValue(intArgument.stringValueOf(i));
            if (intArgument.intValue() != i) {
                exitCode = exitCode2;
                out.println(sErr2 + "check: stringValueOf(max()+1)\n" + "result: inequality\n");
            }
        }
        if (exitCode != exitCode0) {
            out.println("TEST FAILED");
        }
        return exitCode;
    }
}
