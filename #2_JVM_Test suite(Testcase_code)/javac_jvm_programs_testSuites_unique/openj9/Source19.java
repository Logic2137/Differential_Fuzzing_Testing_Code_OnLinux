
package com.ibm.jvmti.tests.BCIWithASM;

public class Source19 {
	public static int method1() {
		
		int array [] = new int [10];
		
		try {
			int var1 = array[0];
		} catch ( ArrayIndexOutOfBoundsException e ) {
			array[0] = 1;
		} finally {
			array[1] = 2;
		}
		
		return array[0];
	}
}	
