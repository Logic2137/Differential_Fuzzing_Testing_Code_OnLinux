import com.sun.tools.doclint.Checker;
import com.sun.tools.doclint.Entity;
import com.sun.tools.doclint.HtmlTag;
import com.sun.tools.doclint.Messages;
import java.util.Objects;

public class CoverageExtras {

    public static void main(String... args) {
        new CoverageExtras().run();
    }

    void run() {
        check(HtmlTag.A, HtmlTag.valueOf("A"), HtmlTag.values());
        check(HtmlTag.Attr.ABBR, HtmlTag.Attr.valueOf("ABBR"), HtmlTag.Attr.values());
        check(HtmlTag.AttrKind.INVALID, HtmlTag.AttrKind.valueOf("INVALID"), HtmlTag.AttrKind.values());
        check(HtmlTag.BlockType.BLOCK, HtmlTag.BlockType.valueOf("BLOCK"), HtmlTag.BlockType.values());
        check(HtmlTag.EndKind.NONE, HtmlTag.EndKind.valueOf("NONE"), HtmlTag.EndKind.values());
        check(HtmlTag.Flag.EXPECT_CONTENT, HtmlTag.Flag.valueOf("EXPECT_CONTENT"), HtmlTag.Flag.values());
        check(Checker.Flag.TABLE_HAS_CAPTION, Checker.Flag.valueOf("TABLE_HAS_CAPTION"), Checker.Flag.values());
        check(Entity.nbsp, Entity.valueOf("nbsp"), Entity.values());
        check(Messages.Group.ACCESSIBILITY, Messages.Group.valueOf("ACCESSIBILITY"), Messages.Group.values());
    }

    <T extends Enum<T>> void check(T expect, T value, T[] values) {
        if (!Objects.equals(expect, value)) {
            error("Mismatch: '" + expect + "', '" + value + "'");
        }
        if (!Objects.equals(expect, values[0])) {
            error("Mismatch: '" + expect + "', '" + values[0] + "'");
        }
    }

    void error(String msg) {
        System.err.println("Error: " + msg);
        errors++;
    }

    int errors;
}
