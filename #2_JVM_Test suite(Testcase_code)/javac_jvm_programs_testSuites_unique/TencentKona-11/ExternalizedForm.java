

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ExternalizedForm implements Externalizable {

    
    public void writeExternal(ObjectOutput oo) throws IOException {}

    
    public void readExternal(ObjectInput oi) throws IOException, ClassNotFoundException {}
}
