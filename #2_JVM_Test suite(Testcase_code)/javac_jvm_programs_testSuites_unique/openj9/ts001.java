
package com.ibm.jvmti.tests.traceSubscription;

public class ts001 {
	

	private static final byte eyecatcherASCII[] = new byte[] { 'U', 'T', 'T', 'H' };
	private static final byte eyecatcherEBCDIC[] = new byte[] { (byte)228, (byte)227, (byte)227, (byte)200 };	
	
	public String helpTraceSubscription()
	{
		return "Check trace subscriber functionality. " +
		       "Added as a unit test for J9 VM design ID 1944";		
	}

	public boolean testTraceSubscription()
	{
		int count = 0;
		
		System.out.println("Subscribing to trace");
		if (!tryRegisterTraceSubscriber()) {
			System.err.println("Failed to subscribe to trace");
			return false;
		}

		System.out.println("Flushing trace data");
		if (!tryFlushTraceData()) {
			System.err.println("Failed to flush trace data");
			return false;
		}
		

		System.out.println("waiting for buffers to be passed to subscriber");
		while (getBufferCount() <= 0) {
			try {
				
				throw new Exception("generate trace data");
			} catch (Exception e) {
			}
		}

		System.out.println("Unsubscribing from trace");
		count = tryDeregisterTraceSubscriber();
		
		if (count == -1) {
			System.err.println("Failed to deregister subscriber");
			return false;
		}

		
		System.out.println("Flushing trace data (will fail if not outputing trace to a file)");
		try {
			tryFlushTraceData();
		} catch (Exception e) {
			
		}

		
		System.out.println("waiting for alarm to complete");
		while (getFinalBufferCount() == 0) {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				
			}
		}

		
		System.out.println("Checking that buffer count hasn't increased after unsubscribe completed");
		if (getBufferCount() != getFinalBufferCount()) {
			System.err.println("Buffer count increased after subscriber deregistered");
			return false;
		}

		
		
		byte eyecatcher[] = new byte[4];
		for (int i = 0; i < eyecatcher.length; i++) {
			
			byte b = tryGetTraceMetadata(i);
			eyecatcher[i] =  b;
			if (b != eyecatcherASCII[i] && b != eyecatcherEBCDIC[i]) {
				System.err.println("eyecatcher byte in trace metadata at index "+i+" doesn't match expectation. Found: '"+(char)b+"'");
				return false;
			}
		}
		
		System.out.println("Trace metadata validated");
		return true;		
	}

	public static native boolean tryRegisterTraceSubscriber();
	public static native boolean tryFlushTraceData();
	public static native int tryDeregisterTraceSubscriber();
	public static native int getBufferCount();
	public static native int getFinalBufferCount();
	public static native byte tryGetTraceMetadata(int index);
}
