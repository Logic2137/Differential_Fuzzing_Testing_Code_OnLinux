
package com.ibm.dump.tests.excessive_gc;

import sun.misc.*;
import java.util.*;

public class HeapAllocation {

	static Hashtable globalTable = new Hashtable();

	public static void main(String args[]) {
		
		int nThreads=1,nObjects=100000;

		if (args != null && args.length == 2) {
			try {
				nThreads = Integer.parseInt(args[0]);
				nObjects = Integer.parseInt(args[1]);
			} catch (NumberFormatException nfe){
				System.out.println("Test program HeapAllocation.main() - bad parameter(s)");
				return;
			}
		}

		Object[] objects = new Object[nThreads];
		for (int i=0; i<nThreads; i++) {
			objects[i] = new String("object " + i + " ");
			myThread t = new myThread(i);
			t.init(objects[i], objects[i], nObjects);
			t.start();
		}
		System.out.println("Test program com.ibm.dump.tests.HeapAllocation.main() - " + nThreads + " threads running with " + nObjects + " objects");

		
		try {
			java.lang.Thread.sleep(2000);
		} catch (InterruptedException e){
			e.printStackTrace();
			throw new RuntimeException("com.ibm.dump.tests.HeapAllocation.main(): thread sleep exception");
		}
	}

	
	private static class myThread extends Thread {

		String string1,string2;
		int threadID,nObjects;
		Hashtable table;

		myThread(int i) {
			threadID = i;
			table = new Hashtable();
			globalTable.put(new Integer(i),table);
		}

		public void init(Object p1, Object p2, int p3){
			string1 = (String)p1;
			string2 = (String)p2;
			nObjects = p3;
		}

		public void run(){
			synchronized(string1){
				Object item;
				for (int i=1; i<=nObjects; i++) {
					item = doIt(i);
				}
				yield();
				
				for (int i=1; i<=4 ; i++) {
					string1 = string1 + string2;
					string2 = string2 + string1;
				}
			}
		}

		Object doIt(int n) {
			Integer item = new Integer(n);
			return item;
		}
	} 

} 
