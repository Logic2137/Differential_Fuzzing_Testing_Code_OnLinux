
package com.ibm.jvmti.tests.getSystemProperty;

import java.util.Arrays;

public class gsp001 {

	private static native String getSystemProperty(String propName);
	private static native void cleanup();
	
	
	private static String[] sysPropNames = {
		"java.vm.vendor",
		"java.vm.version",
		"java.vm.name",
		"java.vm.info",
		"java.library.path",
		"java.class.path",
		"java.vm.specification.version"
	};

	
	public boolean testGetSystemPropertyAgentOnLoad() {
		return Arrays.stream(sysPropNames).filter(s -> getSystemProperty(s) == null).count() == 0;
	}

	public String helpGetSystemPropertyAgentOnLoad() {
		return "Test that JVMTI GetSystemProperty can retrieve certain system properties at early phase Agent_OnLoad().";
	}
	
	public boolean teardown() {
		cleanup();
		return true;
	}
}
