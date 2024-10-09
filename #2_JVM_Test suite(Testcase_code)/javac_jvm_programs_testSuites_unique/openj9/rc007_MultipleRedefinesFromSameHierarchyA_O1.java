
package com.ibm.jvmti.tests.redefineClasses;

public class rc007_MultipleRedefinesFromSameHierarchyA_O1 {
	double pi;

	public rc007_MultipleRedefinesFromSameHierarchyA_O1() {
		pi = 3.14;
	}
	
	public int meth1() {
		return (int) (10000*pi);
	}
	
	public void add(double val) {
		pi += val;
	}
}
