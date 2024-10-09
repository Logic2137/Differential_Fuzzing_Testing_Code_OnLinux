import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

@SupportedAnnotationTypes("*")
public class GenerateTypeProcessor extends AbstractProcessor {

    private String code = "public class GeneratedType { }";

    private boolean firstTime = true;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (firstTime) {
            try (OutputStream out = processingEnv.getFiler().createSourceFile("GeneratedType").openOutputStream()) {
                out.write(code.getBytes());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            firstTime = false;
        }
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
}
