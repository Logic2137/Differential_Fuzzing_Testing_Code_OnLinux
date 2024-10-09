import java.rmi.*;

interface LeaseLeak extends Remote {

    void ping() throws RemoteException;
}
