
package com.ibm.jvmti.tests.BCIWithASM;

public class Source5 {
	public int returnOne() {
		
			try {
				return 1/0;
			} catch ( ArithmeticException e ) {
				return 0;
			}
		
		
		
	}
}
