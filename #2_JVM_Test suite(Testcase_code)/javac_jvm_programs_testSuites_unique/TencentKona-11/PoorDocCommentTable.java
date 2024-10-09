
package sampleapi.util;

import java.util.HashMap;
import com.sun.tools.javac.parser.Tokens.Comment;
import com.sun.tools.javac.tree.DCTree.DCDocComment;
import com.sun.tools.javac.tree.DocCommentTable;
import com.sun.tools.javac.tree.JCTree;

public class PoorDocCommentTable implements DocCommentTable {

    HashMap<JCTree, Comment> table;

    public PoorDocCommentTable() {
        table = new HashMap<>();
    }

    public boolean hasComment(JCTree tree) {
        return table.containsKey(tree);
    }

    public Comment getComment(JCTree tree) {
        return table.get(tree);
    }

    public String getCommentText(JCTree tree) {
        Comment c = getComment(tree);
        return (c == null) ? null : c.getText();
    }

    public DCDocComment getCommentTree(JCTree tree) {
        return null;
    }

    public void putComment(JCTree tree, Comment c) {
        table.put(tree, c);
    }
}
