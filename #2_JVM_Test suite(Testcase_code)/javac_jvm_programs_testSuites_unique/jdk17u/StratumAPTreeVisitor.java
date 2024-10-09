
package vm.mlvm.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.LabeledStatementTree;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

public class StratumAPTreeVisitor extends TreePathScanner<Object, Trees> {

    public static final String LABEL_PREFIX = "Stratum_";

    public static class StratumLineInfo implements Comparable<StratumLineInfo> {

        String stratumName;

        int stratumLineOrder;

        String stratumLine;

        int javaLineNum;

        public StratumLineInfo(String stratumName, int stratumLineOrder, String stratumLine, int javaLineNum) {
            this.stratumName = stratumName;
            this.stratumLineOrder = stratumLineOrder;
            this.stratumLine = stratumLine;
            this.javaLineNum = javaLineNum;
        }

        public String getStratumName() {
            return stratumName;
        }

        public int getStratumLineOrder() {
            return stratumLineOrder;
        }

        public String getStratumSourceLine() {
            return stratumLine;
        }

        public int getJavaLineNum() {
            return javaLineNum;
        }

        @Override
        public int compareTo(StratumLineInfo o) {
            int i;
            if ((i = getStratumName().compareTo(o.getStratumName())) != 0)
                return i;
            if ((i = getStratumLineOrder() - o.getStratumLineOrder()) != 0)
                return i;
            return 0;
        }

        @Override
        public String toString() {
            return getStratumName() + ":" + getStratumLineOrder() + " =>  Java:" + getJavaLineNum() + " [" + getStratumSourceLine() + "]";
        }
    }

    public Map<String, Set<StratumLineInfo>> strata = new HashMap<String, Set<StratumLineInfo>>();

    @Override
    public Object visitLabeledStatement(LabeledStatementTree node, Trees p) {
        processLabel(node, p);
        return super.visitLabeledStatement(node, p);
    }

    private void processLabel(LabeledStatementTree node, Trees p) {
        String label = node.getLabel().toString();
        if (!label.startsWith(LABEL_PREFIX))
            return;
        int stratumNameEndPos = label.indexOf('_', LABEL_PREFIX.length());
        if (stratumNameEndPos == -1)
            return;
        String stratumName = label.substring(LABEL_PREFIX.length(), stratumNameEndPos);
        int stratumLineEndPos = label.indexOf('_', stratumNameEndPos + 1);
        if (stratumLineEndPos == -1)
            return;
        String stratumLineNumStr = label.substring(stratumNameEndPos + 1, stratumLineEndPos);
        int stratumLineNum = Integer.parseInt(stratumLineNumStr);
        String stratumLine = label.substring(stratumLineEndPos + 1);
        CompilationUnitTree unit = getCurrentPath().getCompilationUnit();
        int javaLineNum = (int) unit.getLineMap().getLineNumber(p.getSourcePositions().getStartPosition(unit, node));
        Set<StratumLineInfo> stratumLines = this.strata.get(stratumName);
        if (stratumLines == null) {
            stratumLines = new TreeSet<StratumLineInfo>();
            this.strata.put(stratumName, stratumLines);
        }
        stratumLines.add(new StratumLineInfo(stratumName, stratumLineNum, stratumLine, javaLineNum));
    }
}
