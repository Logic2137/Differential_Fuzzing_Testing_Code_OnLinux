



import java.lang.reflect.Field;
import java.util.ArrayList;
import org.w3c.dom.Element;

public class CoerceNullToMoreSpecificTypeTest {
    abstract class NodeImpl {
    }

    NodeImpl ownerNode;

    public Element getElement() {
        return (Element) (isOwned() ? ownerNode : null);
    }

    boolean isOwned() {
        return true;
    }

    static void processArrays(boolean expectNulls, Object [] nulla, Object [][] nullaa) {
        if (expectNulls) {
            if (nulla != null || nullaa != null) {
                throw new AssertionError("Null actual, but not null formal");
            }
        } else {
            if (nulla.length != 123 || nullaa.length != 321)
                throw new AssertionError("Wrong arrays received");
        }
    }

    public static void main(String[] args) {
        ArrayList<Class<?>> typeList = new ArrayList<>();
        Field rf = null;
        typeList.add((rf != null) ? rf.getType() : null);
        processArrays(true, null, null);
        processArrays(false, new Object[123], new Object[321][]);
    }
}
