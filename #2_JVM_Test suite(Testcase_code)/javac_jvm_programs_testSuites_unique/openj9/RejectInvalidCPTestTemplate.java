


package org.openj9.test.bsmargs;

import java.util.*;
public class RejectInvalidCPTestTemplate {
	public static void main(String[] args) {
		List<Integer> list=new ArrayList<Integer>();
		list.add(100);
		list.forEach((n)->System.out.println(n));
	}
}
