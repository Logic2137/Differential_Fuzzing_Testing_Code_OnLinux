import java.rmi.Remote;
import java.rmi.RemoteException;

interface MyRMI extends java.rmi.Remote {

    public void printOut(String toPrint) throws RemoteException;

    public void printErr(String toPrint) throws RemoteException;

    public void shutdown() throws Exception;
}
