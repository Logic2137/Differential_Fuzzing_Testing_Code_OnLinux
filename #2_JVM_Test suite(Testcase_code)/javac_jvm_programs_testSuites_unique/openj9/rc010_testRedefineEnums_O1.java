
package com.ibm.jvmti.tests.redefineClasses;

public enum rc010_testRedefineEnums_O1 {
	TYPE_A,
	TYPE_B,
	TYPE_C;
	
	public static boolean isVowelType(rc010_testRedefineEnums_O1 t) {
		return t.equals(TYPE_A);
	}
}
