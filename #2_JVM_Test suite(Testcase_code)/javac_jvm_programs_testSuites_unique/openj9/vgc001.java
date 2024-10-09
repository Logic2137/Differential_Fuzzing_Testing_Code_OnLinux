
package com.ibm.jvmti.tests.verboseGC;

public class vgc001 {
	

	protected void createGarbage() 
	{
		byte[][] garbage = new byte[32][];
		for(int i = 0; i < 1024; i++) {
			garbage[i % garbage.length] = new byte[1024];
		}
	}

	public String helpVerboseGCSubscription_1simple()
	{
		return "Check verbose GC subscriber functionality. " +
		       "Added as a unit test for Jazz Design 43965 / LIR 15727";		
	}
	
	public boolean testVerboseGCSubscription_1simple()
	{
		boolean rc = true;
		int count = 0;
		
		System.out.println("Subscribing to verbose GC");
		if (!tryRegisterVerboseGCSubscriber()) {
			System.err.println("Failed to subscribe to verbose GC");
			return false;
		}

		System.out.println("Waiting for buffers to be passed to subscriber");
		while (getBufferCount() <= 0) {
			
			createGarbage();
		}
		
		if(hasAlarmed()) {
			System.out.println("Alarm function triggered unexpectedly");
			rc = false;		
		}

		System.out.println("Unsubscribing from verbose GC");
		count = tryDeregisterVerboseGCSubscriber();
		
		if (count == -1) {
			System.err.println("Failed to deregister subscriber");
			rc = false;
		}

		return rc;		
	}
	
	public String helpVerboseGCSubscription_2multiple()
	{
		return "Check verbose GC subscriber functionality with multiple subscribers. " +
		       "Added as a unit test for Jazz Design 43965 / LIR 15727";		
	}
	
	public boolean testVerboseGCSubscription_2multiple()
	{
		int savedCount = 0;
		int savedCount2 = 0;
		int count = 0;
		int count2 = 0;
		boolean rc = true;
		
		System.out.println("Subscribing to verbose GC");
		if (!tryRegisterVerboseGCSubscriber()) {
			System.err.println("Failed to subscribe to verbose GC");
			return false;
		}
		if (!tryRegisterVerboseGCSubscriber2()) {
			System.err.println("Failed second subscribe to verbose GC");
			tryDeregisterVerboseGCSubscriber();
			return false;
		}

		System.out.println("Waiting for buffers to be passed to subscribers");
		while ((getBufferCount() <= 0)&&(getBufferCount2() <= 0)) {
			
			createGarbage();
		}
		
		if(hasAlarmed() || hasAlarmed2()) {
			System.out.println("Alarm function triggered unexpectedly");
			rc = false;		
		}
		
		System.out.println("Unsubscribing first subscriber from verbose GC");
		count = tryDeregisterVerboseGCSubscriber();
		if (count == -1) {
			System.err.println("Failed to deregister subscriber");
			tryDeregisterVerboseGCSubscriber2();
			return false;
		}

		
		savedCount = getBufferCount();
		savedCount2 = getBufferCount2();
		
		System.out.println("Waiting for more buffers to be passed to subscriber");
		while ((count2 = getBufferCount2()) > 0 && (count2 <= savedCount2)) {
			
			createGarbage();
		}
		
		if(hasAlarmed2()) {
			System.out.println("Alarm function triggered unexpectedly");
			rc = false;		
		}

		
		count = getBufferCount();
		count2 = getBufferCount2();
		
		if(count != savedCount) {
			System.err.println("Deregistered subscriber is still receiving data");
			rc = false;
		}

		System.out.println("Unsubscribing second subscriber from verbose GC");
		count2 = tryDeregisterVerboseGCSubscriber2();
		if (count2 == -1) {
			System.err.println("Failed to deregister second subscriber");
			rc = false;
		}
		
		return rc;		
	}
	
	public String helpVerboseGCSubscription_3alarm()
	{
		return "Check verbose GC subscriber functionality with a failing user function. " +
		       "Added as a unit test for Jazz Design 43965 / LIR 15727";		
	}
	
	public boolean testVerboseGCSubscription_3alarm()
	{
		int count = 0;
		
		System.out.println("Subscribing to verbose GC");
		if (!tryRegisterVerboseGCSubscriber3()) {
			System.err.println("Failed to subscribe to verbose GC");
			return false;
		}

		System.out.println("Waiting for buffers to be passed to subscriber");
		while (!hasAlarmed3() && getBufferCount3() <= 5) {
			
			createGarbage();
		}

		if(hasAlarmed3()) {
			System.out.println("Alarm function triggered as expected");
			
			return true;		
		} else {
			System.out.println("Alarm function not triggered as expected");
			
			
			System.out.println("Unsubscribing from verbose GC");
			count = tryDeregisterVerboseGCSubscriber3();			
			if (count == -1) {
				System.err.println("Failed to deregister subscriber");
			}
			return false;
		}
	}

	public static native boolean tryRegisterVerboseGCSubscriber();
	public static native int tryDeregisterVerboseGCSubscriber();
	public static native int getBufferCount();
	public static native boolean hasAlarmed();
	
	public static native boolean tryRegisterVerboseGCSubscriber2();
	public static native int tryDeregisterVerboseGCSubscriber2();
	public static native int getBufferCount2();
	public static native boolean hasAlarmed2();
	
	public static native boolean tryRegisterVerboseGCSubscriber3();
	public static native int tryDeregisterVerboseGCSubscriber3();
	public static native int getBufferCount3();
	public static native boolean hasAlarmed3();
}
