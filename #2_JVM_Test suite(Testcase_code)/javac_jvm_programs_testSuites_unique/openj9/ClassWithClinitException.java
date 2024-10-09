
package j9vm.test.clinitexception;


public class ClassWithClinitException {
	static	{	f();	}
	
	static  int f() {
		if (! false) {
			RuntimeException ex = new RuntimeException();
			
			System.out.println("before throw");
			throw ex;
		}
		return 10;
	}

}
