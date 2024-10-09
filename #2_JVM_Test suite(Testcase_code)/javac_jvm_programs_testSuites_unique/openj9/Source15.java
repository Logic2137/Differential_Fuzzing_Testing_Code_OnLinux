
package com.ibm.jvmti.tests.BCIWithASM;

public class Source15 {
	public int method1() {
		int var1 = 1;
		int var2 = 2;
		int var3 = 0 ; 
		try { 
			var3 = var1/0;
		} catch ( ArithmeticException e ) {
			var3 = var1/var2;
		}
		return var3;
	}
}
