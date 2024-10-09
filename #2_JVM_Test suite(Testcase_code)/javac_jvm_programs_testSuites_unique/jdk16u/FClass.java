
package vm.share.options;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FClass
{

    
    String key(); 
    
    String description();

    
    Class<?> type();
}
