import java.rmi.activation.ActivationGroupID;
import java.rmi.registry.Registry;
import java.rmi.Remote;
import java.rmi.RemoteException;

interface CanCreateStubs extends java.rmi.Remote {

    Registry getRegistry() throws RemoteException;

    void shutdown() throws Exception;

    Object getForbiddenClass() throws Exception;

    ActivationGroupID returnGroupID() throws RemoteException;
}
