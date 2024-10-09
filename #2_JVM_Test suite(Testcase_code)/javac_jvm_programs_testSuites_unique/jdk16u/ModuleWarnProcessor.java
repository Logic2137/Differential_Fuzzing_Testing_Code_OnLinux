

package processor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.tools.Diagnostic.Kind;
import javax.lang.model.element.AnnotationMirror;

@SupportedAnnotationTypes("annotation.ModuleWarn")
public class ModuleWarnProcessor extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return this.processingEnv.getSourceVersion().compareTo(SourceVersion.RELEASE_9) < 0 ? SourceVersion.RELEASE_9 : this.processingEnv.getSourceVersion();
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotations.stream().flatMap(annotation -> roundEnv.getElementsAnnotatedWith(annotation).stream()).forEach(element -> {
            for(AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
                this.processingEnv.getMessager().printMessage(Kind.MANDATORY_WARNING, "Module warning", element, annotationMirror);
            }
        });
        return false;
    }
}

