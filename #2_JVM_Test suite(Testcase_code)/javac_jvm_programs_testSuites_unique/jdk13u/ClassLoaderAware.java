
package jdk.nashorn.test.models;

@SuppressWarnings("javadoc")
public interface ClassLoaderAware {

    public ClassLoader getContextClassLoader();

    public void checkMemberAccess(Class<?> clazz, int which);
}
