


import java.rmi.activation.ActivationID;
import java.rmi.Remote;
import java.rmi.RemoteException;
interface ActivateMe extends Remote {
    public String getMessage() throws RemoteException;
    public String getProperty(String name) throws RemoteException;
    public ActivationID getID() throws RemoteException;
    public void shutdown() throws Exception;
}
