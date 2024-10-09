import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import jdk.javadoc.doclet.*;

public class BaseClass implements Doclet {

    public boolean run(DocletEnvironment root) {
        Elements elementUtils = root.getElementUtils();
        TypeElement klass = elementUtils.getTypeElement("baz.Foo");
        if (!root.isIncluded(klass)) {
            throw new AssertionError("Base class is not included: baz.Foo");
        }
        for (TypeElement te : ElementFilter.typesIn(root.getSpecifiedElements())) {
            if (te.getKind() == ElementKind.CLASS && te.getSimpleName().contentEquals("Bar")) {
                klass = te;
            }
        }
        if (klass == null) {
            throw new AssertionError("class Bar not found");
        }
        List<? extends Element> members = klass.getEnclosedElements();
        boolean foundPublic = false;
        boolean foundProtected = false;
        boolean foundPackagePrivate = false;
        boolean foundPrivate = false;
        List<Element> included = members.stream().filter(cls -> root.isIncluded(cls)).collect(Collectors.toList());
        for (Element e : included) {
            System.out.println("element: " + e);
            if (e.getSimpleName().toString().equals("aPublicMethod")) {
                foundPublic = true;
            }
            if (e.getSimpleName().toString().equals("aProtectedMethod")) {
                foundProtected = true;
            }
            if (e.getSimpleName().toString().equals("aPackagePrivateMethod")) {
                foundPackagePrivate = true;
            }
            if (e.getSimpleName().toString().equals("aPackagePrivateMethod")) {
                foundPrivate = true;
            }
        }
        if (!foundPublic || !foundProtected) {
            throw new AssertionError("selected methods not found");
        }
        if (foundPrivate || foundPackagePrivate) {
            throw new AssertionError("unselected methods found");
        }
        return true;
    }

    public Set<Doclet.Option> getSupportedOptions() {
        return Collections.emptySet();
    }

    public void init(Locale locale, Reporter reporter) {
        return;
    }

    @Override
    public String getName() {
        return "BaseClass";
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
}
