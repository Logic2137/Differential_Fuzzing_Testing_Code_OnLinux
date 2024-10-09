public class DoPrivAccomplice {

    public String go() {
        String name = (String) java.security.AccessController.doPrivileged((java.security.PrivilegedAction) () -> System.getProperty("user.name"));
        return name;
    }
}
