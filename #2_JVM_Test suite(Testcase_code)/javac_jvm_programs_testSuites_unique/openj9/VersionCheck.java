
package org.openj9.test.util;

import java.lang.reflect.*;


public class VersionCheck {
	
	static final Object versionInstance;
	static final Method majorMethod;
	
	static {
		Object versionInstanceTemp = null;
		Method majorMethodTemp = null;
		try { 
			Method versionMethod = Runtime.class.getDeclaredMethod("version");
			versionInstanceTemp = versionMethod.invoke(null);
			majorMethodTemp = versionInstanceTemp.getClass().getDeclaredMethod("major");
		} catch(NoSuchMethodException e) {
			
		} catch(SecurityException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		versionInstance = versionInstanceTemp;
		majorMethod = majorMethodTemp;
	}
	
	
	
	public static int major() {
		if (versionInstance != null) {
			try {
				return ((Integer)majorMethod.invoke(versionInstance)).intValue();
			} catch(IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
		return 8;
	}

	 
	public static int classFile() {
		return major() + 44;
	}
}
