
package com.ibm.jvmti.tests.getOrSetLocal;

import java.util.concurrent.Semaphore;

public class gosl001 
{
	final Semaphore main = new Semaphore(0);
	final Semaphore thrd = new Semaphore(0);
	
	native static boolean getInt(Thread thread, int stackDepth, Class clazz, String methodName, String methodSignature, String varName, int expectedInt);
	
	public boolean testGetSingleIntLocal() 
	{
		IntContainer thread = new IntContainer();
		
		thread.start();

		try {
		
			
			
			thrd.acquire();    
			
			
			main.release();  
			System.out.println("main: MADE SURE method1 is compiled"); 
			
			
			
			
			thrd.acquire();    
			
			getInt(thread, 1, thread.getClass(), "method1", "()I", "localInt", 1234);
			
			main.release();  
			
			thread.join();
			

		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
	    
	    return true;
	}
	
	public String helpGetSingleIntLocal()
	{
		return "retrieve a single int local from a frame containing just that";		
	}
	
	
	private class IntContainer extends Thread
	{
		public void run() 
		{ 
			try {
				method1();
				method1();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("method1: run done"); 
		}
		
		synchronized private int method1() throws InterruptedException 
		{
			int localInt = 1234;
			int int2 = 0x22222222;
			int int3 = 0x33333333;
			int int4 = 0x44444444;
			int int5 = 0x55555555;
					
					
			
			thrd.release();  
			
			main.acquire();  
			
			
			return localInt;
		}
		
	}

	
	
}
