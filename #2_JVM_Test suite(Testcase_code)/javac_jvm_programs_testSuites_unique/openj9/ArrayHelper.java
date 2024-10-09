
package org.openj9.test.varhandle;

public class ArrayHelper {
	static byte[] reset(byte[] array) {
		if (null == array) {
			array = new byte[3];
		}
		array[0] = 1;
		array[1] = 2;
		array[2] = 3;
		return array;
	}
	
	static char[] reset(char[] array) {
		if (null == array) {
			array = new char[3];
		}
		array[0] = '1';
		array[1] = '2';
		array[2] = '3';
		return array;
	}
	
	static double[] reset(double[] array) {
		if (null == array) {
			array = new double[3];
		}
		array[0] = 1.1;
		array[1] = 2.2;
		array[2] = 3.3;
		return array;
	}
	
	static float[] reset(float[] array) {
		if (null == array) {
			array = new float[3];
		}
		array[0] = 1.1f;
		array[1] = 2.2f;
		array[2] = 3.3f;
		return array;
	}
	
	static int[] reset(int[] array) {
		if (null == array) {
			array = new int[3];
		}
		array[0] = 1;
		array[1] = 2;
		array[2] = 3;
		return array;
	}
	
	static long[] reset(long[] array) {
		if (null == array) {
			array = new long[3];
		}
		array[0] = 1;
		array[1] = 2;
		array[2] = 3;
		return array;
	}
	
	static String[] reset(String[] array) {
		if (null == array) {
			array = new String[3];
		}
		array[0] = "1";
		array[1] = "2";
		array[2] = "3";
		return array;
	}
	
	static Class<?>[] reset(Class<?>[] array) {
		if (null == array) {
			array = new Class<?>[3];
		}
		array[0] = Object.class;
		array[1] = String.class;
		array[2] = ClassLoader.class;
		return array;
	}
	
	static short[] reset(short[] array) {
		if (null == array) {
			array = new short[3];
		}
		array[0] = 1;
		array[1] = 2;
		array[2] = 3;
		return array;
	}
	
	static boolean[] reset(boolean[] array) {
		if (null == array) {
			array = new boolean[4];
		}
		array[0] = true;
		array[1] = false;
		array[2] = true;
		array[3] = false;
		return array;
	}
}
