

import java.rmi.*;
import java.rmi.server.*;


interface TellServerName extends Remote {
    void tellServerName (String serverName) throws RemoteException;
}
