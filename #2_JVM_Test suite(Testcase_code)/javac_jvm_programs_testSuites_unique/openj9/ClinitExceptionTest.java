
package j9vm.test.clinitexception;


public class ClinitExceptionTest {

	public void run() throws RuntimeException {
		
		boolean passed = false;
		String className = "j9vm.test.clinitexception.ClassWithClinitException";
		try {
			Class clazz = Class.forName(className);
		}
		catch(ClassNotFoundException e) { 
			System.out.println("Class " + className + " not found");
		}
		catch (ExceptionInInitializerError e)     {
			System.out.println("passed");
			passed = true;
		}
		if( !passed ) {
			throw new RuntimeException();
		}
			
			
	}

public static void main(String[] args) { new ClinitExceptionTest().run(); }
}
