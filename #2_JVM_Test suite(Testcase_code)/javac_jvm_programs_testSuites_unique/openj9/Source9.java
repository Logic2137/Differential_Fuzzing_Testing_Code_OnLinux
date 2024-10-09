
package com.ibm.jvmti.tests.BCIWithASM;

public class Source9 {
	public int returnOne() {
		
		int returnVal = -1;
		
		try {
			returnVal = 1/0;
		} catch ( ArithmeticException e ) {
			returnVal = 0;		
		}
		
		try {
			returnVal = 10;
		} catch ( NullPointerException e ) {
			returnVal = -2;
		}
		
		return returnVal;
	}
}
