
package com.ibm.oti.jvmtests;



public class SupportJVM {
	public native static long GetNanoTimeAdjustment(long time);
	
	static {
		System.loadLibrary("j9vmtest");
	}
}
