
package com.ibm.jvmti.tests.retransformClasses;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class rtc003 {
	public static native boolean retransformClass(Class originalClass);

	public boolean testRedefineImplementedInterface() {
		if (!retransformClass(Map.class)) {
			System.out.println("Failed to retransform class");
			return false;
		}

		TreeMap<String, String> treeMap = new TreeMap<>();                                     
		treeMap.computeIfAbsent("TestKey", new TestFunction());

		return true;
	}

	public String helpRedefineImplementedInterface() {
		return "Tests retransforming an implemented interface class";
	}

	private class TestFunction implements Function<String, String> {
		public String apply(String t) {
			return t + ": " + TestFunction.class.getName();
		}
	}
}
