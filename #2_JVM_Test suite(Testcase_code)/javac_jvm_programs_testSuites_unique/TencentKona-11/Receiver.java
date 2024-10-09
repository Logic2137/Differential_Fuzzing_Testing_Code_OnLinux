


import java.rmi.Remote;
import java.rmi.RemoteException;
public interface Receiver extends Remote {
    public void receive(Object obj) throws RemoteException;
}
