import com.sun.jdi.*;
import com.sun.jdi.connect.*;
import com.sun.jdi.request.EventRequestManager;
import java.util.*;
import java.io.*;

class VMConnection {

    private VirtualMachine vm;

    private Process process = null;

    private int outputCompleteCount = 0;

    private final Connector connector;

    private final Map connectorArgs;

    private final int traceFlags;

    static public String getDebuggeeVMOptions() {
        String retVal = "";
        String testClasses = System.getProperty("test.classes");
        if (testClasses == null) {
            return retVal;
        }
        retVal += "-classpath " + testClasses;
        String vmOpts = System.getProperty("test.vm.opts");
        System.out.println("vmOpts: '" + vmOpts + "'");
        if (vmOpts != null && !vmOpts.trim().isEmpty()) {
            retVal += " " + vmOpts;
        }
        String javaOpts = System.getProperty("test.java.opts");
        System.out.println("javaOpts: '" + javaOpts + "'");
        if (javaOpts != null && !javaOpts.trim().isEmpty()) {
            retVal += " " + javaOpts;
        }
        return retVal;
    }

    static public String[] insertDebuggeeVMOptions(String[] cmdLine) {
        String opts = getDebuggeeVMOptions();
        if (opts.equals("")) {
            return cmdLine;
        }
        String[] v1 = opts.split(" +");
        String[] retVal = new String[cmdLine.length + v1.length];
        retVal[0] = cmdLine[0];
        System.arraycopy(v1, 0, retVal, 1, v1.length);
        System.arraycopy(cmdLine, 1, retVal, v1.length + 1, cmdLine.length - 1);
        return retVal;
    }

    private Connector findConnector(String name) {
        List connectors = Bootstrap.virtualMachineManager().allConnectors();
        Iterator iter = connectors.iterator();
        while (iter.hasNext()) {
            Connector connector = (Connector) iter.next();
            if (connector.name().equals(name)) {
                return connector;
            }
        }
        return null;
    }

