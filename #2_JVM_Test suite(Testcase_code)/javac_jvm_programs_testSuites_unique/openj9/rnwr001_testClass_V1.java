
package com.ibm.jvmti.tests.registerNativesWithRetransformation;

public class rnwr001_testClass_V1 {
	public native int registeredNativeMethod();
	public int getValue() {
		return 1;
	}
}
