
package com.ibm.jvmti.tests.decompResolveFrame;

public class decomp005
{
	private static boolean pass;
	private static boolean entered;
	private native boolean triggerDecompile();
	
	public void interp(boolean jitted) throws Throwable {
		if (jitted) {
			if (!triggerDecompile()) {
				System.out.println("FAIL: Failed to trigger decompilation");
				return;
			}
		}
		throw new InterruptedException();
	}

	public synchronized void jitInline(boolean jitted) throws Throwable {
		
		
		
		if (!entered) {
			entered = true;
			interp(jitted);
		}
		System.out.println("FAIL: jitInline was restarted at bytecode 0 (crash likely to follow)");
		pass = false;
	}

	public boolean jitOuter(boolean jitted) {
		entered = false;
		pass = true;
		try {
			jitInline(jitted);
		} catch(Throwable e) {
			if (jitted) {
				System.out.println("Exception caught in jitOuter: " + e);
			}
			return pass;
		}
		System.out.println("FAIL: Exception not caught in jitOuter");
		return false;
	}

	public boolean testInlinedSyncDecompile() {
		decomp005 o = new decomp005();
		try {
			
			if (!o.jitOuter(false)) {
				System.out.println("FAIL: Warmup failed");
				return false;
			}
			
			return o.jitOuter(true);
		} catch(Throwable t) {
			System.out.println("FAIL: Uncaught exception during test: " + t);
			return false;
		}
	}

	public String helpInlinedSyncDecompile() {
		return "decompile at exception catch with an inlined synchronized method"; 
	}
}
