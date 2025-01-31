

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import javax.tools.*;

import com.sun.tools.classfile.*;
import com.sun.tools.classfile.Dependencies.*;
import com.sun.tools.classfile.Dependency.Location;
import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.util.Context;


public class GetDeps {
    public static void main(String... args) throws Exception {
        new GetDeps().run(args);
    }

    void run(String... args) throws IOException, ClassFileNotFoundException {
        PrintWriter pw = new PrintWriter(System.out);
        try {
            run(pw, args);
        } finally {
            pw.flush();
        }
    }

    void run(PrintWriter out, String... args) throws IOException, ClassFileNotFoundException {
        decodeArgs(args);

        final StandardJavaFileManager fm = new JavacFileManager(new Context(), false, null);
        if (classpath != null)
            fm.setLocation(StandardLocation.CLASS_PATH, classpath);

        ClassFileReader reader = new ClassFileReader(fm);

        Dependencies d = new Dependencies();

        if (regex != null)
            d.setFilter(Dependencies.getRegexFilter(Pattern.compile(regex)));

        if (packageNames.size() > 0)
            d.setFilter(Dependencies.getPackageFilter(packageNames, false));

        SortedRecorder r = new SortedRecorder(reverse);

        d.findAllDependencies(reader, rootClassNames, transitiveClosure, r);

        SortedMap<Location,SortedSet<Dependency>> deps = r.getMap();
        for (Map.Entry<Location, SortedSet<Dependency>> e: deps.entrySet()) {
            out.println(e.getKey());
            for (Dependency dep: e.getValue()) {
                out.println("    " + dep.getTarget());
            }
        }
    }

    void decodeArgs(String... args) {
        rootClassNames = new TreeSet<String>();
        packageNames = new TreeSet<String>();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-classpath") && (i + 1 < args.length))
                classpath = getPathFiles(args[++i]);
            else if (arg.equals("-p") && (i + 1 < args.length))
                packageNames.add(args[++i]);
            else if (arg.equals("-r") && (i + 1 < args.length))
                regex = args[++i];
            else if (arg.equals("-rev"))
                reverse = true;
            else if (arg.equals("-t"))
                transitiveClosure = true;
            else if (arg.startsWith("-"))
                throw new Error(arg);
            else {
                for ( ; i < args.length; i++)
                    rootClassNames.add(args[i]);
            }
        }
    }

    List<File> getPathFiles(String path) {
        List<File> files = new ArrayList<File>();
        for (String p: path.split(File.pathSeparator)) {
            if (p.length() > 0)
                files.add(new File(p));
        }
        return files;
    }

    boolean transitiveClosure;
    List<File> classpath;
    Set<String> rootClassNames;
    Set<String> packageNames;
    String regex;
    boolean reverse;


    static class ClassFileReader implements Dependencies.ClassFileReader {
        private JavaFileManager fm;

        ClassFileReader(JavaFileManager fm) {
            this.fm = fm;
        }

        @Override
        public ClassFile getClassFile(String className) throws ClassFileNotFoundException {
            try {
                JavaFileObject fo = fm.getJavaFileForInput(
                        StandardLocation.CLASS_PATH, className, JavaFileObject.Kind.CLASS);
                if (fo == null)
                    fo = fm.getJavaFileForInput(
                        StandardLocation.PLATFORM_CLASS_PATH, className, JavaFileObject.Kind.CLASS);
                if (fo == null)
                    throw new ClassFileNotFoundException(className);
                InputStream in = fo.openInputStream();
                try {
                    return ClassFile.read(in);
                } finally {
                    in.close();
                }
            } catch (ConstantPoolException e) {
                throw new ClassFileNotFoundException(className, e);
            } catch (IOException e) {
                throw new ClassFileNotFoundException(className, e);
            }
        }
    };

    static class SortedRecorder implements Recorder {
        public SortedRecorder(boolean reverse) {
            this.reverse = reverse;
        }

        public void addDependency(Dependency d) {
            Location o = (reverse ? d.getTarget() : d.getOrigin());
            SortedSet<Dependency> odeps = map.get(o);
            if (odeps == null) {
                Comparator<Dependency> c = (reverse ? originComparator : targetComparator);
                map.put(o, odeps = new TreeSet<Dependency>(c));
            }
            odeps.add(d);
        }

        public SortedMap<Location, SortedSet<Dependency>> getMap() {
            return map;
        }

        private Comparator<Dependency> originComparator = new Comparator<Dependency>() {
            public int compare(Dependency o1, Dependency o2) {
                return o1.getOrigin().toString().compareTo(o2.getOrigin().toString());
            }
        };

        private Comparator<Dependency> targetComparator = new Comparator<Dependency>() {
            public int compare(Dependency o1, Dependency o2) {
                return o1.getTarget().toString().compareTo(o2.getTarget().toString());
            }
        };

        private Comparator<Location> locationComparator = new Comparator<Location>() {
            public int compare(Location o1, Location o2) {
                return o1.toString().compareTo(o2.toString());
            }
        };

        private final SortedMap<Location, SortedSet<Dependency>> map =
                new TreeMap<Location, SortedSet<Dependency>>(locationComparator);

        boolean reverse;
    }

}
