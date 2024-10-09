import java.rmi.*;

public interface TestIface extends Remote {

    public String testCall(String ign) throws RemoteException;
}
