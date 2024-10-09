

package nsk.jdi.IntegerArgument.min;

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





public class min001 {

    public static void main(String argv[]) {
        System.exit(run(argv, System.out) + 95); 
    }

    public static int run(String argv[], PrintStream out) {

        int exitCode  = 0;
        int exitCode0 = 0;
        int exitCode2 = 2;

        String sErr1 =  "WARNING\n" +
                        "Method tested: " +
                        "jdi.Connector.IntegerArgument.min\n" ;

        String sErr2 =  "ERROR\n" +
                        "Method tested: " +
                        "jdi.Connector.IntegerArgument.min\n" ;

        VirtualMachineManager vmm = Bootstrap.virtualMachineManager();

        List connectorsList = vmm.allConnectors();
        Iterator connectorsListIterator = connectorsList.iterator();

        Connector.IntegerArgument argument = null;

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
                    "no Connector with IntegerArgument found\n");
                return exitCode0;
            }
        }

        int iMin = argument.min();

        if (iMin < -2147483648) {
            exitCode = exitCode2;
            out.println(sErr2 +
                        "check: iMin < -2147483648\n");
        }

        if (iMin > 2147483647) {
            exitCode = exitCode2;
            out.println(sErr2 +
                        "check: iMin > 2147483647\n");
        }

        if (exitCode != exitCode0)
            out.println("TEST FAILED");

        return exitCode;
    }
}
