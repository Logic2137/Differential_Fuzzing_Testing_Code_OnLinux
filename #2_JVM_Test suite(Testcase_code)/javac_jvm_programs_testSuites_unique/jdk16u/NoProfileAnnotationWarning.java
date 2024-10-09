



import javax.annotation.processing.ProcessingEnvironment;

public class NoProfileAnnotationWarning {
    void t(ProcessingEnvironment pe) {
        pe.getElementUtils().getTypeElement("a");
    }
}
