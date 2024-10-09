



package org.example.authz;

import javax.naming.ldap.*;

public class AuthzIdRequestControl extends BasicControl {

    public static final String OID = "2.16.840.1.113730.3.4.16";

    public AuthzIdRequestControl() {
        super(OID, true, null);
    }

    public AuthzIdRequestControl(boolean criticality) {
        super(OID, criticality, null);
    }
}
