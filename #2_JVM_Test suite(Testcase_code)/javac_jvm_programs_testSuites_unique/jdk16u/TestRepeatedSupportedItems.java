



import java.lang.annotation.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;


@SupportedAnnotationTypes({"foo/Baz", "foo/Baz", "bar/Baz", "Baz", "Baz"})
@SupportedOptions({"Foo", "Foo"})
@Baz
public class TestRepeatedSupportedItems extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnvironment) {
        return true;
    }
}

@Retention(RetentionPolicy.RUNTIME)
@interface Baz {
}
