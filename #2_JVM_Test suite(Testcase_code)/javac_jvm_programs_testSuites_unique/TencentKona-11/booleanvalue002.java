

package nsk.jdi.BooleanArgument.booleanValue;

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
import com.sun.jdi.connect.Connector.Argument;
import com.sun.jdi.connect.Connector.BooleanArgument;
import com.sun.jdi.connect.LaunchingConnector;





public class booleanvalue002 {

    public static void main(String argv[]) {
        System.exit(run(argv, System.out) + 95); 
    }

    public static int run(String argv[], PrintStream out) {

        int exitCode  = 0;
        int exitCode0 = 0;
        int exitCode2 = 2;

        String sErr1 =  "WARNING:\n" +
                        "Method tested: " +
                        "jdi.Connector.BooleanArgument.booleanValue\n" ;

        String sErr2 =  "INFO:\n" +
                        "Method tested: " +
                        "jdi.Connector.BooleanArgument.booleanValue\n" ;


        VirtualMachineManager vmm = Bootstrap.virtualMachineManager();

        List connectorsList = vmm.allConnectors();
        Iterator connectorsListIterator = connectorsList.iterator();

        Connector.BooleanArgument argument = null;

        for ( ; ; ) {
            try {
                Connector connector =
                (Connector) connectorsListIterator.next();

                Map defaultArguments = connector.defaultArguments();
                Set keyset     = defaultArguments.keySet();
                int keysetSize = defaultArguments.size();
                Iterator  keysetIterator = keyset.iterator();

                for ( ; ; ) {
                    try {
                        String argName = (String) keysetIterator.next();

                        try {

                            argument = (Connector.BooleanArgument)
                                       defaultArguments.get(argName);
                            break ;
                        } catch ( ClassCastException e) {
                        }
                    } catch ( NoSuchElementException e) {
                        break ;
                    }
                }
                if (argument != null) {
                    break ;
                }
            } catch ( NoSuchElementException e) {
                out.println(sErr1 +
                    "no Connecter with needed BooleanArgument found\n");
                return exitCode0;
            }
        }

        boolean b;

        argument.setValue(true);
        argument.setValue("");
        b = argument.booleanValue();
        if (b) {
            exitCode = exitCode2;
            out.println(sErr2 +
                      "check: '' -> true = ? \n" +
                      "result: booleanValue() == " + b + "\n");
        }

        argument.setValue(true);
        argument.setValue("tru");
        b = argument.booleanValue();
        if (b) {
            exitCode = exitCode2;
            out.println(sErr2 +
                      "check: 'tru' -> true = ? \n" +
                      "result: booleanValue() == " + b + "\n");
        }

        argument.setValue(false);
        argument.setValue("");
        b = argument.booleanValue();
        if (b) {
            exitCode = exitCode2;
            out.println(sErr2 +
                      "check: '' -> false = ? \n" +
                      "result: booleanValue() == " + b + "\n");
        }

        argument.setValue(false);
        argument.setValue("fals");
        b = argument.booleanValue();
        if (b) {
            exitCode = exitCode2;
            out.println(sErr2 +
                      "check: '' -> false = ? \n" +
                      "result: booleanValue() == " + b + "\n");
        }

        argument.setValue("true");
        argument.setValue("");
        b = argument.booleanValue();
        if (b) {
            exitCode = exitCode2;
            out.println(sErr2 +
                     "check: '' -> 'true' = ? \n" +
                     "result: booleanValue() == " + b + "\n");
        }

        argument.setValue("true");
        argument.setValue("tru");
        b = argument.booleanValue();
        if (b) {
            exitCode = exitCode2;
            out.println(sErr2 +
                      "check: 'tru' -> 'true' = ? \n" +
                      "result: booleanValue() == " + b + "\n");
        }

        argument.setValue("false");
        argument.setValue("");
        b = argument.booleanValue();
        if (b) {
            exitCode = exitCode2;
            out.println(sErr2 +
                      "check: '' -> 'false' = ? \n" +
                      "result: booleanValue() == " + b + "\n");
        }

        argument.setValue("false");
        argument.setValue("fals");
        b = argument.booleanValue();
        if (b) {
            exitCode = exitCode2;
            out.println(sErr2 +
                      "check: 'fals' -> 'false' = ? \n" +
                      "result: booleanValue() == " + b + "\n");
        }

        if (exitCode != exitCode0)
            out.println("TEST FAILED");

        return exitCode;
    }
}
