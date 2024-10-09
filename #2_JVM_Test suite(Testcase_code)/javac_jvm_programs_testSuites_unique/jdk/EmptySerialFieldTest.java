

import java.io.ObjectStreamField;
import java.io.Serializable;


public class EmptySerialFieldTest implements Serializable {  EmptySerialFieldTest() { }

    
    private static final ObjectStreamField[] serialPersistentFields = {
        new ObjectStreamField("empty", String.class),
    };
}
