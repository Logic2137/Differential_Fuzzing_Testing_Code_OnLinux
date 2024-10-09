
package com.ibm.jvmti.tests.util;

public class SyncThread extends Thread 
{
    protected Object lock = new Object();
    protected volatile boolean notified = false;

    public SyncThread() 
    {
        super();
    }
    
    public SyncThread(ThreadGroup group, String name) 
    {
        super(group, name);
    }
    
    
    public void syncStart() 
    {

    	synchronized(lock) 
    	{
            super.start();
            try {
            	while(!notified) {
            		lock.wait();
            	}
            } catch (java.lang.InterruptedException ie) {
            	interrupt();
            }
    	}
    }
    
    public void run() 
    {
        synchronized(lock) {
        	notified = true;
            lock.notify();
        }
        while(!isInterrupted() && work()) {
        	Thread.yield();
        }
    }
        
    protected boolean work() 
    {
        Thread.yield();
        return true;
    }
}
