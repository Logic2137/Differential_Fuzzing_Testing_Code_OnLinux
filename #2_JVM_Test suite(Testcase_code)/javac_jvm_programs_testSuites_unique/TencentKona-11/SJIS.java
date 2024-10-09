



import java.io.*;


public class SJIS {

    private static void rm(File f) {
        if (!f.delete()) throw new RuntimeException("Can't delete " + f);
    }

    private static void touch(File f) throws IOException {
        OutputStream o = new FileOutputStream(f);
        o.close();
    }

    public static void main(String[] args) throws Exception {

        
        if (File.separatorChar != '\\') return;
        String enc = System.getProperty("file.encoding");
        if ((enc == null) || !enc.equals("SJIS")) return;

        File f = new File("\u30BD");
        if (f.exists()) rm(f);

        System.err.println(f.getCanonicalPath());
        touch(f);
        System.err.println(f.getCanonicalPath());

        rm(f);

        if (!f.mkdir()) {
            throw new Exception("Can't create directory " + f);
        }
        File f2 = new File(f, "\u30BD");
        System.err.println(f2.getCanonicalPath());
        touch(f2);
        String cfn = f2.getCanonicalPath();
        if (!(new File(cfn)).exists()) {
            throw new Exception(cfn + " not found");
        }

        File d = new File(".");
        String[] fs = d.list();
        if (fs == null) System.err.println("No files listed");
        for (int i = 0; i < fs.length; i++) {
            System.err.println(fs[i]);
        }

    }

}
