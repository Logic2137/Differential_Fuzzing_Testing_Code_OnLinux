
import java.lang.reflect.*;

public class WalkThroughInvoke {
  public void stackWalk() {
      try {
          Class b = Object.class;
          SecurityManager sm = new SecurityManager();
          
          
          sm.checkPermission(new RuntimePermission("accessDeclaredMembers"));
      } catch (java.security.AccessControlException e) {
          
          
      }
  }
};
