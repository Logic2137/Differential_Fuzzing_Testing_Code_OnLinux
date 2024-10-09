
package com.ibm.jvmti.tests.BCIWithASM;

public class Source16 {
	public int method1() {
		int var1 = 1;
		int var2 = 2;
		int var3 = 0 ; 
		
		if ( var1 > var2 ) {
			var3 = var1 + 1 ; 
		}
		
		int var4 = var3 + var2;
		
		return var4;
	}
}
