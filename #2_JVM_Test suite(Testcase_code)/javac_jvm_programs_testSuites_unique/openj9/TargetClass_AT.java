
package jit.test.jitt.codecache;

import java.util.ArrayList;
import java.util.Random;

public class TargetClass_AT {
	ArrayList<Integer> evenList = new ArrayList<Integer>();
	ArrayList<Integer> oddList = new ArrayList<Integer>();
	Random r = new Random();

	public int callee(int count) {
		if ( count == 1 ) {
			return 1;
		}
		else {
			int res = 0 ;
			for ( int i = 0 ; i < count ; i++ ) {
				for ( int j = 0 ; j < count ; j++ ) {
					for ( int k = 0 ; k < count ; k++ ) {
						res = res + 1;

						int n = r.nextInt();

						if ( n % 2 == 0 ) {
							evenList.add(n);
						} else {
							oddList.add(n);
						}

						if ( n < 200 ) {
							if ( (n - n*k) < (n - n*i) ) {
								for ( int z = 0 ; z < n ; z++ ) {
									
									new Helper().doWork(z);
								}
							}
						}
					}
				}
			}
			return res;
		}
	}
	private static class Helper{
		public int doWork(int n) {
			 if ( n > 0 ) return n;
			 else return -1;
		}
	}
}