    private Map parseConnectorArgs(Connector connector, String argString) {
        StringTokenizer tokenizer = new StringTokenizer(argString, ",");
        Map arguments = connector.defaultArguments();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            int index = token.indexOf('=');
            if (index == -1) {
                throw new IllegalArgumentException("Illegal connector argument: " + token);
            }
            String name = token.substring(0, index);
            String value = token.substring(index + 1);
            Connector.Argument argument = (Connector.Argument) arguments.get(name);
            if (argument == null) {
                throw new IllegalArgumentException("Argument " + name + "is not defined for connector: " + connector.name());
            }
            argument.setValue(value);
        }
        return arguments;
    }

    VMConnection(String connectSpec, int traceFlags) {
        String nameString;
        String argString;
        int index = connectSpec.indexOf(':');
        if (index == -1) {
            nameString = connectSpec;
            argString = "";
        } else {
            nameString = connectSpec.substring(0, index);
            argString = connectSpec.substring(index + 1);
        }
        connector = findConnector(nameString);
        if (connector == null) {
            throw new IllegalArgumentException("No connector named: " + nameString);
        }
        connectorArgs = parseConnectorArgs(connector, argString);
        this.traceFlags = traceFlags;
    }

    synchronized VirtualMachine open() {
        if (connector instanceof LaunchingConnector) {
            vm = launchTarget();
        } else if (connector instanceof AttachingConnector) {
            vm = attachTarget();
        } else if (connector instanceof ListeningConnector) {
            vm = listenTarget();
        } else {
            throw new InternalError("Invalid connect type");
        }
        vm.setDebugTraceMode(traceFlags);
        System.out.println("JVM version:" + vm.version());
        System.out.println("JDI version: " + Bootstrap.virtualMachineManager().majorInterfaceVersion() + "." + Bootstrap.virtualMachineManager().minorInterfaceVersion());
        System.out.println("JVM description: " + vm.description());
        return vm;
    }

    boolean setConnectorArg(String name, String value) {
        if (vm != null) {
            return false;
        }
        Connector.Argument argument = (Connector.Argument) connectorArgs.get(name);
        if (argument == null) {
            return false;
        }
        argument.setValue(value);
        return true;
    }

    String connectorArg(String name) {
        Connector.Argument argument = (Connector.Argument) connectorArgs.get(name);
        if (argument == null) {
            return "";
        }
        return argument.value();
    }

    public synchronized VirtualMachine vm() {
        if (vm == null) {
            throw new InternalError("VM not connected");
        } else {
            return vm;
        }
    }

    boolean isOpen() {
        return (vm != null);
    }

    boolean isLaunch() {
        return (connector instanceof LaunchingConnector);
    }

    Connector connector() {
        return connector;
    }

    boolean isListen() {
        return (connector instanceof ListeningConnector);
    }

    boolean isAttach() {
        return (connector instanceof AttachingConnector);
    }

    private synchronized void notifyOutputComplete() {
        outputCompleteCount++;
        notifyAll();
    }

    private synchronized void waitOutputComplete() {
        if (process != null) {
            while (outputCompleteCount < 2) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void disposeVM() {
        try {
            if (vm != null) {
                vm.dispose();
                vm = null;
            }
        } finally {
            if (process != null) {
                process.destroy();
                process = null;
            }
            waitOutputComplete();
        }
    }

    private void dumpStream(InputStream stream) throws IOException {
        PrintStream outStream = System.out;
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        String line;
        while (true) {
            try {
                line = in.readLine();
                if (line == null) {
                    break;
                }
                outStream.println(line);
            } catch (IOException ieo) {
                String s = ieo.getMessage();
                if (s.startsWith("Bad file number")) {
                    break;
                }
                throw ieo;
            } catch (NullPointerException npe) {
                throw new IOException("Bug 4728096 in Java io may cause in.readLine() to throw a NULL pointer exception");
            }
        }
    }

    private void displayRemoteOutput(final InputStream stream) {
        Thread thr = new Thread("output reader") {

            public void run() {
                try {
                    dumpStream(stream);
                } catch (IOException ex) {
                    System.err.println("IOException reading output of child java interpreter:" + ex.getMessage());
                } finally {
                    notifyOutputComplete();
                }
            }
        };
        thr.setPriority(Thread.MAX_PRIORITY - 1);
        thr.start();
    }

    private void dumpFailedLaunchInfo(Process process) {
        try {
            dumpStream(process.getErrorStream());
            dumpStream(process.getInputStream());
        } catch (IOException e) {
            System.err.println("Unable to display process output: " + e.getMessage());
        }
    }

    private VirtualMachine launchTarget() {
        LaunchingConnector launcher = (LaunchingConnector) connector;
        try {
            VirtualMachine vm = launcher.launch(connectorArgs);
            process = vm.process();
            displayRemoteOutput(process.getErrorStream());
            displayRemoteOutput(process.getInputStream());
            return vm;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("\n Unable to launch target VM.");
        } catch (IllegalConnectorArgumentsException icae) {
            icae.printStackTrace();
            System.err.println("\n Internal debugger error.");
        } catch (VMStartException vmse) {
            System.err.println(vmse.getMessage() + "\n");
            dumpFailedLaunchInfo(vmse.process());
            System.err.println("\n Target VM failed to initialize.");
        }
        return null;
    }

    private VirtualMachine attachTarget() {
        AttachingConnector attacher = (AttachingConnector) connector;
        try {
            return attacher.attach(connectorArgs);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("\n Unable to attach to target VM.");
        } catch (IllegalConnectorArgumentsException icae) {
            icae.printStackTrace();
            System.err.println("\n Internal debugger error.");
        }
        return null;
    }

    private VirtualMachine listenTarget() {
        ListeningConnector listener = (ListeningConnector) connector;
        try {
            String retAddress = listener.startListening(connectorArgs);
            System.out.println("Listening at address: " + retAddress);
            vm = listener.accept(connectorArgs);
            listener.stopListening(connectorArgs);
            return vm;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("\n Unable to attach to target VM.");
        } catch (IllegalConnectorArgumentsException icae) {
            icae.printStackTrace();
            System.err.println("\n Internal debugger error.");
        }
        return null;
    }
}
