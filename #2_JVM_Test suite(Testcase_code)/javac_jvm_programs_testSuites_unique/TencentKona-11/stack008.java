



package nsk.stress.stack;


import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class stack008 {
    public static void main(String[] args) {
        int exitCode = run(args, System.out);
        System.exit(exitCode + 95);
    }

    public static int run(String args[], PrintStream out) {
        int depth;
        
        
        
        for (depth = 100; ; depth += 100)
            try {
                invokeRecurse(depth);
            } catch (Throwable exception) {
                Throwable target = getTargetException(exception);
                if ((target instanceof StackOverflowError) ||
                        (target instanceof OutOfMemoryError))
                    break; 
                target.printStackTrace(out);
                if (target instanceof ThreadDeath)
                    throw (ThreadDeath) target;
                return 2;
            }
        out.println("Max. depth: " + depth);
        
        
        
        for (int i = 0; i < 100; i++)
            try {
                invokeRecurse(2 * depth);

            } catch (Throwable exception) {
                Throwable target = getTargetException(exception);
                if ((target instanceof StackOverflowError) ||
                        (target instanceof OutOfMemoryError))
                    continue; 
                target.printStackTrace(out);
                if (target instanceof ThreadDeath)
                    throw (ThreadDeath) target;
                return 2;
            }
        return 0;
    }

    private static Throwable getTargetException(Throwable exception) {
        Throwable target;
        
        
        
        for (
                target = exception;
                target instanceof InvocationTargetException;
                target = ((InvocationTargetException) target).getTargetException()
                )
            ;
        return target;
    }

    static Method method = null;
    static stack008 instance = null;
    static Object params[] = null;

    private static void invokeRecurse(int depth) throws Exception {
        if (method == null) {
            
            
            
            instance = new stack008();
            method = stack008.class.getMethod("recurse");
            params = new Object[]{};
        }
        
        
        
        instance.depth = depth;
        method.invoke(instance, params);
    }

    int depth = 0;

    public void recurse() throws Exception {
        if (depth > 0)
            
            
            
            invokeRecurse(depth - 1);
    }
}
