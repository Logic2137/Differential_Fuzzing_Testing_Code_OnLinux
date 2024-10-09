

package com.ibm.j9.recreateclass.testclasses;


public class StackMapTableWithByteBooleanArrayTest {
	public void stackMapTableTest(String args[]) {
		if (args.length > 0) {
			byte mat1[] = new byte[] {1, 2, 3};
			byte mat2[] = new byte[] {4, 5, 6};
			byte mat3[] = new byte[mat1.length];
			for (int i = 0; i < mat3.length; i++) {
				mat3[i] = (byte)(mat1[i] + mat2[i]);
			}
		} else { 
			boolean mat1[] = new boolean[] {true, false};
			boolean mat2[] = new boolean[] {false, true};
			boolean mat3[] = new boolean[mat1.length];
			for (int i = 0; i < mat3.length; i++) {
				mat3[i] = mat1[i] | mat2[i];
			}
		}
	}
}
