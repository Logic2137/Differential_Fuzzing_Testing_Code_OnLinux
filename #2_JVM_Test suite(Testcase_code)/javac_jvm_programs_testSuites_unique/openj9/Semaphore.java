
package com.ibm.jvmti.tests.forceEarlyReturn;

public class Semaphore 
{
	private int value;

	public Semaphore(int value) 
	{ 
		this.value = value; 
	}

	public synchronized void down() 
	{
		--value;
		if (value < 0) {
			try { 
				wait(); 
			} catch( Exception e ) {

			}
		}
	}

	public synchronized void up() 
	{
		++value;
		notify();
	}
}
