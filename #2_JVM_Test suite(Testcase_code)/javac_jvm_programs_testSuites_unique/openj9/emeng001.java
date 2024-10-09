
package com.ibm.jvmti.tests.eventMethodEntryGrow;

public class emeng001 {
	public native void emeng001NativeMethod(String o, int i, long l, String o2);

	public static boolean pass = false;
	public static boolean stackOverFlow = false;
	public static boolean called = false;
	public static emeng001 receiverStatic;
	public static String oStatic = "hey";
	public static int iStatic = 42;
	public static long lStatic = -1L;
	public static String o2Static = "there";

	public void verifyValues(String o, int i, long l, String o2)
	{
		pass = true;
		if (this != receiverStatic) {
			System.out.println("Receiver corrupted");
			pass = false;
		}
		if (o != oStatic) {
			System.out.println("o corrupted");
			pass = false;
		}
		if (i != iStatic) {
			System.out.println("i corrupted");
			pass = false;
		}
		if (l != lStatic) {
			System.out.println("l corrupted");
			pass = false;
		}
		if (o2 != o2Static) {
			System.out.println("o2 corrupted");
			pass = false;
		}
	}

	public static void recurse()
	{
		
		int result = fibonacci(100000);
		System.out.println("fibonacci(100000) = " + result);
	}

	public static int fibonacci(int x) {
		if (x < 3) {
			return 1;
		}
		return (fibonacci(x - 1) + fibonacci(x - 2));
	}
	
	public static void growStack()
	{
		called = true;
		try {
			recurse();
		} catch (StackOverflowError e) {
			stackOverFlow = true;
		}
	}
	
	public boolean testMethodEntryWithStackGrow()
	{
		receiverStatic = this;
		emeng001NativeMethod(oStatic, iStatic, lStatic, o2Static);		
		if (!called) {
			System.out.println("growStack was not called");			
		}
		if (!stackOverFlow) {
			System.out.println("No overflow detected");			
		}
		return pass && called && stackOverFlow;
	}
	
	public String helpMethodEntryWithStackGrow()
	{
		return "Test growing the stack during a MethodEntry event on a native method";
	}
}
