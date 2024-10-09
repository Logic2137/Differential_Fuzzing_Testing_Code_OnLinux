
package org.openj9.test.varhandle;

public class StaticHelper {
	static final int finalI = 1;
	
	static byte b;
	static char c;
	static double d;
	static float f;
	static int i;
	static long j;
	static String l1;
	static Class<?> l2;
	static short s;
	static boolean z;
	
	static void reset() {
		b = 1;
		c = '1';
		d = 1.0;
		f = 1.0f;
		i = 1;
		j = 1L;
		l1 = "1";
		l2 = String.class;
		s = 1;
		z = true;
	}
	
	static final class StaticNoInitializationHelper {
		static final int finalI = 1;
	}
}
