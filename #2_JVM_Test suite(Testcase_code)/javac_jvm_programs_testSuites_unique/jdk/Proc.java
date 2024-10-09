

package jdk.test.lib.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Permission;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;


public class Proc {
    private Process p;
    private BufferedReader br;      
    private String launcher;        

    private List<String> args = new ArrayList<>();
    private Map<String,String> env = new HashMap<>();
    private Map<String,String> prop = new HashMap();
    private Map<String,String> secprop = new HashMap();
    private boolean inheritIO = false;
    private boolean noDump = false;

    private List<String> cp;        
    private String clazz;           
    private String debug;           
                                    
                                    

    final private static String PREFIX = "PROCISFUN:";

    
    final private StringBuilder perms = new StringBuilder();
    
    final private StringBuilder grant = new StringBuilder();

    

    
    
    public static Proc create(String clazz, String... launcher) {
        Proc pc = new Proc();
        pc.clazz = clazz;
        if (launcher.length > 0) {
            pc.launcher = launcher[0];
        }
        return pc;
    }
    
    
    
    public Proc inheritIO() {
        inheritIO = true;
        return this;
    }
    
    public Proc nodump() {
        noDump = true;
        return this;
    }
    
    public Proc args(String... args) {
        for (String c: args) {
            this.args.add(c);
        }
        return this;
    }
    
    public String debug() {
        return debug;
    }
    
    public Proc debug(String title) {
        debug = title;
        return this;
    }
    
    public Proc env(String a, String b) {
        env.put(a, b);
        return this;
    }
    
    public Proc prop(String a, String b) {
        prop.put(a, b);
        return this;
    }
    
    public Proc secprop(String a, String b) {
        secprop.put(a, b);
        return this;
    }
    
    public Proc inheritProp(String k) {
        String v = System.getProperty(k);
        if (v != null) {
            prop.put(k, v);
        }
        return this;
    }
    
    
    public Proc cp(String... s) {
        if (cp == null) {
            cp = new ArrayList<>();
        }
        cp.addAll(Arrays.asList(s));
        return this;
    }
    
    
    
    
    
    
    public Proc perm(Permission p) {
        if (grant.length() != 0) {      
            if (perms.length() != 0) {  
                perms.append("};\n");
            }
            perms.append("grant ").append(grant).append(" {\n");
            grant.setLength(0);
        } else {
            if (perms.length() == 0) {  
                perms.append("grant {\n");
            }
        }
        if (p.getActions().isEmpty()) {
            String s = String.format("%s \"%s\"",
                    p.getClass().getCanonicalName(),
                    p.getName()
                            .replace("\\", "\\\\").replace("\"", "\\\""));
            perms.append("    permission ").append(s).append(";\n");
        } else {
            String s = String.format("%s \"%s\", \"%s\"",
                    p.getClass().getCanonicalName(),
                    p.getName()
                            .replace("\\", "\\\\").replace("\"", "\\\""),
                    p.getActions());
            perms.append("    permission ").append(s).append(";\n");
        }
        return this;
    }

    
    
    

    
    public Proc grant(Principal p) {
        grant.append("principal ").append(p.getClass().getName())
                .append(" \"").append(p.getName()).append("\", ");
        return this;
    }
    
    public Proc grant(File f) {
        grant.append("codebase \"").append(f.toURI()).append("\", ");
        return this;
    }
    
    public Proc grant(String v) {
        grant.append(v).append(", ");
        return this;
    }
    
