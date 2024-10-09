import java.lang.reflect.*;

public class WalkThroughInvoke {

    public void stackWalk() {
        try {
            Class b = Object.class;
            SecurityManager sm = new SecurityManager();
            sm.checkMemberAccess(b, Member.DECLARED);
        } catch (java.security.AccessControlException e) {
        }
    }
}
