

package nsk.jdi.BooleanArgument.stringValueOf;

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





public class stringvalueof002 {

    public static void main(String argv[]) {
        System.exit(run(argv, System.out) + 95); 
    }

    public static int run(String argv[], PrintStream out) {

        int exitCode  = 0;
        int exitCode0 = 0;
        int exitCode2 = 2;

        String sErr1 =  "WARNING\n" +
                        "Method tested: " +
                        "jdi.Connector.BooleanArgument.stringValueOf\n" ;

        String sErr2 =  "ERROR\n" +
                        "Method tested: " +
                        "jdi.Connector.BooleanArgument.stringValueOf\n" ;

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
                    "no Connector with BooleanArgument found\n");
                return exitCode0;
            }
        }

        String argumentValue;
        
        argument.setValue(true);
        argumentValue = argument.stringValueOf(true);
        if (argument.booleanValue() != true) {
            exitCode = 2;
            out.println(sErr2 +
                      "case: stringValueOf(true) doesn't change value true\n" +
                      "error: argument's value != true\n");
        }

        
        argument.setValue(true);
        argumentValue = argument.stringValueOf(false);
        if (argument.booleanValue() != true) {
            exitCode = 2;
            out.println(sErr2 +
                      "case: stringValueOf(false) doesn't change value true\n" +
                      "error: argument's value != true\n");
        }

        
        argument.setValue(false);
        argumentValue = argument.stringValueOf(true);
        if (argument.booleanValue() != false) {
            exitCode = 2;
            out.println(sErr2 +
                      "case: stringValueOf(true) doesn't change value false\n" +
                      "error: a returned value != false\n");
        }

        
        argument.setValue(false);
        argumentValue = argument.stringValueOf(false);
        if (argument.booleanValue() != false) {
            exitCode = 2;
            out.println(sErr2 +
                      "case: stringValueOf(false) doesn't change value false\n" +
                      "error: a returned value != false\n");
        }

        if (exitCode != exitCode0)
            out.println("TEST FAILED");

        return exitCode;
    }
}
