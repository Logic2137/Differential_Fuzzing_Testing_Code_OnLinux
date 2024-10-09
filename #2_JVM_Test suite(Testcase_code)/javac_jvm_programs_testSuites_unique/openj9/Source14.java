
package com.ibm.jvmti.tests.BCIWithASM;

public class Source14 {
	public double method1() {
		double localVar11 = 4;
		double localVar12 = 2;
		double localVar13 = localVar11 / localVar12 ;
		double r = method2();
		double r2 = r / localVar13;
		return r2;
	}
	
	private double method2() {
		double localVar21 = 10;
		double localVar22 = localVar21 * 2;
		return localVar22;
	}
}
