


import com.sun.jdi.Bootstrap;
import com.sun.jdi.connect.Connector;
import java.util.List;

public class ListConnectors {

    public void list() {
        List<Connector> l = Bootstrap.virtualMachineManager().allConnectors();

        for(Connector c: l) {
            System.out.println(c.name());
        }
    }
}
