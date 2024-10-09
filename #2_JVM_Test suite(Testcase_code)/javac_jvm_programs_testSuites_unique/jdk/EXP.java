

package com.sun.exp.provider;

import java.security.Provider;

public class EXP extends Provider {

    public EXP() {
        super("EXP", 0.0d, "Test provider");
        put("MessageDigest.SHA1", "com.sun.exp.provider.SHA");
    }
}
