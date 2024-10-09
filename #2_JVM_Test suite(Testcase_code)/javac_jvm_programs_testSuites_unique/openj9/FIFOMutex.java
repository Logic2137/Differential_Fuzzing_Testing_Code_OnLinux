
package com.ibm.dump.tests.javacore_thread;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;


class FIFOMutex {
	   private final AtomicBoolean locked = new AtomicBoolean(false);
	   private final Queue<Thread> waiters
	     = new ConcurrentLinkedQueue<Thread>();
	   

	   public void lock() {
		 long i = 0;
	     boolean wasInterrupted = false;
	     Thread current = Thread.currentThread();
	     waiters.add(current);

	     
	     while (waiters.peek() != current ||
	            !locked.compareAndSet(false, true)) {
	        LockSupport.park(this);
	    	i++;
	        if (Thread.interrupted()) {
	          wasInterrupted = true;
	        }
	     }

	     waiters.remove();
	     if (wasInterrupted) {         
	        current.interrupt();
	     }  
	   }

	   public void unlock() {
	     locked.set(false);
	     LockSupport.unpark(waiters.peek());
	   }
	 }
