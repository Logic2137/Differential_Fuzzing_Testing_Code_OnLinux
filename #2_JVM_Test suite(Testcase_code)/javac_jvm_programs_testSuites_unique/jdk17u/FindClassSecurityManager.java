
package test.java.lang.invoke;

import java.lang.invoke.MethodHandles;

public class FindClassSecurityManager {

    public static void main(String[] args) throws Throwable {
        assert null != System.getSecurityManager();
        Class<?> thisClass = FindClassSecurityManager.class;
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        Class<?> lookedUp = lookup.findClass(thisClass.getName());
        assert thisClass == lookedUp;
        Class<?> accessed = lookup.accessClass(thisClass);
        assert thisClass == accessed;
    }
}
