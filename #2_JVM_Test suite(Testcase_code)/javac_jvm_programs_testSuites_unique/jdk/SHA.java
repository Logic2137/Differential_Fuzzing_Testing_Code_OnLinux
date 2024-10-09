

package com.sun.exp.provider;

import java.security.MessageDigestSpi;

public class SHA extends MessageDigestSpi {
    protected void engineReset() {}
    protected void engineUpdate(byte input) {}
    protected void engineUpdate(byte[] input, int offset, int len) {}
    protected byte[] engineDigest() { return null; }
}
