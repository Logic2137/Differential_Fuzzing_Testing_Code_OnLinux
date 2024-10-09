
package nsk.jvmti.AttachOnDemand.attach002;

import java.util.Vector;

public class ClassToRedefine {
    public int getSize() {
        int size = 10;
        Vector<String> vector = new Vector<String>();
        for (int i = 0; i < size; i++) {
            vector.add("String-" + i);
        }
        return vector.size();
    }
}
