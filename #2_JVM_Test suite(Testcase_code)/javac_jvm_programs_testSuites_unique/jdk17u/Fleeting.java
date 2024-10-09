import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Fleeting {

    int value();
}
