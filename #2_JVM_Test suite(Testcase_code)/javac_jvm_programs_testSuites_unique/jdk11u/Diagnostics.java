

package tools.javac.combo;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;


public class Diagnostics implements javax.tools.DiagnosticListener<JavaFileObject> {

    protected List<Diagnostic<? extends JavaFileObject>> diags = new ArrayList<>();
    protected boolean foundErrors = false;

    public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
        diags.add(diagnostic);
        foundErrors = foundErrors || diagnostic.getKind() == Diagnostic.Kind.ERROR;
    }

    
    public boolean errorsFound() {
        return foundErrors;
    }

    
    public List<String> keys() {
        return diags.stream()
                    .map(Diagnostic::getCode)
                    .collect(toList());
    }

    
    public boolean containsErrorKey(String key) {
        return diags.stream()
                    .filter(d -> d.getKind() == Diagnostic.Kind.ERROR)
                    .anyMatch(d -> d.getCode().equals(key));
    }

    
    public List<String> errorKeys() {
        return diags.stream()
                    .filter(d -> d.getKind() == Diagnostic.Kind.ERROR)
                    .map(Diagnostic::getCode)
                    .collect(toList());
    }

    public String toString() { return keys().toString(); }

    
    public void reset() {
        diags.clear();
        foundErrors = false;
    }
}
