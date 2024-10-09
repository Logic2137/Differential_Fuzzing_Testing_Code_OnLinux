import java.beans.Beans;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

public class Test4144543 {

    public static void main(String[] args) throws Exception {
        Class type = Beans.instantiate(null, "Test4144543").getClass();
        Introspector.getBeanInfo(type);
        new PropertyDescriptor("value", type);
        new PropertyDescriptor("value", type, "getValue", "setValue");
    }

    private int value;

    public int getValue() {
        return this.value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
