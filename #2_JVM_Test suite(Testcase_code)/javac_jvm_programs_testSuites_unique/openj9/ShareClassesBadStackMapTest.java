
public class ShareClassesBadStackMapTest {
	public static void main(String[] paramArrayOfString) {
	  foo(Integer.valueOf(0));
	}
	
	public static boolean foo(Object paramObject) {
	  System.out.println("foo");
	  return true;
	}
  }
