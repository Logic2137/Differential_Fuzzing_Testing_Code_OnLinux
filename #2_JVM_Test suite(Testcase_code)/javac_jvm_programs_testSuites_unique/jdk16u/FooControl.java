



package org.example.foo;

import javax.naming.ldap.*;

public class FooControl extends BasicControl {

    public FooControl() {
        super("1.2.3.4.5.6.7.8.9", true, null);
    }

    public FooControl(boolean criticality) {
        super("1.2.3.4.5.6.7.8.9", criticality, null);
    }
}
