
package examples;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;


public class PackageExamples {
	
	public static Lookup getLookup() {
		return MethodHandles.lookup();
	}
	
	public static Lookup getPublicLookup() {
		return MethodHandles.publicLookup();
	}
	
	final protected String finalProtectedMethod() { return "finalProtectedMethod"; }

	protected static String protectedMethod () { return "protectedMethod"; }
	
	 static String defaultMethod() { return "defaultMethod"; }
	
	public int nonStaticPublicField;
	public static int staticPublicField;
	
	private int nonStaticPrivateField;
	private static int staticPrivateField;
	
	protected int nonStaticProtectedField;
	
	public int addPublic(int a, int b){return a+b;}
	private int addPrivate(int a, int b){return a+b;}
	
	public static int addPublicStatic (int a,int b) {return a+b;}
	private static int addPrivateStatic (int a,int b) {return a+b;}
	
	protected static int addProtectedStatic(int a,int b) {return a+b;}
	
	public PackageExamples(){
		super();
	}
	
	public PackageExamples(int a, int b){
		this.nonStaticPublicField = a + b;
	}
	

	public class CrossPackageInnerClass{
		public int addPublicInner(int a, int b){ return a+b; }
		public Lookup getLookup() { return MethodHandles.lookup(); }
		
		public class CrossPackageInnerClass2_Nested_Level2 {
			public Lookup getLookup() {
				return MethodHandles.lookup();
			}
			public int addPublicInner_level2(int a, int b){ return a+b; }
		}
	}
	
	public int getLength(String[] o){
		return o.length;
	}
	
	public static int getLengthStatic(String[] o){
		return o.length;
	}
	
	public int addPublicVariableArity(int... n){
		int sum = 0 ; 
		for ( int i = 0 ; i < n.length ; i++ ){
			sum += n[i];
		}
		return sum;
	}
	
	public static int addPublicStaticVariableArity(int... n){
		int sum = 0 ; 
		for ( int i = 0 ; i < n.length ; i++ ){
			sum += n[i];
		}
		return sum;
	}
	
	protected int addProtected(int a, int b) {return a+b;}
}
