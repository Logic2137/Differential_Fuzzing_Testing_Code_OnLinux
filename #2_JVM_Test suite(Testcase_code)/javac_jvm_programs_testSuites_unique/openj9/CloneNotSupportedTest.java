
package j9vm.test.clone;


public class CloneNotSupportedTest {

	
  	static class T2 extends Object
	{
		Object cl() throws CloneNotSupportedException{
			return super.clone();
		}
	}

	
	T2 t2 = new  T2();


	public void run() {
		try {
			t2.cl();
			System.out.println("did not throw an exception");
			throw new RuntimeException();
		} catch(CloneNotSupportedException e) {
			System.out.println("caught CloneNotSupportedException");
		}catch(Exception e) {
			System.out.println("caught something unexpected:");
			e.printStackTrace();
			throw new RuntimeException();
		}

	}
	
	public static void main(String[] args) { new CloneNotSupportedTest().run(); }
}
