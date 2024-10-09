import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteExiter extends Remote {

    void exit() throws RemoteException;
}
