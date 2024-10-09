

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreeScanner;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.util.Set;

@SupportedAnnotationTypes("*")
public class TypeAnnotationPositionProcessor extends AbstractProcessor {
    private Trees trees;
    private boolean processed = false;

    @Override
    public void init(ProcessingEnvironment pe) {
        super.init(pe);
        trees = Trees.instance(pe);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (processed) {
            return false;
        } else {
            processed = true;
        }
        Set<? extends Element> elements = roundEnv.getRootElements();
        TypeElement typeElement = null;
        for (TypeElement te : ElementFilter.typesIn(elements)) {
            if ("TypeAnnotationPositionTest".equals(te.getSimpleName().toString())) {
                typeElement = te;
                break;
            }
        }
        for (ExecutableElement m : ElementFilter.methodsIn(typeElement.getEnclosedElements())) {
            if ("test".equals(m.getSimpleName().toString())) {
                MethodTree methodTree = trees.getTree(m);
                new PositionVisitor().scan(methodTree, ((JCMethodDecl) methodTree).pos);
            }
        }
        return false;
    }

    private static class PositionVisitor extends TreeScanner<Void, Integer> {
        @Override
        public Void scan(Tree tree, Integer p) {
            if (tree != null) ((JCTree) tree).pos = p;
            return super.scan(tree, p);
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
}
