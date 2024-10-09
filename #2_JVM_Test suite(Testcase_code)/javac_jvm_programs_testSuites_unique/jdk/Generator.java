

package nsk.monitoring.share;

import java.util.*;
import java.io.*;
import java.util.ArrayList;

public class Generator {
        private String patternFileName;
        private String outDir;
        private List<String> lines = new ArrayList<>();
        private String prefix = "LoadableClass";

        public Generator(String patternFileName, String outDir) {
                this.patternFileName = patternFileName;
                this.outDir = outDir;
        }

        private void load() throws IOException {
                BufferedReader rd = null;
                try {
                        rd = new BufferedReader(new FileReader(patternFileName));
                        String s = rd.readLine();
                        while (s != null) {
                                lines.add(s);
                                s = rd.readLine();
                        }
                } finally {
                        if (rd != null) {
                                rd.close();
                        }
                }
        }

        private void generate(int n) throws IOException {
                PrintStream out = null;
                String tokens = Integer.toString(n);
                if (tokens.length() == 1)
                        tokens = "00" + tokens;
                else if (tokens.length() == 2)
                        tokens = "0" + tokens;
                try {
                        out = new PrintStream(new FileOutputStream(new File(outDir, prefix + tokens + ".java")));
                        for (int i = 0; i < lines.size(); ++i) {
                                String line = lines.get(i);
                                out.println(line.replaceAll("XYZ", tokens));
                        }
                } finally {
                        if (out != null) {
                                out.close();
                        }
                }
        }

        public void run() throws IOException {
                load();
                for (int i = 1; i < 101; ++i)
                        generate(i);
        }

        private static void usage() {
                System.out.println("Usage: nsk.monitoring.share.Generator <pattern file> <output directory>");
        }

        public static void main(String[] args) throws Exception {
                if (args.length != 2) {
                        usage();
                        throw new IllegalArgumentException("Need exactly two arguments.");
                }
                new Generator(args[0], args[1]).run();
        }
}
