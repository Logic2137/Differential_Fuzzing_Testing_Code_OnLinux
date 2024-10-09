import java.io.Serializable;

public class RjmxMBeanParameter implements Serializable {

    public String name = "unset";

    public RjmxMBeanParameter() {
    }

    public RjmxMBeanParameter(String name) {
        this.name = name;
    }

    public boolean equals(Object obj) {
        if (this.name.equals(((RjmxMBeanParameter) obj).name)) {
            return true;
        } else {
            return false;
        }
    }
}
