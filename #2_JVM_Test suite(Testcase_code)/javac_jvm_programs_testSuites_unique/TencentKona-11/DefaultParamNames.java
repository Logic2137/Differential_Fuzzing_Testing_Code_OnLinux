



import static java.util.stream.Collectors.joining;

import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type.MethodType;
import com.sun.tools.javac.util.Assert;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;
import java.util.Objects;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class DefaultParamNames {

    public static void main(String[] args) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        BasicJavacTask task = (BasicJavacTask) compiler.getTask(null, null, null, null, null, null);
        Context context = task.getContext();
        Names names = Names.instance(context);
        Symtab symtab = Symtab.instance(context);
        MethodType mt =
                new MethodType(
                        List.of(symtab.intType, symtab.stringType, symtab.objectType),
                        symtab.voidType,
                        List.nil(),
                        symtab.methodClass);
        MethodSymbol ms =
                new MethodSymbol(Flags.SYNTHETIC, names.fromString("test"), mt, symtab.methodClass);
        String paramNames =
                ms.params().stream().map(p -> p.getSimpleName().toString()).collect(joining(","));
        assertEquals("arg0,arg1,arg2", paramNames);
    }

    static void assertEquals(Object expected, Object actual) {
        Assert.check(
                Objects.equals(expected, actual),
                String.format("expected: %s, but was: %s", expected, actual));
    }
}
