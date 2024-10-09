import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;

public class TestDnsObject {

    private String value;

    public TestDnsObject(Attribute attr) {
        StringBuilder buf = new StringBuilder();
        try {
            NamingEnumeration enumObj = attr.getAll();
            while (enumObj.hasMore()) {
                buf.append(enumObj.next());
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
        value = buf.toString();
    }

    public String toString() {
        return value;
    }
}
