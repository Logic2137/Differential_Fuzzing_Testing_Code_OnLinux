
import java.util.Enumeration;
import java.util.Properties;

public class SystemProperties {
	public static void main(String []args ) {
		Properties systemProperties = System.getProperties();
		for ( Enumeration i = systemProperties.keys(); i.hasMoreElements(); ) {
			String name = (String) i.nextElement();
			String value = (String) systemProperties.get(name);
			System.out.println( name + "=" + value );
		}
	}
}
