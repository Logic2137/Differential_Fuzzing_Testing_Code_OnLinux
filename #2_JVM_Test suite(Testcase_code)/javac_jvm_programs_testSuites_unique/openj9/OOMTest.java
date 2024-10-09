
package com.ibm.dump.tests.oomtest;

import java.util.Vector;


public class OOMTest {

	
	public static void main(String[] args) {
		Vector<Long> myLongs = new Vector<Long>();
		
		for (long i=0; i<Long.MAX_VALUE; i++) {
			myLongs.add(i);
		}
	}
}
