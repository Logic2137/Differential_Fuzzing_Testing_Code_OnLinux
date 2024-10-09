



import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VolatileCallSite;

public class Test7158807 {
    
    public static void main(String[] args) throws Throwable {
        for (int i = 0; i < 25600; i++) {
            MethodType mt = MethodType.methodType(java.lang.String.class);
            System.out.println(mt);
            MethodType mt3 = null;
            try {
              mt3 = MethodType.genericMethodType(i);
            } catch (IllegalArgumentException e) {
              System.out.println("Passed");
              System.exit(95);
            }
            System.out.println(i+":");
            try {
                VolatileCallSite vcs = new VolatileCallSite(mt3);
                System.out.println(vcs);
                MethodHandle mh = vcs.dynamicInvoker();
                vcs.setTarget(mh);
                
                mh.invoke(mt, mh);
            } catch (Throwable e) {
            }
        }
    }
}

