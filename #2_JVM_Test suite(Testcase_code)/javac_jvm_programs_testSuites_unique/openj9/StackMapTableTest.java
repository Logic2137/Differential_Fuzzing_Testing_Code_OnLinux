

package com.ibm.j9.recreateclass.testclasses;


public class StackMapTableTest {
	public void stackMapTableTest(String args[]) {
		if (args.length == 1) {
			if (args[0].charAt(0) == 'B') {
				byte mat1[] = new byte[] {1, 2, 3};
				byte mat2[] = new byte[] {4, 5, 6};
				byte mat3[] = new byte[mat1.length];
				for (int i = 0; i < mat3.length; i++) {
					mat3[i] = (byte)(mat1[i] + mat2[i]);
				}
			} else if (args[0].charAt(0) == 'I') {
				int mat1[] = new int[] {1, 2, 3};
				int mat2[] = new int[] {4, 5, 6};
				int mat3[] = new int[mat1.length];
				for (int i = 0; i < mat3.length; i++) {
					mat3[i] = mat1[i] + mat2[i];
				}
			} else if (args[0].charAt(1) == 'J') {
				long mat1[] = new long[] {1, 2, 3};
				long mat2[] = new long[] {4, 5, 6};
				long mat3[] = new long[mat1.length];
				for (int i = 0; i < mat3.length; i++) {
					mat3[i] = mat1[i] + mat2[i];
				}
			}
		}
	}
}
