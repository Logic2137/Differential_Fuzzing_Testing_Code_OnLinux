

package nsk.jdi.StringArgument.isValid;

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





public class isvalid001 {

    public static void main(String argv[]) {
        System.exit(run(argv, System.out) + 95); 
    }

    public static int run(String argv[], PrintStream out) {

        int exitCode  = 0;
        int exitCode0 = 0;
        int exitCode2 = 2;

        String sErr1 =  "WARNING\n" +
                        "Method tested: " +
                        "jdi.Connector.StringArgument.isValid\n" ;

        String sErr2 =  "ERROR\n" +
                        "Method tested: " +
                        "jdi.Connector.StringArgument.isValid\n" ;

        VirtualMachineManager vmm = Bootstrap.virtualMachineManager();

        List connectorsList = vmm.allConnectors();
        Iterator connectorsListIterator = connectorsList.iterator();

        Connector.StringArgument argument = null;

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

                            argument = (Connector.StringArgument)
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
                    "no Connector with StringArgument found\n");
                return exitCode0;
            }
        }

        if (!argument.isValid("")) {
            exitCode = exitCode2;
            out.println(sErr2 +
                      "case:  parameter == empty string\n" +
                      "error: return value != true\n");
        }

        if (!argument.isValid("0")) {
            exitCode = exitCode2;
            out.println(sErr2 +
                      "case:  parameter == non-empty string\n" +
                      "error: return value != true\n");
        }

        if (exitCode != exitCode0)
            out.println("TEST FAILED");

        return exitCode;
    }
}
