import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Location;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.Connector.Argument;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequestManager;

public class GetObjectLockCount {

    public static final String CLASS_NAME = "RecursiveObjectLock";

    public static final String METHOD_NAME = "breakpoint1";

    public static final String ARGUMENTS = "";

    static LaunchingConnector findLaunchingConnector() {
        List<Connector> connectors = Bootstrap.virtualMachineManager().allConnectors();
        Iterator<Connector> iter = connectors.iterator();
        while (iter.hasNext()) {
            Connector connector = iter.next();
            if (connector.name().equals("com.sun.jdi.CommandLineLaunch")) {
                return (LaunchingConnector) connector;
            }
        }
        throw new Error("No launching connector");
    }

    static VirtualMachine launchTarget(String mainArgs) {
        LaunchingConnector connector = findLaunchingConnector();
        Map<String, Argument> arguments = connectorArguments(connector, mainArgs);
        try {
            return (VirtualMachine) connector.launch(arguments);
        } catch (IOException exc) {
            throw new Error("Unable to launch target VM: " + exc);
        } catch (IllegalConnectorArgumentsException exc) {
            throw new Error("Internal error: " + exc);
        } catch (VMStartException exc) {
            throw new Error("Target VM failed to initialize: " + exc.getMessage());
        }
    }

    static Map<String, Connector.Argument> connectorArguments(LaunchingConnector connector, String mainArgs) {
        Map<String, Connector.Argument> arguments = connector.defaultArguments();
        Connector.Argument mainArg = (Connector.Argument) arguments.get("main");
        if (mainArg == null) {
            throw new Error("Bad launching connector");
        }
        mainArg.setValue(mainArgs);
        Connector.Argument optionsArg = (Connector.Argument) arguments.get("options");
        if (optionsArg == null) {
            throw new Error("Bad launching connector");
        }
        optionsArg.setValue(ARGUMENTS);
        return arguments;
    }

    private static void addClassWatch(VirtualMachine vm) {
        EventRequestManager erm = vm.eventRequestManager();
        ClassPrepareRequest classPrepareRequest = erm.createClassPrepareRequest();
        classPrepareRequest.addClassFilter(CLASS_NAME);
        classPrepareRequest.setEnabled(true);
    }

    private static void addBreakpoint(VirtualMachine vm, ReferenceType refType) {
        Location breakpointLocation = null;
        List<Location> locs;
        try {
            locs = refType.allLineLocations();
            for (Location loc : locs) {
                if (loc.method().name().equals(METHOD_NAME)) {
                    breakpointLocation = loc;
                    break;
                }
            }
        } catch (AbsentInformationException e) {
            e.printStackTrace();
        }
        if (breakpointLocation != null) {
            EventRequestManager evtReqMgr = vm.eventRequestManager();
            BreakpointRequest bReq = evtReqMgr.createBreakpointRequest(breakpointLocation);
            bReq.setSuspendPolicy(BreakpointRequest.SUSPEND_ALL);
            bReq.enable();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        VirtualMachine vm = launchTarget(CLASS_NAME);
        EventQueue eventQueue = vm.eventQueue();
        boolean launched = false;
        while (!launched) {
            EventSet eventSet = eventQueue.remove();
            for (Event event : eventSet) {
                if (event instanceof VMStartEvent) {
                    System.out.println("Vm launched");
                    List<ReferenceType> referenceTypes = vm.classesByName(CLASS_NAME);
                    for (ReferenceType refType : referenceTypes) {
                        System.out.println("Found Class");
                        addBreakpoint(vm, refType);
                    }
                    addClassWatch(vm);
                    vm.resume();
                    launched = true;
                }
            }
        }
        Process process = vm.process();
        Thread outThread = new StreamRedirectThread("out reader", process.getInputStream());
        Thread errThread = new StreamRedirectThread("error reader", process.getErrorStream());
        int recursionCount = -1;
        errThread.start();
        outThread.start();
        boolean connected = true;
        while (connected) {
            EventSet eventSet = eventQueue.remove();
            for (Event event : eventSet) {
                if (event instanceof VMDeathEvent || event instanceof VMDisconnectEvent) {
                    connected = false;
                } else if (event instanceof ClassPrepareEvent) {
                    System.out.println("ClassPrepareEvent");
                    ClassPrepareEvent classPrepEvent = (ClassPrepareEvent) event;
                    ReferenceType refType = classPrepEvent.referenceType();
                    addBreakpoint(vm, refType);
                } else if (event instanceof BreakpointEvent) {
                    recursionCount = getLockRecursions(vm);
                    System.out.println("resume...");
                }
            }
            eventSet.resume();
        }
        try {
            errThread.join();
            outThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (recursionCount != 3) {
            throw new AssertionError("recursions: expected 3, but was " + recursionCount);
        }
    }

    public static int getLockRecursions(VirtualMachine vm) {
        List<ThreadReference> threads = vm.allThreads();
        for (ThreadReference thread : threads) {
            if (thread.name().equals("main")) {
                System.out.println("Found main thread.");
                try {
                    StackFrame frame = thread.frame(3);
                    return frame.thisObject().entryCount();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Main thread not found!");
        }
        return -1;
    }
}

class StreamRedirectThread extends Thread {

    private final BufferedReader in;

    private static final int BUFFER_SIZE = 2048;

    StreamRedirectThread(String name, InputStream in) {
        super(name);
        this.in = new BufferedReader(new InputStreamReader(in));
    }

    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("testvm: " + line);
            }
            System.out.flush();
        } catch (IOException exc) {
            System.err.println("Child I/O Transfer - " + exc);
            exc.printStackTrace();
        }
    }
}
