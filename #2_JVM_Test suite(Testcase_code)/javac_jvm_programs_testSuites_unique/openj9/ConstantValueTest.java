

package com.ibm.j9.recreateclass.testclasses;


public class ConstantValueTest {
	final static int iConst = 1;
	final static float fConst = 2.0f;
	final static long lConst = 10l;
	final static double dConst = 20.0d;
	final static String sConst = "ConstantString";
	final static ConstantValueTest obj = new ConstantValueTest();
	
	public static long foo() {
		return 10l;	
	}
	public static double bar() {
		return 20.0d;	
	}
}
