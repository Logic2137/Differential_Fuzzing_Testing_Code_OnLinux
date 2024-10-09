

package jdk.test.lib.security;

import java.util.List;

public final class JDKSecurityProperties {

    public static final List<String> jdkProps = List.of(
        "crypto.policy",
        "jceks.key.serialFilter",
        "jdk.certpath.disabledAlgorithms",
        "keystore.type",
        "krb5.kdc.bad.policy",
        "login.config",
        "networkaddress.cache.ttl",
        "ocsp.responderURL",
        "package.access",
        "policy.allowSystemProperty",
        "securerandom.drbg.config",
        "security.provider.1",
        "ssl.KeyManagerFactory.algorithm",
        "sun.rmi.registry.registryFilter"
    );

    public static List<String> getKeys() {
        return jdkProps;
    }
}
