
package jit.test.jitt.codecache;

public class TargetClass_BT {
	public int callee(int count) {
		int res = 0 ;
		for ( int i = 0 ; i < count ; i++ ) {
			for ( int k = 0 ; k < count ; k++ ) {
				res = res + 1;
			}
		}
		return res;
	}
}
