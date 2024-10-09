
package com.ibm.jvmti.tests.samplingObjectAllocation;

public class soae001 {
	private final static int DEFAULT_SAMPLING_RATE = 512 * 1024; 
	
	private native static void reset();	
	private native static int enable(Thread thread);	
	private native static int disable();	
	private native static int check();	
	
	public boolean testDefaultInterval() {
		boolean result = false;
		int jvmtiResult = 0;

		reset();
		jvmtiResult = enable(null);
		if (0 != jvmtiResult) {
			System.out.println("com.ibm.jvmti.tests.samplingObjectAllocation.soae001.enable() failed with: " + jvmtiResult);
		} else {
			byte[] bytes;
			bytes = new byte[DEFAULT_SAMPLING_RATE];
			System.out.println("Allocated a byte array with size " + bytes.length);
			
			int samplingResult = check();
			if (samplingResult < 1) {
				System.out.println("com.ibm.jvmti.tests.samplingObjectAllocation.soae001.check() failed, expected 1+ but got: " + samplingResult);
			} else {
				jvmtiResult = enable(Thread.currentThread());
				if (0 != jvmtiResult) {
					System.out.println("com.ibm.jvmti.tests.samplingObjectAllocation.soae001.enable(thread) failed as expected with: " + jvmtiResult);
					jvmtiResult = disable();
					if (0 != jvmtiResult) {
						System.out.println("com.ibm.jvmti.tests.samplingObjectAllocation.soae001.disable() failed with: " + jvmtiResult);
					} else {
						result = true;
					}
				}
			}
		}
		
		return result;
	}
	public String helpDefaultInterval() {
		return "Test that with default sampling interval the event callback is invoked once as expected after enabled, and not invoked if the event is disable.";
	}
}
