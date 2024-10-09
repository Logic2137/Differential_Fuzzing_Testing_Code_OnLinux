

import java.io.*;
import java.util.*;
import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;


public abstract class JavapTester {

    private static boolean debug = false;
    private static final PrintStream out = System.err;
    private static final PrintStream err = System.err;


    protected void run(String... args) throws Exception {

        final File classesdir = new File(System.getProperty("test.classes", "."));

        String[] classNames = args;

        
        
        if (args.length == 0) {
            final String pattern =  ".*\\.class";
            final File classFiles[] = classesdir.listFiles(new FileFilter() {
                    public boolean accept(File f) {
                        return f.getName().matches(pattern);
                    }
                });
            ArrayList<String> names = new ArrayList<String>(classFiles.length);
            for (File f : classFiles) {
                String fname = f.getName();
                names.add(fname.substring(0, fname.length() -6));
            }
            classNames = names.toArray(new String[names.size()]);
        } else {
            debug = true;
        }
        
        
        
        for (String clname : classNames) {
            try {
                final Class tclass = Class.forName(clname);
                if  (!getClass().isAssignableFrom(tclass)) continue;
                TestCase anno = (TestCase) tclass.getAnnotation(TestCase.class);
                if (anno == null) continue;
                if (!debug) {
                    ignore i = (ignore) tclass.getAnnotation(ignore.class);
                    if (i != null) {
                        out.println("Ignore: " + clname);
                        ignored++;
                        continue;
                    }
                }
                out.println("TestCase: " + clname);
                cases++;
                JavapTester tc = (JavapTester) tclass.getConstructor().newInstance();
                if (tc.errors > 0) {
                    error("" + tc.errors + " test points failed in " + clname);
                    errors += tc.errors - 1;
                    fcases++;
                }
            } catch(ReflectiveOperationException roe) {
                error("Warning: " + clname + " - ReflectiveOperationException");
                roe.printStackTrace(err);
            } catch(Exception unknown) {
                error("Warning: " + clname + " - uncaught exception");
                unknown.printStackTrace(err);
            }
        }

        String imsg = ignored > 0 ? " (" +  ignored + " ignored)" : "";
        if (errors > 0)
            throw new Error(errors + " error, in " + fcases + " of " + cases + " test-cases" + imsg);
        else
            err.println("" + cases + " test-cases executed" + imsg + ", no errors");
    }


    
    @Retention(RetentionPolicy.RUNTIME)
    @interface TestCase { }

    
    @Retention(RetentionPolicy.RUNTIME)
    @interface ignore { }

    
    public JavapTester() {
        this.testCase = this.getClass().getName();
        src = new TestSource("TESTCASE");
    }

    
    protected JavapTester setSrc(TestSource src) {
        this.src = src;
        return this;
    }

    
    protected JavapTester setSrc(String... lines) {
        return innerSrc("TESTCASE", lines);
    }

    
    protected JavapTester innerSrc(String key, String... lines) {
        return innerSrc(key, new TestSource(lines));
    }

    
    protected JavapTester innerSrc(String key, TestSource content) {
        if (src == null) {
            src = new TestSource(key);
        }
        src.setInner(key, content);
        return this;
    }

    
    protected void verify(String... expect) {
        if (!didExecute) {
            try {
                execute();
            } catch(Exception ue) {
                throw new Error(ue);
            } finally {
                didExecute = true;
            }
        }
        if (output == null) {
            error("output is null");
            return;
        }
        for (String e: expect) {
            
            
            
            String rc[] = { "(", ")", "[", "]", "{", "}", "$" };
            for (String c : rc) {
                e = e.replace(c, "\\" + c);
            }
            
            
            if (!output.matches("(?s).*" + e + ".*")) {
                if (!didPrint) {
                    out.println(output);
                    didPrint = true;
                }
                error("not matched: '" + e + "'");
            } else if(debug) {
                out.println("matched: '" + e + "'");
            }
        }
    }

    
    protected void execute() throws IOException {
        err.println("TestCase: " + testCase);
        writeTestFile();
        compileTestFile();
        process();
    }

    
    protected void writeTestFile() throws IOException {
        javaFile = new File("Test.java");
        FileWriter fw = new FileWriter(javaFile);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);
        for (String line : src) {
            pw.println(line);
            if (debug) out.println(line);
        }
        pw.close();
    }

    
    protected void compileTestFile() {
        String path = javaFile.getPath();
        String params[] =  {"-g", path };
        int rc = com.sun.tools.javac.Main.compile(params);
        if (rc != 0)
            throw new Error("compilation failed. rc=" + rc);
        classFile = new File(path.substring(0, path.length() - 5) + ".class");
    }


    
    protected void process() {
        String testClasses = "."; 
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        String[] args = { "-v", "-classpath", testClasses, "Test" };
        int rc = com.sun.tools.javap.Main.run(args, pw);
        if (rc != 0)
            throw new Error("javap failed. rc=" + rc);
        pw.close();
        output = sw.toString();
        if (debug) {
            out.println(output);
            didPrint = true;
        }

    }


    private String testCase;
    private TestSource src;
    private File javaFile = null;
    private File classFile = null;
    private String output = null;
    private boolean didExecute = false;
    private boolean didPrint = false;


    protected void error(String msg) {
        err.println("Error: " + msg);
        errors++;
    }

    private int cases;
    private int fcases;
    private int errors;
    private int ignored;

    
    public class TestSource implements Iterable<String> {

        private String[] lines;
        private Hashtable<String, TestSource> innerSrc;

        public TestSource(String... lines) {
            this.lines = lines;
            innerSrc = new Hashtable<String, TestSource>();
        }

        public void setInner(String key, TestSource inner) {
            innerSrc.put(key, inner);
        }

        public void setInner(String key, String... lines) {
            innerSrc.put(key, new TestSource(lines));
        }

        public Iterator<String> iterator() {
            return new LineIterator();
        }

        private class LineIterator implements Iterator<String> {

            int nextLine = 0;
            Iterator<String> innerIt = null;

            public  boolean hasNext() {
                return nextLine < lines.length;
            }

            public String next() {
                if (!hasNext()) throw new NoSuchElementException();
                String str = lines[nextLine];
                TestSource inner = innerSrc.get(str);
                if (inner == null) {
                    nextLine++;
                    return str;
                }
                if (innerIt == null) {
                    innerIt = inner.iterator();
                }
                if (innerIt.hasNext()) {
                    return innerIt.next();
                }
                innerIt = null;
                nextLine++;
                return next();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }
}
