


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.Field;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.ModificationWatchpointEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ModificationWatchpointRequest;

public class FieldMonitor {

  public static final String CLASS_NAME = "TestPostFieldModification";
  public static final String FIELD_NAME = "value";
  public static final String ARGUMENTS = "-Xshare:off -XX:+PrintGC";

  public static void main(String[] args)
      throws IOException, InterruptedException {

    StringBuffer sb = new StringBuffer();

    for (int i=0; i < args.length; i++) {
        sb.append(' ');
        sb.append(args[i]);
    }
    
    VirtualMachine vm = launchTarget(CLASS_NAME);

    System.out.println("Vm launched");
    
    List<ReferenceType> referenceTypes = vm
        .classesByName(CLASS_NAME);
    for (ReferenceType refType : referenceTypes) {
      addFieldWatch(vm, refType);
    }
    
    addClassWatch(vm);

    
    EventQueue eventQueue = vm.eventQueue();
    

    Process process = vm.process();


    
    Thread outThread = new StreamRedirectThread("out reader", process.getInputStream());
    Thread errThread = new StreamRedirectThread("error reader", process.getErrorStream());

    errThread.start();
    outThread.start();


    vm.resume();
    boolean connected = true;
    while (connected) {
      EventSet eventSet = eventQueue.remove();
      for (Event event : eventSet) {
        if (event instanceof VMDeathEvent
            || event instanceof VMDisconnectEvent) {
          
          connected = false;
        } else if (event instanceof ClassPrepareEvent) {
          
          System.out.println("ClassPrepareEvent");
          ClassPrepareEvent classPrepEvent = (ClassPrepareEvent) event;
          ReferenceType refType = classPrepEvent
              .referenceType();
          addFieldWatch(vm, refType);
        } else if (event instanceof ModificationWatchpointEvent) {
          System.out.println("sleep for 500 ms");
          Thread.sleep(500);
          System.out.println("resume...");

          ModificationWatchpointEvent modEvent = (ModificationWatchpointEvent) event;
          System.out.println("old="
              + modEvent.valueCurrent());
          System.out.println("new=" + modEvent.valueToBe());
          System.out.println();
        }
      }
      eventSet.resume();
    }
    
    try {
        errThread.join(); 
        outThread.join();
    } catch (InterruptedException exc) {
        
    }
  }

  
  static LaunchingConnector findLaunchingConnector() {
    List <Connector> connectors = Bootstrap.virtualMachineManager().allConnectors();
    Iterator <Connector> iter = connectors.iterator();
    while (iter.hasNext()) {
      Connector connector = iter.next();
      if (connector.name().equals("com.sun.jdi.CommandLineLaunch")) {
        return (LaunchingConnector)connector;
      }
    }
    throw new Error("No launching connector");
  }
  
 static Map <String,Connector.Argument> connectorArguments(LaunchingConnector connector, String mainArgs) {
      Map<String,Connector.Argument> arguments = connector.defaultArguments();
      for (String key : arguments.keySet()) {
        System.out.println(key);
      }

      Connector.Argument mainArg = (Connector.Argument)arguments.get("main");
      if (mainArg == null) {
          throw new Error("Bad launching connector");
      }
      mainArg.setValue(mainArgs);

      Connector.Argument optionsArg = (Connector.Argument)arguments.get("options");
      if (optionsArg == null) {
        throw new Error("Bad launching connector");
      }
      optionsArg.setValue(ARGUMENTS);
      return arguments;
  }

 static VirtualMachine launchTarget(String mainArgs) {
    LaunchingConnector connector = findLaunchingConnector();
    Map  arguments = connectorArguments(connector, mainArgs);
    try {
        return (VirtualMachine) connector.launch(arguments);
    } catch (IOException exc) {
        throw new Error("Unable to launch target VM: " + exc);
    } catch (IllegalConnectorArgumentsException exc) {
        throw new Error("Internal error: " + exc);
    } catch (VMStartException exc) {
        throw new Error("Target VM failed to initialize: " +
                        exc.getMessage());
    }
}


  private static void addClassWatch(VirtualMachine vm) {
    EventRequestManager erm = vm.eventRequestManager();
    ClassPrepareRequest classPrepareRequest = erm
        .createClassPrepareRequest();
    classPrepareRequest.addClassFilter(CLASS_NAME);
    classPrepareRequest.setEnabled(true);
  }


  private static void addFieldWatch(VirtualMachine vm,
      ReferenceType refType) {
    EventRequestManager erm = vm.eventRequestManager();
    Field field = refType.fieldByName(FIELD_NAME);
    ModificationWatchpointRequest modificationWatchpointRequest = erm
        .createModificationWatchpointRequest(field);
    modificationWatchpointRequest.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
    modificationWatchpointRequest.setEnabled(true);
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
        while ((line = in.readLine ()) != null) {
          System.out.println ("testvm: " + line);
      }
     System.out.flush();
    } catch(IOException exc) {
      System.err.println("Child I/O Transfer - " + exc);
    }
  }
}
