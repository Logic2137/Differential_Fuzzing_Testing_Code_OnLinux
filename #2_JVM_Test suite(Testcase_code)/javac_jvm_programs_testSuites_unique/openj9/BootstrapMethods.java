

package com.ibm.j9.recreateclass.testclasses;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;


public class BootstrapMethods {
	
	public static String concatStrings(String s1, String s2) {
		return s1 + s2;
	}
	
	public static int addIntegers(int i1, int i2) {
		return i1 + i2;
	}
	
	public static CallSite bootstrapConcatStrings(Lookup caller, String name,
			MethodType type) throws Throwable {
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		Class<?> thisClass = lookup.lookupClass();
		MethodHandle concatStrings = lookup.findStatic(thisClass,
				"concatStrings", type);
		return new ConstantCallSite(concatStrings);
	}
	
	public static CallSite bootstrapAddInts(Lookup caller, String name,
			MethodType type) throws Throwable {
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		Class<?> thisClass = lookup.lookupClass();
		MethodHandle addIntegers = lookup.findStatic(thisClass,
				"addIntegers", type);
		return new ConstantCallSite(addIntegers);
	}
	
	public static CallSite bootstrapGetStringConstant(Lookup caller, String name, MethodType type, String s1) throws Throwable {
		return new ConstantCallSite(MethodHandles.constant(String.class, s1));
	}
}
