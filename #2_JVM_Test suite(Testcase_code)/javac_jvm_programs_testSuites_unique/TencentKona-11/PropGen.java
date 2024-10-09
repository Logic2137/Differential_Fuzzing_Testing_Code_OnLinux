

package propgen;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PropGen {

    
    public static void main(String[] args) throws IOException {
        new PropGen().run();
    }

    final PrintStream out;

    final Path outFile;
    final ByteArrayOutputStream baos;

    PropGen() {
        out = System.out;
        outFile = null;
        baos = null;
    }

    public PropGen(Path outDir) throws IOException {
        outFile = Paths.get(outDir.toString(), "Demo.java");
        baos = new ByteArrayOutputStream();
        out = new PrintStream(baos);
    }

    enum Kind {
        FIELD(1),
        GETTER(2),
        SETTER(4),
        PROPERTY(8);
        Kind(int bit) {
            this.bit = bit;
        }
        int bit;
    }

    public void run() throws IOException {
        out.println("import javafx.beans.property.Property;");
        out.println("public class Demo {");
        for (int i = 1; i < 16; i++) {
            Set<Kind> set = EnumSet.noneOf(Kind.class);
            for (Kind k : Kind.values()) {
                if ((i & k.bit) == k.bit) {
                    set.add(k);
                }
            }
            addItems(set);
        }
        out.println("}");
        if (baos != null && outFile != null) {
            Files.write(outFile, baos.toByteArray());
        }
    }

    void addItems(Set<Kind> kinds) {
        String name = kinds.stream()
                .map(k -> k.name())
                .map(s -> s.substring(0, 1))
                .collect(Collectors.joining(""))
                .toLowerCase();
        if (kinds.contains(Kind.FIELD)) {
            out.println("    ");
            out.println("    public Property " + name + ";");
        }
        if (kinds.contains(Kind.GETTER)) {
            out.println("    ");
            out.println("    public Object " + mname("get", name) + "() { return null; }");
        }
        if (kinds.contains(Kind.SETTER)) {
            out.println("    ");
            out.println("    public void " + mname("set", name) + "(Object o) {  }");
        }
        if (kinds.contains(Kind.PROPERTY)) {
            out.println("    ");
            out.println("    public Property " + name + "Property() { return null; }");
        }
        out.println();
    }

    String mname(String prefix, String base) {
        return prefix + Character.toUpperCase(base.charAt(0)) + base.substring(1);
    }

}
