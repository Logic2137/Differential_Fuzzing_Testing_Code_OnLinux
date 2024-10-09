
package j9vm.regression;



public class ClassLoadDuringException {
	public static void main(String args[]) throws Throwable {
		a();
	}

	public static void a() {
		jittedB();
	}

	public static void jittedB() {
		try {
			c();
		} catch(NullPointerException e) {
			System.out.println("caught npe");
		}
	}

	public static void c() {
		jittedD();
	}
	
	public static void jittedD() {
		try {
			e();
		} catch(NotLoadedYet e) {
		}
	}

	public static void e() {
		throw new NullPointerException();
	}
}

class NotLoadedYet extends RuntimeException {
}
