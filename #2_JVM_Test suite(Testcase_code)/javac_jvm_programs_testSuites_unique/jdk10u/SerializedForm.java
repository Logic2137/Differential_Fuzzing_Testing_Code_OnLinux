

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;

public class SerializedForm implements Serializable {

    
    @Deprecated
    private static final ObjectStreamField[] serialPersistentFields = {
        new ObjectStreamField("i", int.class),
        new ObjectStreamField("count", Integer.TYPE),
        new ObjectStreamField("name", String.class)
    };

    

    
    private void readObject(ObjectInputStream s) throws IOException {}

    
    private void writeObject(ObjectOutputStream s) throws IOException {}

    
    protected Object readResolve() throws IOException {return null;}

    
    protected Object writeReplace() throws IOException {return null;}

    
    protected Object readObjectNoData() throws IOException {
        return null;
    }
}
