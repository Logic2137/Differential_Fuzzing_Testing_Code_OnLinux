
package com.ibm.jvmti.tests.BCIWithASM;

public class Source12 {
	public void method1(){
		for ( int i = 0 ; i < 10 ; i++ ) {
			System.out.print(i);
		}
		System.out.println();
	}
	
	public void method2(){
		for ( int i = 20 ; i < 40 ; i++ ) {
			System.out.print(i);
		}
		System.out.println();
	}
	
}
