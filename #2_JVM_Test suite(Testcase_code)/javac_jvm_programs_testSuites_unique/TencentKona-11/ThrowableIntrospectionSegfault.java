



import java.lang.reflect.*;

public class ThrowableIntrospectionSegfault {
    public static void main(java.lang.String[] unused) {
        
        Throwable throwable = new Throwable();
        throwable.fillInStackTrace();

        
        Class class1 = throwable.getClass();
        Field field;
        try {
            field = class1.getDeclaredField("backtrace");
        }
        catch (NoSuchFieldException e) {
            System.err.println("Can't retrieve field handle Throwable.backtrace: " + e.toString());
            return;
        }
        field.setAccessible(true);

        
        Object backtrace;
        try {
            backtrace = field.get(throwable);
        }
        catch (IllegalAccessException e) {
            System.err.println( "Can't retrieve field value for Throwable.backtrace: " + e.toString());
            return;
        }

        try {

            
            Class class2 = ((Object[]) ((Object[]) backtrace)[2])[0].getClass();

            
            
            String class2Name = class2.getName();

            System.err.println("class2Name=" + class2Name);
            return;  
        } catch (ClassCastException e) {
            
            
            System.out.println("Catch exception " + e);
            return;  
        }
    }
}
