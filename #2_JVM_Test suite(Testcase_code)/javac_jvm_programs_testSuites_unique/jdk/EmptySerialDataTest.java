

import java.io.ObjectOutputStream;
import java.io.Serializable;


public class EmptySerialDataTest implements Serializable {  EmptySerialDataTest() { }
    
    private void writeObject(ObjectOutputStream s) { }
}
