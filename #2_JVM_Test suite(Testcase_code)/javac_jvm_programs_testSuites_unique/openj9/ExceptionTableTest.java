

package com.ibm.j9.recreateclass.testclasses;


public class ExceptionTableTest {
	public void foo(String args[]) {
		int index = -1;
		try {
			args[index] = "Invalid index";
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Exception expected");
		} finally {
			System.out.println("Good bye");
		}
	}
}
