

package jdk.nashorn.test.models;

import jdk.nashorn.api.tree.SimpleTreeVisitorES6;
import jdk.nashorn.api.tree.*;

public class ES6ASTVisitor{

        public static void visit(String script) {
                DiagnosticListener listener = (Diagnostic diag) -> { System.err.println(diag.toString()); };
                Parser parser = Parser.create("--language=es6","--empty-statements");
                Tree astRoot = parser.parse("unknown", script, listener);
                astRoot.accept(new SimpleTreeVisitorES6<Boolean, Void>() {
                        @Override
                        public Boolean visitCompilationUnit(CompilationUnitTree stmt, Void none) {
                                for (Tree item : stmt.getSourceElements()) {
                                        System.out.println(item.getStartPosition() + "-" + item.getEndPosition() + " " + item.getKind());
                                }
                                return super.visitCompilationUnit(stmt, none);
                        }

                }, null);

        }

}

