import java.rmi.*;

public interface Echo extends Remote {

    byte[] echoNot(byte[] data) throws RemoteException;
}
