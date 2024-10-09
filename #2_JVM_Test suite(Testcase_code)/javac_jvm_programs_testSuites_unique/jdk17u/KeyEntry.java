
package jdk.test.lib.security;

public class KeyEntry {

    public final String keyAlgo;

    public final String keyStr;

    public final String password;

    public final String[] certStrs;

    public KeyEntry(String keyAlgo, String keyStr, String password, String[] certStrs) {
        this.keyAlgo = keyAlgo;
        this.keyStr = keyStr;
        this.password = password;
        this.certStrs = certStrs;
    }

    public KeyEntry(String keyAlgo, String keyStr, String[] certStrs) {
        this(keyAlgo, keyStr, null, certStrs);
    }
}
