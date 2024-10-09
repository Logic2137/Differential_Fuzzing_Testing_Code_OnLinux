import java.rmi.Remote;
import java.rmi.RemoteException;

public interface P extends Remote {

    void m(boolean b) throws RemoteException;
}
