


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RegisteringActivatable extends Remote {
    public void shutdown() throws Exception;
}
