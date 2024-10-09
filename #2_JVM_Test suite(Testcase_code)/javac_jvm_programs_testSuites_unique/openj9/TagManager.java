
package com.ibm.jvmti.tests.followReferences;

public class TagManager 
{
	public static native boolean setTag(Object obj, long tag, int flags);
	public static native boolean checkTags();
	public static native boolean clearTags(int flags);
	public static native boolean isTagQueued(long tag);
	
	public static boolean setClassTag(Class klass, long tag)
	{
		setTag((Object) klass, tag, 0);
		
		return true;
	}
	
	public static boolean setObjectTag(Object object, long tag)
	{
		setTag(object, tag, 0);
		
		return true;
	}
	
	public static boolean clearTags()
	{
		clearTags(0);
		
		return true;
	}
	
}
