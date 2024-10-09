import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes("*")
public class NonPublicAnnotations extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> roots, RoundEnvironment roundEnv) {
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}

class TestForkJoinPool extends ForkJoinPool {
}
