import java.rmi.MarshalledObject;

public class NullReference {

    public static void main(String[] args) throws Throwable {
        new MarshalledObject(null);
    }
}
