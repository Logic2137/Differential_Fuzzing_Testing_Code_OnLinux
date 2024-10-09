
package j9vm.test.innerclass;


public class InnerClassTest {

	private String outerString;
	private int outerInt;


	class Inner {


		void doSomething() {

			outerInt = outerInt + 1;
			outerString = outerString + "some more text";
		}
	}


	public InnerClassTest () {

		this.outerInt = 0;
		this.outerString = "";
	}


	public void run() {

		try {
			new Inner().doSomething();
			System.out.println("Everything ran fine.");
		} catch ( Exception e ) {
			System.out.println("Something blew up:");
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public static void main(String[] args) { new InnerClassTest().run(); }
}
