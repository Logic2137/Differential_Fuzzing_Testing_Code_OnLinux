
package com.ibm.j9.jsr292;


public class BasicStaticGetSetExample {

	
	
	public static int intField;
	public static long longField;
	public static float floatField;
	public static double doubleField;
	public static byte byteField;
	public static short shortField;
	public static boolean booleanField;
	public static char charField;
	public static Object objectField;
	
	static {
		intField = 100;
		longField = 100;
		floatField = (float) 1.23456;
		doubleField = 1.23456;
		byteField = 100;
		shortField = 100;
		booleanField = true;
		charField = 100;
		objectField = new Object();
	}
	
}
