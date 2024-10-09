



import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ModuleElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@SupportedAnnotationTypes("*")
public class ReleaseModulesAndTypeElement extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> roots, RoundEnvironment roundEnv) {
        Elements elements = processingEnv.getElementUtils();
        if (elements.getTypeElement(JX_A_P_GENERATED) == null) {
            throw new AssertionError("jx.a.p.Generated not found by unqualified search!");
        }
        ModuleElement javaBase = elements.getModuleElement("java.base");
        if (elements.getTypeElement(javaBase, JX_A_P_GENERATED) != null) {
            throw new AssertionError("jx.a.p.Generated found in java.base!");
        }
        ModuleElement javaCompiler = elements.getModuleElement("java.compiler");
        if (elements.getTypeElement(javaCompiler, JX_A_P_GENERATED) == null) {
            throw new AssertionError("jx.a.p.Generated not found in java.compiler!");
        }
        return false;
    }
    
        private static final String JX_A_P_GENERATED = "javax.annotation.processing.Generated";

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
