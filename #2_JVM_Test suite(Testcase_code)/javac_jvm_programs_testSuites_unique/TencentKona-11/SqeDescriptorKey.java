

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.management.DescriptorKey;


@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface SqeDescriptorKey {
    @DescriptorKey("sqeDescriptorKey")
    String value();

    
    
    
    
    
    
    
    @DescriptorKey("descriptorFields")
    String[] descriptorFields() default {};
}
