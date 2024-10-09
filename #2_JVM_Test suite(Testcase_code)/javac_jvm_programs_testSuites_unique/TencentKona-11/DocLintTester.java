

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.tools.doclint.DocLint;
import com.sun.tools.doclint.DocLint.BadArgs;

public class DocLintTester {

    public static void main(String... args) throws Exception {
        new DocLintTester().run(args);
    }

    public void run(String... args) throws Exception {
        String testSrc = System.getProperty("test.src");

        boolean badArgs = false;
        File refFile = null;
        List<String> opts = new ArrayList<String>();
        List<File> files = new ArrayList<File>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-ref")) {
                refFile = new File(testSrc, args[++i]);
            } else if (arg.equals("-badargs")) {
                badArgs = true;
            } else if (arg.startsWith("-Xmsgs")) {
                opts.add(arg);
            } else if (arg.startsWith("-XcustomTags")) {
                opts.add(arg);
            }  else if (arg.startsWith("-XhtmlVersion")) {
                opts.add(arg);
            } else if (arg.startsWith("-")) {
                opts.add(arg);
                if (i < args.length - 1 && !args[i+1].startsWith("-"))
                    opts.add(args[++i]);
            } else
                files.add(new File(testSrc, arg));
        }

        check(opts, files, badArgs, refFile);

        if (errors > 0)
            throw new Exception(errors + " errors occurred");
    }

    void check(List<String> opts, List<File> files, boolean expectBadArgs, File refFile) throws Exception {
        List<String> args = new ArrayList<String>();
        args.addAll(opts);
        for (File file: files)
            args.add(file.getPath());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            new DocLint().run(pw, args.toArray(new String[args.size()]));
            if (expectBadArgs)
                error("expected exception not thrown");
        } catch (BadArgs e) {
            if (!expectBadArgs)
                error("unexpected exception caught: " + e);
        }
        pw.flush();
        String out = normalizeNewlines(removeFileNames(sw.toString())).trim();
        if (out != null)
            System.err.println("Output:\n" + out);

        if (refFile == null) {
            if (!out.isEmpty())
                error("unexpected output");
        } else {
            String expect = readFile(refFile);
            if (!expect.equals(out)) {
                error("expected output not found");
                System.err.println("EXPECT>>" + expect + "<<");
                System.err.println(" FOUND>>" + out    + "<<");
            }
        }
    }

    String readFile(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        Reader in = new BufferedReader(new FileReader(file));
        try {
            char[] buf = new char[1024];
            int n;
            while ((n = in.read(buf)) != -1)
                sb.append(buf, 0, n);
        } finally {
            in.close();
        }
        return sb.toString().trim();
    }

    private static final Pattern dirFileLine = Pattern.compile(
            "(?m)"                          
            + "^(.*?)"                      
            + "([-A-Za-z0-9.]+:[0-9]+:)");  

    String removeFileNames(String s) {
        Matcher m = dirFileLine.matcher(s);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "$2");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private static final String nl = System.getProperty("line.separator");
    String normalizeNewlines(String s) {
        return (nl.equals("\n") ? s : s.replace(nl, "\n"));
    }


    void error(String msg) {
        System.err.println("Error: " + msg);
        errors++;
    }

    int errors;
}
