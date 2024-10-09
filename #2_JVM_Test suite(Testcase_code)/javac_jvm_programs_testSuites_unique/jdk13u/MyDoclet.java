
package pkg;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.util.ElementScanner9;
import javax.tools.Diagnostic;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;

public class MyDoclet implements Doclet {

    private static final boolean OK = true;

    private boolean verbose;

    private Reporter reporter;

    Set<Option> options = Set.of(new Option("--alpha -a", false, "an example no-arg option") {

        @Override
        public boolean process(String option, List<String> arguments) {
            System.out.println("received option " + option + " " + arguments);
            return OK;
        }
    }, new Option("--beta -b", true, "an example 1-arg option") {

        @Override
        public boolean process(String option, List<String> arguments) {
            System.out.println("received option " + option + " " + arguments);
            return OK;
        }
    }, new Option("--verbose", false, "report progress") {

        @Override
        public boolean process(String option, List<String> arguments) {
            verbose = true;
            return OK;
        }
    });

    @Override
    public void init(Locale locale, Reporter reporter) {
        this.reporter = reporter;
    }

    @Override
    public String getName() {
        return "MyDoclet";
    }

    @Override
    public Set<? extends Option> getSupportedOptions() {
        return options;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public boolean run(DocletEnvironment environment) {
        MyScanner myScanner = new MyScanner();
        for (Element e : environment.getSpecifiedElements()) {
            myScanner.scan(e, 0);
        }
        return OK;
    }

    class MyScanner extends ElementScanner9<Void, Integer> {

        @Override
        public Void scan(Element e, Integer depth) {
            String msg = e.getKind() + " " + e;
            reporter.print(Diagnostic.Kind.NOTE, e, msg);
            return super.scan(e, depth + 1);
        }
    }

    abstract class Option implements Doclet.Option {

        final List<String> names;

        final boolean hasArg;

        final String description;

        Option(String names, boolean hasArg, String description) {
            this.names = List.of(names.split("\\s+"));
            this.hasArg = hasArg;
            this.description = description;
        }

        @Override
        public int getArgumentCount() {
            return hasArg ? 1 : 0;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public Kind getKind() {
            return Kind.STANDARD;
        }

        @Override
        public List<String> getNames() {
            return names;
        }

        @Override
        public String getParameters() {
            return hasArg ? "<arg>" : null;
        }
    }
}
