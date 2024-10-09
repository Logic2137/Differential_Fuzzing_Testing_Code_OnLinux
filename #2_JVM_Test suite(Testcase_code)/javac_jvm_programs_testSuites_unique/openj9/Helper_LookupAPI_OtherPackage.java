
package com.ibm.j9.jsr292.helpers;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

public class Helper_LookupAPI_OtherPackage {
	public static Lookup lookupObject = MethodHandles.lookup();
	public static Lookup difflookupObject_SamePackage = Helper_AnotherMethodHandles.lookupObject;
	public static Class<?> privateNestedClass = PrivateNestedClass.class;
	public static Class<?> privateDoubleNestedClass = PrivateNestedClass.PrivateDoubleNestedClass.class;
	public static Class<?> protectedNestedClass = ProtectedNestedClass.class;
	public static Class<?> class_PackageAccess = Helper_Class_PackageAccess.class;

	public static boolean initializedPublicNestedClass;
	public static boolean initializedPrivateNestedClass;
	public static boolean initializedPrivateDoubleNestedClass;
	public static boolean initializedProtectedNestedClass;
	public static boolean initializedClassPackageAccess;

	public static class PublicNestedClass {
        static {
        	initializedPublicNestedClass = true;
        }
    }

	private static class PrivateNestedClass {
        static {
        	initializedPrivateNestedClass = true;
        }

    	private static class PrivateDoubleNestedClass {
            static {
            	initializedPrivateDoubleNestedClass = true;
            }
        }
    }

	protected static class ProtectedNestedClass {
        static {
        	initializedProtectedNestedClass = true;
        }
    }

}

class Helper_AnotherMethodHandles  {
	public static MethodHandles.Lookup lookupObject = MethodHandles.lookup();
}

class Helper_Class_PackageAccess {
    static {
    	Helper_LookupAPI_OtherPackage.initializedClassPackageAccess = true;
    }
}
