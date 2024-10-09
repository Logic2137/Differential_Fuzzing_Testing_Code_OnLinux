

package com.oracle.java.testlibrary;


public enum ExitCode {
    OK(0),
    FAIL(1),
    CRASH(134);

    public final int value;

    ExitCode(int value) {
        this.value = value;
    }
}

