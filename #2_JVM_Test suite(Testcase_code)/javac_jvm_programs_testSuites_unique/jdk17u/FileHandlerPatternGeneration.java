import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;

public class FileHandlerPatternGeneration {

    static final String[] PATTERNS = { "C:/Workspace/hoge.log", "C:/Workspace/hoge.log", "C:/Workspace%g/hoge.log", "C:/Workspace%g/hoge.log", "C:/%uWorkspace/hoge.log", "C:/%uWorkspace/hoge.log", "C:/%uWorkspace%g/hoge.log", "C:/%uWorkspace%g/hoge.log", "C:/Workspace/%ghoge.log", "C:/Workspace/%ghoge.log", "C:/Workspace/%ghoge%u.log", "C:/Workspace/%ghoge%u.log", "C:/Workspace-%g/hoge.log", "C:/Workspace-%g/hoge.log", "C:/Work%hspace/hoge.log", "%h/space/hoge.log", "C:/Works%tpace%g/hoge.log", "%t/pace%g/hoge.log", "C:/%uWork%hspace/hoge.log", "%h/space/hoge.log", "C:/%uWorkspace%g/%thoge.log", "%t/hoge.log", "C:/Workspace/%g%h%%hoge.log", "%h/%%hoge.log", "C:/Work%h%%hspace/hoge.log", "%h/%%hspace/hoge.log", "C:/Works%t%%hpace%g/hoge.log", "%t/%%hpace%g/hoge.log", "C:/%uWork%h%%tspace/hoge.log", "%h/%%tspace/hoge.log", "C:/%uWorkspace%g/%t%%hoge.log", "%t/%%hoge.log", "C:/Workspace/%g%h%%hoge.log", "%h/%%hoge.log", "ahaha", "ahaha", "ahaha/ahabe", "ahaha/ahabe", "../ahaha/ahabe", "../ahaha/ahabe", "/x%ty/w/hoge.log", "%t/y/w/hoge.log", "/x/%ty/w/hoge.log", "%t/y/w/hoge.log", "/x%t/y/w/hoge.log", "%t/y/w/hoge.log", "/x/%t/y/w/hoge.log", "%t/y/w/hoge.log", "%ty/w/hoge.log", "%t/y/w/hoge.log", "%t/y/w/hoge.log", "%t/y/w/hoge.log", "/x%hy/w/hoge.log", "%h/y/w/hoge.log", "/x/%hy/w/hoge.log", "%h/y/w/hoge.log", "/x%h/y/w/hoge.log", "%h/y/w/hoge.log", "/x/%h/y/w/hoge.log", "%h/y/w/hoge.log", "%hy/w/hoge.log", "%h/y/w/hoge.log", "%h/y/w/hoge.log", "%h/y/w/hoge.log", "ahaha-%u-%g", "ahaha-%u-%g", "ahaha-%g/ahabe-%u", "ahaha-%g/ahabe-%u", "../ahaha-%u/ahabe", "../ahaha-%u/ahabe", "/x%ty/w/hoge-%g.log", "%t/y/w/hoge-%g.log", "/x/%ty/w/hoge-%u.log", "%t/y/w/hoge-%u.log", "%u-%g/x%t/y/w/hoge.log", "%t/y/w/hoge.log", "/x/%g%t%u/y/w/hoge.log", "%t/%u/y/w/hoge.log", "%ty/w-%g/hoge.log", "%t/y/w-%g/hoge.log", "%t/y/w-%u/hoge.log", "%t/y/w-%u/hoge.log", "/x%hy/%u-%g-w/hoge.log", "%h/y/%u-%g-w/hoge.log", "/x/%hy/w-%u-%g/hoge.log", "%h/y/w-%u-%g/hoge.log", "/x%h/y/w/%u-%ghoge.log", "%h/y/w/%u-%ghoge.log", "/x/%h/y/w/hoge-%u-%g.log", "%h/y/w/hoge-%u-%g.log", "%hy/w/%u-%g-hoge.log", "%h/y/w/%u-%g-hoge.log", "%h/y/w/hoge-%u-%g.log", "%h/y/w/hoge-%u-%g.log", "/x/y/z/hoge-%u.log", "/x/y/z/hoge-%u.log" };

    static final int[][] GENERATIONS = { { 0, 0, 0 }, { 0, 1, 0 }, { 0, 1, 1 }, { 1, 1, 0 }, { 1, 1, 1 }, { 1, 1, 2 }, { 1, 2, 3 }, { 3, 4, 0 }, { 3, 4, 1 }, { 3, 4, 2 }, { 3, 0, 5 }, { 3, 1, 5 }, { 3, 2, 5 } };

    static final Class<FileHandler> FILE_HANDLER_CLASS = FileHandler.class;

    static final Method GENERATE;

    static final String USER_HOME;

    static final String TMP;

    static {
        Method generate;
        try {
            generate = FILE_HANDLER_CLASS.getDeclaredMethod("generate", String.class, int.class, int.class, int.class);
            generate.setAccessible(true);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
        GENERATE = generate;
        USER_HOME = System.getProperty("user.home");
        TMP = System.getProperty("java.io.tmpdir", USER_HOME);
    }

    public static void main(String... args) throws Throwable {
        for (int i = 0; i < PATTERNS.length; i += 2) {
            String s = PATTERNS[i];
            String partial = PATTERNS[i + 1];
            System.out.println("generate: " + s);
            for (int[] gen : GENERATIONS) {
                String expected = generateExpected(s, partial, gen[0], gen[1], gen[2]);
                String output = generate(s, gen[0], gen[1], gen[2]).toString();
                System.out.println("\t" + Arrays.toString(gen) + ": " + output);
                if (!expected.equals(output)) {
                    throw new RuntimeException("test failed for \"" + s + "\" " + Arrays.toString(gen) + ": " + "\n\tgenerated: \"" + output + "\"" + "\n\t expected: \"" + expected + "\"");
                }
            }
        }
    }

    static String stripTrailingSeparator(String s) {
        if (s.endsWith("/")) {
            return s.substring(0, s.length() - 1);
        } else if (s.endsWith(File.separator)) {
            return s.substring(0, s.length() - File.separator.length());
        } else {
            return s;
        }
    }

    static String generateExpected(String s, String partial, int count, int generation, int unique) {
        boolean sawu = s.replace("%%", "$$$$").contains("%u");
        boolean sawg = s.replace("%%", "$$$$").contains("%g");
        String result = partial.replace("%%", "$$$$");
        String tmp = stripTrailingSeparator(TMP);
        String home = stripTrailingSeparator(USER_HOME);
        result = result.replace("%h", home);
        result = result.replace("%t", tmp);
        result = result.replace("%g", String.valueOf(generation));
        result = result.replace("%u", String.valueOf(unique));
        result = result.replace("$$$$", "%");
        result = result.replace("/", File.separator);
        if (count > 1 && !sawg) {
            result = result + "." + generation;
        }
        if (unique > 0 && !sawu) {
            result = result + "." + unique;
        }
        return result;
    }

    static File generate(String s, int count, int generation, int unique) throws Throwable {
        try {
            return (File) GENERATE.invoke(null, s, count, generation, unique);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
