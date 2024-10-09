
package j9vm.test.fastclasshashtable;

public class FastClassHashTableTest {

	
	public static void main(String[] args) {
		ClassLoader loader = new ClassLoader() {};
		
		try {
			
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			
			e1.printStackTrace();
		}
		
		try {
			
			loader.loadClass("j9vm.test.fastclasshashtable.Dummy1");
			loader.loadClass("j9vm.test.fastclasshashtable.Dummy2");
			loader.loadClass("j9vm.test.fastclasshashtable.Dummy3");
			loader.loadClass("j9vm.test.fastclasshashtable.Dummy4");
			loader.loadClass("j9vm.test.fastclasshashtable.Dummy5");
			loader.loadClass("j9vm.test.fastclasshashtable.Dummy6");
			loader.loadClass("j9vm.test.fastclasshashtable.Dummy7");
			loader.loadClass("j9vm.test.fastclasshashtable.Dummy8");
			loader.loadClass("j9vm.test.fastclasshashtable.Dummy9");
			loader.loadClass("j9vm.test.fastclasshashtable.Dummy10");
			loader.loadClass("j9vm.test.fastclasshashtable.Dummy11");
			loader.loadClass("j9vm.test.fastclasshashtable.Dummy12");
			loader.loadClass("j9vm.test.fastclasshashtable.Dummy13");
			loader.loadClass("j9vm.test.fastclasshashtable.Dummy14");
			System.gc();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
	}

}
