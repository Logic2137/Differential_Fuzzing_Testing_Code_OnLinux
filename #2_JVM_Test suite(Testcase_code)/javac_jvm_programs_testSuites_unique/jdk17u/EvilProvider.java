
package com.evilprovider;

import java.security.*;

public final class EvilProvider extends Provider {

    private static final long serialVersionUID = 11223344550000L;

    public EvilProvider() {
        super("EvilProvider", "1.0", "Evil Provider");
        putService(new Provider.Service(this, "Mac", "HmacSHA1", "com.evilprovider.EvilHmacSHA1", null, null));
    }
}
