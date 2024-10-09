
package com.ibm.trace.tests.trace_subscriber;

public class TraceQueue {
	public static void main(String[] args) {
		int result = 0;
		int publishers = 0;
		int subscribers = 0;
		
		
		if (args.length == 2) {
			try {
				publishers = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.err.println("Invalid value specified for publishers, requires an integer greater than 0");
				result = -1;
			}

			try {
				subscribers = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.err.println("Invalid value specified for subscribers, requires an integer greater than 0");
				result = -1;
			}			
		}
		
		
		if (args.length != 2 || result != 0) {
			System.err.println("Usage: com.ibm.ras.tests.TraceQueue publishers subscribers");
			System.exit(-1);
		}
		
		
		if (!runQueueTests(subscribers, publishers)) {
			System.err.println("FAILED");
			result = -2;
		} else {
			System.out.println("PASSED");
			result = 0;
		}
		
		System.exit(result);
	}
	
	public static native boolean runQueueTests(int subscribers, int publishers);
}
