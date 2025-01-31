import com.sun.jdi.event.*;

public interface TargetListener {

    boolean shouldRemoveListener();

    void eventSetReceived(EventSet set);

    void eventSetComplete(EventSet set);

    void eventReceived(Event event);

    void breakpointReached(BreakpointEvent event);

    void exceptionThrown(ExceptionEvent event);

    void stepCompleted(StepEvent event);

    void classPrepared(ClassPrepareEvent event);

    void classUnloaded(ClassUnloadEvent event);

    void methodEntered(MethodEntryEvent event);

    void methodExited(MethodExitEvent event);

    void monitorContendedEnter(MonitorContendedEnterEvent event);

    void monitorContendedEntered(MonitorContendedEnteredEvent event);

    void monitorWait(MonitorWaitEvent event);

    void monitorWaited(MonitorWaitedEvent event);

    void fieldAccessed(AccessWatchpointEvent event);

    void fieldModified(ModificationWatchpointEvent event);

    void threadStarted(ThreadStartEvent event);

    void threadDied(ThreadDeathEvent event);

    void vmStarted(VMStartEvent event);

    void vmDied(VMDeathEvent event);

    void vmDisconnected(VMDisconnectEvent event);
}
