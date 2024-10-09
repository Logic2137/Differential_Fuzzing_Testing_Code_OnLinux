

public interface ClassLoaderAware {
    public ClassLoader getContextClassLoader();
    public void checkMemberAccess(Class<?> clazz, int which);
}
