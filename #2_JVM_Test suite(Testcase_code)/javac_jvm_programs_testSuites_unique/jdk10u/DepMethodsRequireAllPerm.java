



import java.security.AllPermission;
import java.security.Permission;

public class DepMethodsRequireAllPerm {

    static class MySecurityManager extends SecurityManager {
        final Class<?> expectedClass;

        MySecurityManager(Class<?> c) {
            expectedClass = c;
        }

        @Override
        public void checkPermission(Permission perm) {
            if (perm.getClass() != expectedClass)
                throw new RuntimeException("Got: " + perm.getClass() + ", expected: " + expectedClass);
            super.checkPermission(perm);
        }
    }

    public static void main(String[] args) {
        MySecurityManager sm = new MySecurityManager(AllPermission.class);

        try {
            sm.checkAwtEventQueueAccess();
            throw new RuntimeException("SecurityException expected");
        } catch (SecurityException expected) { }

        try {
            sm.checkSystemClipboardAccess();
            throw new RuntimeException("SecurityException expected");
        } catch (SecurityException expected) { }

        try {
            sm.checkTopLevelWindow(null);
            throw new RuntimeException("NullPointException expected");
        } catch (NullPointerException expected) { }

        if (sm.checkTopLevelWindow(new Object())) {
            throw new RuntimeException("checkTopLevelWindow expected to return false");
        }

        try {
            sm.checkMemberAccess(Object.class, java.lang.reflect.Member.DECLARED);
            throw new RuntimeException("SecurityException expected");
        } catch (SecurityException expected) { }

        try {
            sm.checkMemberAccess(null, java.lang.reflect.Member.DECLARED);
            throw new RuntimeException("NullPointerException expected");
        } catch (NullPointerException expected) { }
    }
}
