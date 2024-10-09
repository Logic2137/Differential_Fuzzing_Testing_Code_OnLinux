import com.sun.jndi.toolkit.dir.HierMemDirCtx;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;

public class FedSubordinateNs {

    static DirContext root = null;

    static {
        try {
            Attributes rootAttrs = new BasicAttributes("name", "root");
            rootAttrs.put("description", "in-memory root");
            Attributes aAttrs = new BasicAttributes("name", "a");
            aAttrs.put("description", "in-memory 1st level");
            Attributes bAttrs = new BasicAttributes("name", "b");
            bAttrs.put("description", "in-memory 2nd level");
            Attributes cAttrs = new BasicAttributes("name", "c");
            cAttrs.put("description", "in-memory 3rd level");
            root = new HierMemDirCtx();
            root.modifyAttributes("", DirContext.ADD_ATTRIBUTE, rootAttrs);
            root.createSubcontext("a", aAttrs);
            root.createSubcontext("a/b", bAttrs);
            root.createSubcontext("a/b/c", cAttrs);
            root.createSubcontext("x");
        } catch (NamingException e) {
            System.out.println("Problem in static initialization of root:" + e);
        }
    }

    static DirContext getRoot() {
        return root;
    }
}