    public Proc start() throws IOException {
        List<String> cmd = new ArrayList<>();
        if (launcher != null) {
            cmd.add(launcher);
            File base = new File(launcher).getParentFile().getParentFile();
        } else {
            cmd.add(new File(new File(System.getProperty("java.home"), "bin"),
                        "java").getPath());
        }

        String testModules = System.getProperty("test.modules");
        if (testModules != null) {
            for (String module : testModules.split("\\s+")) {
                if (module.endsWith(":+open")) {
                    String realModule = module.substring(0, module.length() - 6);
                    cmd.add("--add-opens=" + realModule + "=ALL-UNNAMED");
                } else if (module.contains("/")) {
                    cmd.add("--add-exports=" + module + "=ALL-UNNAMED");
                }
            }
        }

        Collections.addAll(cmd, splitProperty("test.vm.opts"));
        Collections.addAll(cmd, splitProperty("test.java.opts"));

        if (cp == null) {
            cmd.add("-cp");
            cmd.add(System.getProperty("test.class.path") + File.pathSeparator +
                    System.getProperty("test.src.path"));
        } else if (!cp.isEmpty()) {
            cmd.add("-cp");
            cmd.add(cp.stream().collect(Collectors.joining(File.pathSeparator)));
        }

        if (!secprop.isEmpty()) {
            Path p = Path.of(getId("security"));
            try (OutputStream fos = Files.newOutputStream(p);
                 PrintStream ps = new PrintStream(fos)) {
                secprop.forEach((k,v) -> ps.println(k + "=" + v));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            prop.put("java.security.properties", p.toString());
        }

        for (Entry<String,String> e: prop.entrySet()) {
            cmd.add("-D" + e.getKey() + "=" + e.getValue());
        }
        if (perms.length() > 0) {
            Path p = Paths.get(getId("policy")).toAbsolutePath();
            perms.append("};\n");
            Files.write(p, perms.toString().getBytes());
            cmd.add("-Djava.security.policy=" + p.toString());
        }
        cmd.add(clazz);
        for (String s: args) {
            cmd.add(s);
        }
        if (debug != null) {
            System.out.println("PROC: " + debug + " cmdline: " + cmd);
            for (String e : env.keySet()) {
                System.out.print(e + "=" + env.get(e) + " ");
            }
            for (String c : cmd) {
                if (c.indexOf('\\') >= 0 || c.indexOf(' ') > 0) {
                    System.out.print('\'' + c + '\'');
                } else {
                    System.out.print(c);
                }
                System.out.print(' ');
            }
            System.out.println();
        }
        ProcessBuilder pb = new ProcessBuilder(cmd);
        for (Entry<String,String> e: env.entrySet()) {
            pb.environment().put(e.getKey(), e.getValue());
        }
        if (inheritIO) {
            pb.inheritIO();
        } else if (noDump) {
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        } else {
            pb.redirectError(ProcessBuilder.Redirect
                    .appendTo(new File(getId("stderr"))));
        }
        p = pb.start();
        br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        return this;
    }
    String getId(String suffix) {
        if (debug != null) {
            return debug + "." + suffix;
        } else {
            return System.identityHashCode(this) + "." + suffix;
        }
    }
    
    public String readLine() throws IOException {
        String s = br.readLine();
        if (debug != null) {
            System.out.println("PROC: " + debug + " readline: " +
                    (s == null ? "<EOF>" : s));
        }
        return s;
    }
    
    public String readData() throws Exception {
        while (true) {
            String s = readLine();
            if (s == null) {
                if (p.waitFor() != 0) {
                    throw new Exception("Proc abnormal end");
                } else {
                    return s;
                }
            }
            if (s.startsWith(PREFIX)) {
                return s.substring(PREFIX.length());
            }
        }
    }
    
    public void println(String s) throws IOException {
        if (debug != null) {
            System.out.println("PROC: " + debug + " println: " + s);
        }
        write((s + "\n").getBytes());
    }
    
    public void write(byte[] b) throws IOException {
        p.getOutputStream().write(b);
        p.getOutputStream().flush();
    }
    
    public int waitFor() throws Exception {
        while (true) {
            String s = readLine();
            if (s == null) {
                break;
            }
        }
        return p.waitFor();
    }
    
    public void waitFor(int expected) throws Exception {
        if (p.waitFor() != expected) {
            throw new RuntimeException("Exit code not " + expected);
        }
    }

    

    
    public static void binOut(byte[] data) {
        System.out.println(PREFIX + Base64.getEncoder().encodeToString(data));
    }
    
    public static byte[] binIn() throws Exception {
        return Base64.getDecoder().decode(textIn());
    }
    
    public static void textOut(String data) {
        System.out.println(PREFIX + data);
    }
    
    public static String textIn() throws Exception {
        StringBuilder sb = new StringBuilder();
        boolean isEmpty = true;
        while (true) {
            int i = System.in.read();
            if (i == -1) {
                break;
            }
            isEmpty = false;
            if (i == '\n') {
                break;
            }
            if (i != 13) {
                
                sb.append((char)i);
            }
        }
        return isEmpty ? null : sb.toString();
    }
    
    
    public static void d(String s) throws IOException {
        System.err.println(s);
    }
    
    public static void d(Throwable e) throws IOException {
        e.printStackTrace();
    }

    private static String[] splitProperty(String prop) {
        String s = System.getProperty(prop);
        if (s == null || s.trim().isEmpty()) {
            return new String[] {};
        }
        return s.trim().split("\\s+");
    }
}
