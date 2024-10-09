import java.rmi.*;
import java.rmi.server.*;

public class NotExtending implements Remote {

    private Remote stub;

    private int hashValue;

    private boolean hashValueInitialized = false;

    public Remote export() throws RemoteException {
        stub = UnicastRemoteObject.exportObject(this);
        setHashValue(stub.hashCode());
        return stub;
    }

    public void unexport() throws RemoteException {
        UnicastRemoteObject.unexportObject(this, true);
    }

    private void setHashValue(int value) {
        hashValue = value;
        hashValueInitialized = true;
    }

    public int hashCode() {
        if (!hashValueInitialized) {
            throw new AssertionError("hashCode() invoked before hashValue initialized");
        }
        return hashValue;
    }

    public boolean equals(Object obj) {
        return stub.equals(obj);
    }

    public static void main(String[] args) throws Exception {
        NotExtending server = null;
        try {
            server = new NotExtending();
            Remote stub = server.export();
            System.err.println("Server exported without invoking hashCode().");
            if (server.equals(stub)) {
                System.err.println("server.equals(stub) returns true");
            } else {
                throw new AssertionError("server.equals(stub) returns false");
            }
            if (stub.equals(server)) {
                System.err.println("stub.equals(server) returns true");
            } else {
                throw new AssertionError("stub.equals(server) returns false");
            }
        } finally {
            if (server != null) {
                server.unexport();
            }
        }
    }
}
