



package bench.rmi;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface BenchServer extends Remote {
    
    public interface RemoteObjectFactory extends Serializable {
        Remote create() throws RemoteException;
    }

    
    public interface Task extends Serializable {
        Object execute() throws Exception;
    }

    
    Remote create(RemoteObjectFactory factory) throws RemoteException;

    
    boolean unexport(Remote obj, boolean force) throws RemoteException;

    
    Object execute(Task task) throws Exception;

    
    void gc() throws RemoteException;

    
    void terminate(int delay) throws RemoteException;
}
