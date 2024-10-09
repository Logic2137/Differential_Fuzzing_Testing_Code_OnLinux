

package failureAtomicity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;



public class SerialRef implements Serializable {
    private static final long serialVersionUID = -0L;
    public static Object obj;

    @SuppressWarnings("serial") 
    private final Object ref;

    public SerialRef(Object ref) {
        this.ref = ref;
    }

    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        SerialRef.obj = ref;
    }
}
