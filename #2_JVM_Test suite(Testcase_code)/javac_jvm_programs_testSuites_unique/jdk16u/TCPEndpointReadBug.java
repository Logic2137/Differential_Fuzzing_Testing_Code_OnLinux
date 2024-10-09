


import java.io.IOException;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;


public class TCPEndpointReadBug {

    public static void main(String[] args) throws Exception {
        final I implC = new C();
        final I remoteC = (I)UnicastRemoteObject.exportObject(
                implC, 0, new CSF(), RMISocketFactory.getDefaultSocketFactory());

        
        remoteC.echo(remoteC);

        
        remoteC.echo(null);
    }

    interface I extends Remote {
        I echo(I intf) throws RemoteException;
    }

    static class C implements I {
        @Override
        public I echo(I intf) {
            try {
                return  (I)UnicastRemoteObject
                    .exportObject(new C(),0, new CSF(), RMISocketFactory.getDefaultSocketFactory());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    
    static class CSF implements Serializable, RMIClientSocketFactory {
        private static final long serialVersionUID = 1;

        @Override
        public boolean equals(Object object) {
            return object instanceof CSF;
        }

        @Override
        public int hashCode() {
            return 424242;
        }

        @Override
        public Socket createSocket(String host, int port)
            throws IOException {

            final RMIClientSocketFactory defaultFactory =
                RMISocketFactory.getDefaultSocketFactory();
            return defaultFactory.createSocket(host, port);
        }

        
        private Object writeReplace() {
            return null;
        }

        
        @SuppressWarnings("unused")
        private void readObject(ObjectInputStream in) {
            throw new AssertionError();
        }
    }
}
