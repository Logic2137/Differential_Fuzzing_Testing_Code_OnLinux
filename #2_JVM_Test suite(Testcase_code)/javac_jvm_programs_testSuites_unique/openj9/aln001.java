
package com.ibm.jvmti.tests.agentLibraryNatives;

public class aln001 {
	private static native void shortname();
	private static native void longname(int i);

	public String helpAgentLibraryNatives() {
		return "Check that JNI natives (both short and long names) can be found in agent libraries" ;
	}

	public boolean testAgentLibraryNatives() {
		boolean result = true;
		try {
			shortname();
		} catch (UnsatisfiedLinkError e) {
			result = false;
			e.printStackTrace();
		}
		try {
			longname(0);
		} catch (UnsatisfiedLinkError e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
}
