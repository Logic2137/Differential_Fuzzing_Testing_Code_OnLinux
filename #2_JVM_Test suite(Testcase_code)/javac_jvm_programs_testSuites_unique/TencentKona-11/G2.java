

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface G2 extends Remote {
    void m(Collection c) throws RemoteException;
}
