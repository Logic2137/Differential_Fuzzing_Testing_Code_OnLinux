import java.io.ObjectOutputStream;
import java.io.Serializable;

public class EmptySerialDataTest implements Serializable {

    private void writeObject(ObjectOutputStream s) {
    }
}
