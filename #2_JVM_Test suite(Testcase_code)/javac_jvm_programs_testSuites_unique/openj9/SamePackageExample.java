
package com.ibm.j9.jsr292;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;


public class SamePackageExample {
	public static Lookup publicLookupObjectSamePackage = MethodHandles.publicLookup().in(SamePackageExample.class);
	public static int publicStaticField_doNotDeclareInSubClasses; 
	
	public int nonStaticPublicField;
	public final int nonStaticFinalPublicField;
	public static int staticPublicField;
	public final static int staticFinalPublicField = 42;
	
	private int nonStaticPrivateField;
	private static int staticPrivateField;
	
	protected int nonStaticProtectedField;
	
	public SamePackageExample() {
		super();
		nonStaticFinalPublicField = 24;
	}
	
	public SamePackageExample(int a, int b) {
		this.nonStaticPublicField = a + b;
		this.nonStaticFinalPublicField = a + b;
	}
	
	public int addPublic(int a, int b) {return a+b;}
	private int addPrivate(int a, int b) {return a+b;}
	
	public static int addPublicStatic (int a,int b) {return a+b;}
	private static int addPrivateStatic (int a,int b) {return a+b;}
	protected static int addProtectedStatic(int a, int b) {return a+b;}
	
	protected int addProtected(int a, int b) {return a+b;}
	
	public int addPublic_Super(int a, int b) {return a+b+5;}
	
	public static Lookup getLookup() {
		return MethodHandles.lookup();
	}
	
	public class SamePackageInnerClass{
		
		public int nonStaticPublicField_Inner1;
		
		private int nonStaticPrivateField_Inner1;
		
		protected int nonStaticProtectedField_Inner1; 
		
		public int addPublicInner(int a, int b) { return a+b; }
		
		private int addPrivateInner(int a, int b) { return a+b; }
		
		public Lookup getLookup() {
			return MethodHandles.lookup();
		}
		
		public class SamePackageInnerClass_Nested_Level2 {
			
			public int nonStaticPublicField_Inner2;
			
			private int nonStaticPrivateField_Inner2;
			
			protected int nonStaticProtectedField_Inner2; 
			
			public int addPublicInner_Level2(int a, int b) { return a+b; }
			
			public Lookup getLookup() {
				return MethodHandles.lookup();
			}
		}
	}
	
	public class SamePackageInnerClass2{
		
		public int nonStaticPublicField_Inner12;
		public Lookup getLookup() {
			return MethodHandles.lookup();
		}
		
		public class SamePackageInnerClass2_Nested_Level2 {
			
			public int nonStaticPublicField_Inner22;
			
			private int nonStaticPrivateField_Inner22;
			
			protected int nonStaticProtectedField_Inner22; 
			
			public Lookup getLookup() {
				return MethodHandles.lookup();
			}
		}
		
		public class SamePackageInnerClass2_Nested_Level2_SubOf_Inner1 extends SamePackageInnerClass {
			public int addPublicInner(int a, int b) { return a+b+20; } 
		}
	}
	
	protected class SamePackageInnerClass_Protected {
		
		protected Lookup getLookup() {
			return MethodHandles.lookup();
		}
		
		public int addPublicInner(int a, int b) { return a+b; }
		protected int addProtectedInner(int a, int b) { return a+b; }
		
		protected class SamePackageInnerClass_Nested_Level2 {
			public Lookup getLookup() {
				return MethodHandles.lookup();
			}
			public int addPublicInner_Level2(int a, int b) { return a+b; }
			protected int addProtectedInner_Level2(int a, int b) { return a+b; }
		}
	}
	
	static class SamePackageInnerClass_Static {
		public static Lookup getLookup () {
			return MethodHandles.lookup();
		}
	}

	
	public String arrayToString(String[] o) {
		String s = "[";
		
		if ( o == null || o.length == 0 ) {
			return s + "]";
		}
		
		for ( int i = 0 ; i < o.length ; i++ ) {
			s += o[i];
			if ( i + 1 < o.length ) {
				s += ",";
			}
		}
		
		return s + "]";
	}
	
	public int getLength(String[] o) {
		return o.length;
	}
	
	public int getLength(int[] o) {
		return o.length;
	}
	
	public int getLength(double[] o) {
		return o.length;
	}
	
	public int getLength(char[] o) {
		return o.length;
	}
	
	public int getLength(float[] o) {
		return o.length;
	}
	
	public int getLength(boolean[] o) {
		return o.length;
	}
	
	public int getLength(byte[] o) {
		return o.length;
	}
	
	public int getLength(short[] o) {
		return o.length;
	}
	
	public int getLength(long[] o) {
		return o.length;
	}
	
	public static int getLengthStatic(String[] o) {
		return o.length;
	}
	
	public int addPublicVariableArity(int... n) {
		int sum = 0 ; 
		for ( int i = 0 ; i < n.length ; i++ ) {
			sum += n[i];
		}
		return sum;
	}
	
	public int addPublicVariableArity(Object... n) {
		int sum = 0 ; 
		for ( int i = 0 ; i < n.length ; i++ ) {
			sum += (int)n[i];
		}
		return sum;
	}
	
	public void takeVariableArityObject(Object... n) {}
		
	public static String returnOne() {
		return "1";
	}

	public static String returnTwo() {
		return "2";
	}
	
	public static String returnThree() {
		return "3";
	}
	
	public int[] makeArray(int...args) { return args; }
	
	public String arrayToString(Object [] o) {
		String s = "[";
		
		if ( o == null || o.length == 0 ) {
			return s + "]";
		}
		
		for ( int i = 0 ; i < o.length ; i++ ) {
			s += o[i];
			if ( i + 1 < o.length ) {
				s += ",";
			}
		}
		
		return s + "]";
	}
	
	public String toOjectArrayString(Object objArray) {
		Object [] o = (Object[])objArray;
		String s = "[";
		
		if ( o == null || o.length == 0 ) {
			return s + "]";
		}
		
		for ( int i = 0 ; i < o.length ; i++ ) {
			s += o[i];
			if ( i + 1 < o.length ) {
				s += ",";
			}
		}
		
		return s + "]";
	}
	
	
	public SamePackageExample(int...n) {
		nonStaticFinalPublicField = 24;
	}
	
	public boolean isReceiverNull() { return this == null; }
	public String toString() { return "SamePackageExample.toString()" + ((this == null) ? " null" : "notnull"); }
}
