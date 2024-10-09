



import java.io.*;
import java.lang.annotation.*;
import javax.lang.model.element.ElementKind;

public class AnnoTest {
    public static void main(String... args) throws Exception {
        new AnnoTest().run();
    }

    void run() throws Exception {
        String testClasses = System.getProperty("test.classes");
        String out = javap("-v", "-classpath", testClasses, A.class.getName());

        String nl = System.getProperty("line.separator");
        out = out.replaceAll(nl, "\n");

        if (out.contains("\n\n\n"))
            error("double blank line found");

        expect(out,
                "RuntimeVisibleAnnotations:\n" +
                "  0: #21(#22=B#23)\n" +
                "    AnnoTest$ByteAnno(\n" +
                "      value=(byte) 42\n" +
                "    )\n" +
                "  1: #24(#22=S#25)\n" +
                "    AnnoTest$ShortAnno(\n" +
                "      value=(short) 3\n" +
                "    )");
        expect(out,
                "RuntimeInvisibleAnnotations:\n" +
                "  0: #27(#22=[J#28,J#30,J#32,J#34,J#36])\n" +
                "    AnnoTest$ArrayAnno(\n" +
                "      value=[1l,2l,3l,4l,5l]\n" +
                "    )\n" +
                "  1: #38(#22=Z#39)\n" +
                "    AnnoTest$BooleanAnno(\n" +
                "      value=false\n" +
                "    )\n" +
                "  2: #40(#41=c#42)\n" +
                "    AnnoTest$ClassAnno(\n" +
                "      type=class Ljava/lang/Object;\n" +
                "    )\n" +
                "  3: #43(#44=e#45.#46)\n" +
                "    AnnoTest$EnumAnno(\n" +
                "      kind=Ljavax/lang/model/element/ElementKind;.PACKAGE\n" +
                "    )\n" +
                "  4: #47(#22=I#48)\n" +
                "    AnnoTest$IntAnno(\n" +
                "      value=2\n" +
                "    )\n" +
                "  5: #49()\n" +
                "    AnnoTest$IntDefaultAnno\n" +
                "  6: #50(#51=s#52)\n" +
                "    AnnoTest$NameAnno(\n" +
                "      name=\"NAME\"\n" +
                "    )\n" +
                "  7: #53(#54=D#55,#57=F#58)\n" +
                "    AnnoTest$MultiAnno(\n" +
                "      d=3.14159d\n" +
                "      f=2.71828f\n" +
                "    )\n" +
                "  8: #59()\n" +
                "    AnnoTest$SimpleAnno\n" +
                "  9: #60(#22=@#47(#22=I#61))\n" +
                "    AnnoTest$AnnoAnno(\n" +
                "      value=@AnnoTest$IntAnno(\n" +
                "        value=5\n" +
                "      )\n" +
                "    )");
        expect(out,
                "RuntimeInvisibleTypeAnnotations:\n" +
                "  0: #63(): CLASS_EXTENDS, type_index=0\n" +
                "    AnnoTest$TypeAnno");

        if (errors > 0)
            throw new Exception(errors + " errors found");
    }

    String javap(String... args) throws Exception {
        StringWriter sw = new StringWriter();
        int rc;
        try (PrintWriter out = new PrintWriter(sw)) {
            rc = com.sun.tools.javap.Main.run(args, out);
        }
        System.out.println(sw.toString());
        if (rc < 0)
            throw new Exception("javap exited, rc=" + rc);
        return sw.toString();
    }

    void expect(String text, String expect) {
        if (!text.contains(expect))
            error("expected text not found");
    }

    void error(String msg) {
        System.out.println("Error: " + msg);
        errors++;
    }

    int errors;

    
    public @interface SimpleAnno { }
    public @interface BooleanAnno { boolean value(); }
    public @interface IntAnno { int value(); }
    public @interface IntDefaultAnno { int value() default 3; }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ByteAnno { byte value(); }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ShortAnno { short value(); }

    public @interface NameAnno { String name(); }
    public @interface ArrayAnno { long[] value(); }
    public @interface EnumAnno { ElementKind kind(); }
    public @interface ClassAnno { Class<?> type(); }
    public @interface MultiAnno { float f(); double d(); }

    public @interface AnnoAnno { IntAnno value(); }

    @Target(ElementType.TYPE_USE)
    public @interface TypeAnno { }

    @ArrayAnno({1, 2, 3, 4, 5})
    @BooleanAnno(false)
    @ByteAnno(42)
    @ClassAnno(type = Object.class)
    @EnumAnno(kind = ElementKind.PACKAGE)
    @IntAnno(2)
    @IntDefaultAnno
    @NameAnno(name = "NAME")
    @MultiAnno(d = 3.14159, f = 2.71828f)
    @ShortAnno(3)
    @SimpleAnno
    @AnnoAnno(@IntAnno(5))
    public abstract class A implements @TypeAnno Runnable { }
}
