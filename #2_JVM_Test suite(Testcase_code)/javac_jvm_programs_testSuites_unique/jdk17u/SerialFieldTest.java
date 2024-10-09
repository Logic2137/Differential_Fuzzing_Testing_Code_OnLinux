import java.io.ObjectStreamField;
import java.io.Serializable;

public class SerialFieldTest implements Serializable {

    private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("i", int.class), new ObjectStreamField("count", Integer.TYPE), new ObjectStreamField("name", String.class) };
}
