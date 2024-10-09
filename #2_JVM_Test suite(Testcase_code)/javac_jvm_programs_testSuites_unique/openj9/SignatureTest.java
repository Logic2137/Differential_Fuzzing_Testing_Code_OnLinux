

package com.ibm.j9.recreateclass.testclasses;

import java.util.ArrayList;
import java.util.List;


public class SignatureTest<T> {
	List<T> myList = new ArrayList<T>();
	
	public <T> void doSomething(ArrayList<T> list) {
		System.out.println("list(0): " + list.get(0));
	}
}
