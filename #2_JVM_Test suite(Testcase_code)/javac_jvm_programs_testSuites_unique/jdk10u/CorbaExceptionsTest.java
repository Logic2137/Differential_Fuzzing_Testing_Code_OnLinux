

import java.rmi.Remote;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CORBA.TypeCodePackage.BadKind;
import org.omg.CORBA.TypeCodePackage.Bounds;

public interface CorbaExceptionsTest extends Remote {
     public void testExceptionInvalidName() throws java.rmi.RemoteException, InvalidName;
     public void testExceptionBounds() throws java.rmi.RemoteException, Bounds;
     public void testExceptionBadKind() throws java.rmi.RemoteException, BadKind;
     public void testExceptionCorba_Bounds() throws java.rmi.RemoteException, org.omg.CORBA.Bounds;
}

