

import javax.tools.SimpleJavaFileObject;
import java.io.Serializable;

public class WarnSerializableLambdaTestc {
    public interface SerializableIntf<T> extends Serializable {
        String get(T o);
    }

    private void dontWarn() {
        SerializableIntf<SimpleJavaFileObject> s = SimpleJavaFileObject::getName;
    }
}
