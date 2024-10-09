
package javax.naming.spi;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.InitialDirContext;

public class NamingManager {

    NamingManager() {
    }

    public static Context getURLContext(String scheme, Hashtable<?, ?> environment) throws NamingException {
        return new InitialDirContext() {

            public Attributes getAttributes(String name, String[] attrIds) throws NamingException {
                return new BasicAttributes() {

                    public Attribute get(String attrID) {
                        BasicAttribute ba = new BasicAttribute(attrID);
                        ba.add("1 1 99 b.com.");
                        ba.add("0 0 88 a.com.");
                        return ba;
                    }
                };
            }
        };
    }
}
