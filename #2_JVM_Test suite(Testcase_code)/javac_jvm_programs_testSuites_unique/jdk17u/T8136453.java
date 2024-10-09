import java.util.Arrays;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import com.sun.source.util.JavacTask;

public class T8136453 {

    public static void main(String... args) {
        new T8136453().run();
    }

    void run() {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> opts = Arrays.asList("-parameters");
        JavacTask task = (JavacTask) compiler.getTask(null, null, null, opts, null, null);
        TypeElement t = task.getElements().getTypeElement("T");
        ExecutableElement testMethod = ElementFilter.methodsIn(t.getEnclosedElements()).get(0);
        VariableElement param = testMethod.getParameters().get(0);
        Name paramName = param.getSimpleName();
        if (!paramName.contentEquals("p")) {
            throw new AssertionError("Wrong parameter name: " + paramName);
        }
    }
}
