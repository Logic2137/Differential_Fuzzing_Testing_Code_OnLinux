
package com.ibm.jvmti.tests.BCIWithASM;

public class Source10 {
	public int returnOne() {
		
		int counter = 0 ; 
		
		for ( int i = 0 ; i < 10 ; i++ ) {
			counter++;
			
			
				
				
			
		}
		
		for ( int k = 0 ; k < 5 ; k++ ) {
			counter--;
		}
		
		return counter;
	}
}
