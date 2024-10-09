

package org.openj9.test.classtests;



public class UnsafeBootClass {
	private String str = "This class is used to create an unsafe bootstrap class";
	public void foo() {
		System.out.println(str);
	}
	public void bar() {
		System.out.println("running UnsafeBootClass.bar()");
	}
}
