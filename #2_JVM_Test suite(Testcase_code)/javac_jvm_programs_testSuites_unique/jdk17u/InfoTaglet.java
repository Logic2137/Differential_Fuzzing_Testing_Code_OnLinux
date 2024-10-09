import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Element;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Taglet;
import static jdk.javadoc.doclet.Taglet.Location.*;
import com.sun.source.doctree.DocTree;

public class InfoTaglet implements Taglet {

    private DocletEnvironment env;

    private Doclet doclet;

    @Override
    public void init(DocletEnvironment env, Doclet doclet) {
        this.env = env;
        this.doclet = doclet;
    }

    @Override
    public Set<Location> getAllowedLocations() {
        return EnumSet.of(TYPE);
    }

    @Override
    public boolean isInlineTag() {
        return false;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String toString(List<? extends DocTree> tags, Element element) {
        return "<dt>" + "<span class=\"simpleTagLabel\">Info:</span>\n" + "</dt>" + "<dd>" + "<ul>\n" + "<li>Element: " + element.getKind() + " " + element.getSimpleName() + "\n" + "<li>Element supertypes: " + env.getTypeUtils().directSupertypes(element.asType()) + "\n" + "<li>Doclet: " + doclet.getClass() + "\n" + "</ul>\n" + "</dd>";
    }
}
