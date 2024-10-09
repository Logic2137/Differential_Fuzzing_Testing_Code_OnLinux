


import java.rmi.Remote;
import java.rmi.RemoteException;
interface Ping extends Remote {
    public void ping() throws RemoteException;
    public void receiveAndPing(Ping p) throws RemoteException;
}
