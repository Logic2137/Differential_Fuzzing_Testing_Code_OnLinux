import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface G5 extends Remote {

    void m(Vector v) throws RemoteException;
}
