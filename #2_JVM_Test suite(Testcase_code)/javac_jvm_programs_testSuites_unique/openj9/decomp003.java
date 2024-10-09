
package com.ibm.jvmti.tests.decompResolveFrame;

import java.lang.reflect.*;

public class decomp003
{
	public static native boolean startStepping(Thread current);
	public static native boolean stopStepping(Thread current);
	public static boolean pass = true;
	public static Method catcher = null;

	public static void stepOn() {
		if (!startStepping(Thread.currentThread())) {
			pass = false;
		}
	}
	public static void stepOff() {
		if (!stopStepping(Thread.currentThread())) {
			pass = false;
		}
	}
	public static void throwIt() throws InternalError {
    	InternalError e = new InternalError();
    	catcher = null;
    	stepOn();
    	if (true) throw e;
	}
	public static void caught(String description, String name) {
		System.out.println("Successfully caught exception in " + description + " frame");
		if (null == catcher) {
			System.out.println("FAIL: Catcher not detected in JVMTI");
			pass = false;
		} else if (name != catcher.getName()) {
			System.out.println("FAIL: Catcher is " + catcher.getName() + " expected " + name);
			pass = false;
		} else {
			System.out.println("JVMTI detected correct catcher");
		}
	}

	public static void jitTest1b() throws InternalError {
    	throwIt();
	}
	public static void jitTest1a() throws InternalError {
		jitTest1b();
	}
    public static void jitTest1() {
    	try {
    		jitTest1a();
    	} catch(InternalError z) {
    		caught("outer", "jitTest1");
    	}
    	stepOff();
	}

	public static void jitTest2b() throws InternalError {
    	throwIt();
	}
	public static void jitTest2a() {
	   	try {
	   		jitTest2b();
	   	} catch(InternalError z) {
	   		caught("shallowest inline", "jitTest2a");
	   	}
	}
    public static void jitTest2() {
    	jitTest2a();
    	stepOff();
 	}

	public static void jitTest3b() {
	   	try {
	   		throwIt();
	   	} catch(InternalError z) {
	   		caught("deepest inline", "jitTest3b");
    	}
	}
	public static void jitTest3a() {
	   		jitTest3b();
	}
    public static void jitTest3() {
    	jitTest3a();
    	stepOff();
 	}

	public boolean testExceptions()
	{	
		try {
			jitTest1();
		} catch(Throwable t) {
			System.out.println("FAIL: Exception not caught in jitTest1");
			pass = false;
		}

		try {
			jitTest2();
		} catch(Throwable t) {
			System.out.println("FAIL: Exception not caught in jitTest2");
			pass = false;
		}

		try {
			jitTest3();
		} catch(Throwable t) {
			System.out.println("FAIL: Exception not caught in jitTest3");
			pass = false;
		}
		
		return pass;
	}
	
	public String helpExceptions()
	{
		return "Decompile at exception catch at various levels of inlining";
	}
	
}

