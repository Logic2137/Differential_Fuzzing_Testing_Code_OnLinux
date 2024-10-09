

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.lang.model.element.Element;

import com.sun.source.doctree.DocTree;
import com.sun.source.doctree.TextTree;
import com.sun.source.doctree.UnknownBlockTagTree;
import com.sun.source.doctree.UnknownInlineTagTree;
import com.sun.source.util.SimpleDocTreeVisitor;
import jdk.javadoc.doclet.Taglet;
import jdk.javadoc.doclet.Taglet.Location;
import static jdk.javadoc.doclet.Taglet.Location.*;




public class ToDoTaglet implements Taglet {

    private static final String NAME = "todo";
    private static final String HEADER = "To Do:";

    
    public String getName() {
        return NAME;
    }

    private final EnumSet<Location> allowedSet = EnumSet.allOf(Location.class);

    @Override
    public Set<Taglet.Location> getAllowedLocations() {
        return allowedSet;
    }

    

    public boolean isInlineTag() {
        return false;
    }

    
    @Override
    public String toString(List<? extends DocTree> tags, Element element) {
        if (tags.isEmpty()) {
            return "";
        }
        String result = "\n<DT><B>" + HEADER + "</B><DD>";
        result += "<table summary=\"Summary\" cellpadding=2 cellspacing=0><tr><td bgcolor=\"yellow\">";
        for (int i = 0; i < tags.size(); i++) {
            if (i > 0) {
                result += ", ";
            }
            result += getText(tags.get(i));
        }
        return result + "</td></tr></table></DD>\n";
    }

    static String getText(DocTree dt) {
        return new SimpleDocTreeVisitor<String, Void>() {
            @Override
            public String visitUnknownBlockTag(UnknownBlockTagTree node, Void p) {
                for (DocTree dt : node.getContent()) {
                    return dt.accept(this, null);
                }
                return "";
            }

            @Override
            public String visitUnknownInlineTag(UnknownInlineTagTree node, Void p) {
                for (DocTree dt : node.getContent()) {
                    return dt.accept(this, null);
                }
                return "";
            }

            @Override
            public String visitText(TextTree node, Void p) {
                return node.getBody();
            }

            @Override
            protected String defaultAction(DocTree node, Void p) {
                return "";
            }

        }.visit(dt, null);
    }
}
