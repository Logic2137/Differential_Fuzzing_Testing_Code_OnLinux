
package APITests;

import java.lang.reflect.Method;

public class FilterTesterUtils {
	
	protected static Object invokeMethod(Object obj, String name,	Object[] parameters) {

		Class[] types = new Class[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			types[i] = parameters[i].getClass();
		}
		Class c = null;
		if (obj instanceof Class)
			c = (Class) obj;
		else
			c = obj.getClass();
		while (c != null) {
			try {
				Method m = c.getDeclaredMethod(name, types);
				m.setAccessible(true);
				return m.invoke(obj, parameters);
			} catch (NoSuchMethodException e) {
				c = c.getSuperclass(); 
			} catch (Exception e) {
				break;
			}
		}
		return null;
	}

}
