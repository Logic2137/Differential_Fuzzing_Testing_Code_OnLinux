import java.lang.annotation.*;
import static java.lang.annotation.RetentionPolicy.*;

@Retention(RUNTIME)
public @interface ExpectedDeprecation {

    boolean value();
}
