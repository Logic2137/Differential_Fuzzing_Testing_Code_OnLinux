



import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.lang.model.SourceVersion;
import javax.tools.Diagnostic.Kind;
import javax.tools.DocumentationTool;
import javax.tools.DocumentationTool.DocumentationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.Reporter;
import jdk.javadoc.doclet.DocletEnvironment;

public class VerifyLocale implements Doclet {
    
    
    static String language;
    static String country;
    static String variant;

    Locale locale;
    Reporter reporter;

    public static void main(String[] args) {
        DocumentationTool tool = ToolProvider.getSystemDocumentationTool();
        Path thisFile =
            Paths.get(System.getProperty("test.src", ".")).resolve("VerifyLocale.java");
        JavaFileObject fo = tool.getStandardFileManager(null, null, null)
                .getJavaFileObjects(thisFile).iterator().next();

        int skipCount = 0;
        int testCount = 0;

        var languages = new HashSet<>();
        var countries = new HashSet<>();
        var variants = new HashSet<>();

        for (Locale loc : Locale.getAvailableLocales()) {
            language = loc.getLanguage();
            country = loc.getCountry();
            variant = loc.getVariant();

            
            if (!loc.equals(Locale.forLanguageTag(loc.toLanguageTag()))) {
                System.err.println("skipped " + loc
                        + " (language tag round trip: "
                        + loc.toLanguageTag()
                        + ": " + Locale.forLanguageTag(loc.toLanguageTag()) + ")");
                System.err.println();
                skipCount++;
                continue;
            }

            
            
            if (!languages.add(language)
                    & !countries.add(country)
                    & !variants.add(variant)) {
                System.err.println("skipped " + loc + " (duplicate part)");
                System.err.println();
                skipCount++;
                continue;
            }

            System.err.printf("test locale: %s [%s,%s,%s] %s%n",
                loc, language, country, variant, loc.toLanguageTag());

            if (!language.equals("")) {
                List<String> options = List.of("-locale", loc.toLanguageTag());
                System.err.println("test options: " + options);
                DocumentationTask t = tool.getTask(null, null, null,
                        VerifyLocale.class, options, List.of(fo));
                if (!t.call())
                    throw new Error("javadoc encountered warnings or errors.");
                testCount++;
            }
            System.err.println();
        }
        System.err.println("Skipped " + skipCount + " locales");
        System.err.println("Tested " + testCount + " locales");
    }

    public boolean run(DocletEnvironment root) {
        reporter.print(Kind.NOTE, String.format("doclet locale is: %s [%s,%s,%s] %s (%s)",
                locale,
                locale.getLanguage(),
                locale.getCountry(),
                locale.getVariant(),
                locale.toLanguageTag(),
                locale.getDisplayName()));
        return language.equals(locale.getLanguage())
               && country.equals(locale.getCountry())
               && variant.equals(locale.getVariant());
    }

    @Override
    public String getName() {
        return "Test";
    }

    @Override
    public Set<Option> getSupportedOptions() {
        return Collections.emptySet();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    public void init(Locale locale, Reporter reporter) {
        this.locale = locale;
        this.reporter = reporter;
    }
}
