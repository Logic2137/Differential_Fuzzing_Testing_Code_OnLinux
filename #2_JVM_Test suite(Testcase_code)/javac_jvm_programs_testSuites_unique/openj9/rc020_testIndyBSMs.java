

package com.ibm.jvmti.tests.redefineClasses;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

public class rc020_testIndyBSMs {
	private static int bsmCount = 0;

	public static CallSite bootstrapMethod1(MethodHandles.Lookup lUnused, String nUnused, MethodType tUnused) throws IllegalAccessException, NoSuchMethodException {
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodType type = MethodType.methodType(void.class);
		MethodHandle handle = lookup.findStatic(lookup.lookupClass(), "testMethod", type);

		
		bsmCount++;
		
		return new ConstantCallSite(handle);
	}

	public static CallSite bootstrapMethod2(MethodHandles.Lookup lUnused, String nUnused, MethodType tUnused) throws IllegalAccessException, NoSuchMethodException {
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodType type = MethodType.methodType(void.class);
		MethodHandle handle = lookup.findStatic(lookup.lookupClass(), "testMethod", type);

		
		bsmCount++;
		
		return new ConstantCallSite(handle);
	}

	public static CallSite bootstrapMethod3(MethodHandles.Lookup lUnused, String nUnused, MethodType tUnused, int iUnused) throws IllegalAccessException, NoSuchMethodException {
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodType type = MethodType.methodType(void.class);
		MethodHandle handle = lookup.findStatic(lookup.lookupClass(), "testMethod", type);

		
		bsmCount++;
		
		return new ConstantCallSite(handle);
	}

	public static CallSite bootstrapMethod4(MethodHandles.Lookup lUnused, String nUnused, MethodType tUnused, String sUnused, int iUnused) throws IllegalAccessException, NoSuchMethodException {
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodType type = MethodType.methodType(void.class);
		MethodHandle handle = lookup.findStatic(lookup.lookupClass(), "testMethod", type);

		
		bsmCount++;
		
		return new ConstantCallSite(handle);
	}

	public static CallSite bootstrapMethod5(MethodHandles.Lookup lUnused, String nUnused, MethodType tUnused, String sUnused, double dUnused) throws IllegalAccessException, NoSuchMethodException {
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodType type = MethodType.methodType(void.class);
		MethodHandle handle = lookup.findStatic(lookup.lookupClass(), "testMethod", type);

		
		bsmCount++;
		
		return new ConstantCallSite(handle);
	}

	public static CallSite bootstrapMethod5(MethodHandles.Lookup lUnused, String nUnused, MethodType tUnused, int iUnused, double dUnused, String sUnused) throws IllegalAccessException, NoSuchMethodException {
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodType type = MethodType.methodType(void.class);
		MethodHandle handle = lookup.findStatic(lookup.lookupClass(), "testMethod", type);

		
		bsmCount++;
		
		return new ConstantCallSite(handle);
	}

	static void testMethod() {
		
	}

	
	public static int bsmCallCount() {
		return bsmCount;
	}

	
	public static void resetBsmCallCount() {
		bsmCount = 0;
	}
}
