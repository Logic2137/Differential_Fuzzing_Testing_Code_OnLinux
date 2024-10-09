

package com.ibm.j9.tests.jeptests;


public class StaticLinking {
	public static void main(String[] args) {
		StaticLinking instance = new StaticLinking();

		
		
		System.loadLibrary("testlibA");
		System.loadLibrary("testlibB");

		
		
		System.out.println("[MSG] Calling native instance method fooImpl.");
		instance.fooImpl();
		System.out.println("[MSG] Calling native static method barImpl.");
		barImpl();
	}
	private native void fooImpl();
	private static native void barImpl();
}
