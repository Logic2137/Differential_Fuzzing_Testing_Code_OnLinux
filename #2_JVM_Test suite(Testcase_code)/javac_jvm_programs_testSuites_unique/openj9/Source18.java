
package com.ibm.jvmti.tests.BCIWithASM;

public class Source18 {
	public int method1() {
		int var1 = 1;
		int var2 = 2;
		
		int var3 = add ( var1, var2 );
		
		return var3;
	}
	
	private int add( int var1, int var2 ) {
		return var1 + var2;
	}
}
