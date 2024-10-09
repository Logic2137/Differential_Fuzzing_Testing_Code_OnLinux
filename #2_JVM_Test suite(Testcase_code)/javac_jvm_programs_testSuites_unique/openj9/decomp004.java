
package com.ibm.jvmti.tests.decompResolveFrame;

public class decomp004
{
	private static native boolean triggerDecompile();
	
	public synchronized void jitMe() {
		if (!triggerDecompile()) {
			throw new Error("failed to enable single step");
		}
	}

	public boolean testSyncDecompile()
	{
		try {
			new decomp004().jitMe();
		} catch(Throwable t) {
			System.out.println("exception during test: " + t);
			return false;
		}
		return true;
	}

	public String helpSyncDecompile()
	{
		return "decompile a synchronized method"; 
	}
}
