



import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import com.sun.source.tree.IfTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreeScanner;
import com.sun.source.util.Trees;
import javax.tools.Diagnostic;

@SupportedAnnotationTypes("*")
public class PatternMatchPosTest extends AbstractProcessor {

    int round;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (round++ != 0)
            return false;

        try {
            TypeElement data = processingEnv.getElementUtils().getTypeElement("PatternMatchPosTestData");
            Trees trees = Trees.instance(processingEnv);
            SourcePositions sp = trees.getSourcePositions();
            TreePath dataPath = trees.getPath(data);
            String text = dataPath.getCompilationUnit().getSourceFile().getCharContent(true).toString();

            new TreeScanner<Void, Void>() {
                boolean print;
                @Override
                public Void visitIf(IfTree node, Void p) {
                    boolean prevPrint = print;
                    try {
                        print = true;
                        scan(node.getCondition(), p);
                    } finally {
                        print = prevPrint;
                    }
                    scan(node.getThenStatement(), p);
                    scan(node.getElseStatement(), p);
                    return null;
                }
                @Override
                public Void scan(Tree tree, Void p) {
                    if (tree == null)
                        return null;
                    if (print) {
                        int start = (int) sp.getStartPosition(dataPath.getCompilationUnit(), tree);
                        int end = (int) sp.getEndPosition(dataPath.getCompilationUnit(), tree);
                        if (start != (-1) || end != (-1)) {
                            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                                                                     text.substring(start, end));
                        }
                    }
                    return super.scan(tree, p);
                }
            }.scan(dataPath.getLeaf(), null);
            return false;
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
