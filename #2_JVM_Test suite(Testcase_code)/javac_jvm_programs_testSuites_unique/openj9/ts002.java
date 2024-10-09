
package com.ibm.jvmti.tests.traceSubscription;

public class ts002 {
	

	public String helpTracePointSubscription()
	{
		return "Check tracepoint subscriber functionality. " +
		       "Added as a unit test for J9 VM design Jazz103 #40425";
	}

	public boolean testTracePointSubscription()
	{
		int count = 0;

		System.out.println("Subscribing to selected -Xtrace tracepoints");
		if (!tryRegisterTracePointSubscribers()) {
			System.err.println("Failed to subscribe to tracepoints");
			return false;
		}

		System.out.println("Generating tracepoints to be passed to the subscribers");
		while (getTracePointCount1() < 50 || getTracePointCount2() < 50) {
			try {
				
				throw new Exception("generate trace data");
			} catch (Exception e) {
			}
		}

		
		System.out.println("Checking that subscriber alarms have been called");
		while (hasAlarmTriggered1() == false || hasAlarmTriggered2() == false) {
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				
			}
		}

		System.out.println("Unsubscribing from -Xtrace tracepoints");
		if (!tryDeregisterTracePointSubscribers()) {
			System.err.println("Failed to unsubscribe to tracepoints");
			return false;
		}

		return true;
	}

	public static native boolean tryRegisterTracePointSubscribers();
	public static native boolean tryDeregisterTracePointSubscribers();
	public static native int getTracePointCount1();
	public static native int getTracePointCount2();
	public static native boolean hasAlarmTriggered1();
	public static native boolean hasAlarmTriggered2();
}
