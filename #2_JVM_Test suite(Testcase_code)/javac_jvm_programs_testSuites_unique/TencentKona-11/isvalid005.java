

package nsk.jdi.Argument.isValid;

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





public class isvalid005 {

    public static void main(String argv[]) {
        System.exit(run(argv, System.out) + 95); 
    }

    public static int run(String argv[], PrintStream out) {

        int exitCode  = 0;
        int exitCode0 = 0;
        int exitCode2 = 2;

        String sErr1 =  "WARNING\n" +
                        "Method tested: " +
                        "jdi.Connector.Argument.isValid\n" ;

        String sErr2 =  "ERROR\n" +
                        "Method tested: " +
                        "jdi.Connector.Argument.isValid\n" ;

        VirtualMachineManager vmm = Bootstrap.virtualMachineManager();

        List connectorsList = vmm.allConnectors();
        Iterator connectorsListIterator = connectorsList.iterator();

        Connector.Argument argument = null;
        Connector.IntegerArgument intArgument = null;

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

                            argument = (Connector.IntegerArgument)
                                       defaultArguments.get(argName);
                            try {
                                intArgument = (Connector.IntegerArgument)
                                             defaultArguments.get(argName);
                                break ;
                            } catch ( ClassCastException e) {
                            }
                        } catch ( ClassCastException e) {
                        }
                    } catch ( NoSuchElementException e) {
                        break ;
                    }
                }
                if (intArgument != null) {
                    break ;
                }
            } catch ( NoSuchElementException e) {
                out.println(sErr1 +

                    "no Connector with IntegerArgument found\n");
                return exitCode0;
            }
        }

        if (!argument.isValid("")) {
        } else {
            exitCode = exitCode2;
            out.println(sErr2 +
                     "check: super.isValid('')\n" +
                     "result: true\n");
        }

        if (!argument.isValid("a")) {
        } else {
            exitCode = exitCode2;
            out.println(sErr2 +
                     "check: super.isValid('a')\n" +
                     "result: true\n");
        }

        if (!argument.isValid((String) null)) {
        } else {
            exitCode = exitCode2;
            out.println(sErr2 +
                     "check: super.isValid((String) null))\n" +
                     "result: true\n");
        }

        String s = null;
        if (!argument.isValid(s)) {
        } else {
            exitCode = exitCode2;
            out.println(sErr2 +
                     "check: super.isValid(String s = null)\n" +
                     "result: true\n");
        }


        if (exitCode != exitCode0) {
            out.println("TEST FAILED");
        }
        return exitCode;
    }
}
