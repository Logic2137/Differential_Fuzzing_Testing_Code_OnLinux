

package nsk.share.gc;

import java.io.*;


public class Generator {

    
    private final static String[] TYPES = {"byte", "int", "long", "short",
                                           "boolean", "double", "float",
                                           "Object"};

    
    private final static String[] SIMPLE_MODIFIERS = {"static", "private",
                                                      "public", "protected",
                                                      "transient", "volatile",
                                                      "static", "public"};

    
    private final static String[] COMBINED_MODIFIERS = {"static", "public",
                                                       "protected", "transient",
                                                       "volatile"};

    
    private final static String EXT = ".java";
    private final static String PACKAGE = "package nsk.share.gc.newclass;\n";
    private final static String CLASS_BEGIN = "public class ";

    private final static String PARENT_SUFFIX = "parent";
    private final static String PARENT_FIELD_PREFIX = "p";
    private final static int SIMPLE_PARENT_FIELDS_NUM = 65500;
    private final static int COMBINED_PARENT_FIELDS_NUM = 32616;

    private final static String FIELD_NAME = "f";

    private final static String LCHILD_SUFFIX = "lchild";
    private final static int SIMPLE_LCHILD_FIELDS_NUM = 46;
    private final static int COMBINED_LCHILD_FIELDS_NUM = 33030;

    private final static String SCHILD_SUFFIX = "schild";
    private final static int SIMPLE_SCHILD_FIELDS_NUM = 35;
    private final static int COMBINED_SCHILD_FIELDS_NUM = 33018;

    
    public Generator() {}

    
    public static void main(String[] argv) throws FileNotFoundException {

        
        
        for (int i = 0; i < SIMPLE_MODIFIERS.length; i++)
            generateSimple(System.out, argv[0], TYPES[i], SIMPLE_MODIFIERS[i]);

        
        
        for (int i = 0; i < COMBINED_MODIFIERS.length; i++)
            generateCombined(System.out, argv[0], COMBINED_MODIFIERS[i]);
    }

    
    private static void generateSimple(PrintStream out, String dir, String type,
                                 String modifier) throws FileNotFoundException {

        
        String parentName = modifier + "_" + type + "_" + PARENT_SUFFIX;
        try (PrintWriter pw = getPrintWriter(dir, parentName + EXT)) {

            pw.println(PACKAGE);
            pw.println(CLASS_BEGIN + parentName + " {");

            
            
            String m = modifier;
            if (modifier.equals("private"))
                m = "public";
            for (int i = 0; i < SIMPLE_PARENT_FIELDS_NUM; i++)
                pw.println(m + " " + type + " "
                         + PARENT_FIELD_PREFIX + FIELD_NAME + (i + 1) + ";");
            pw.println("public Object objP = null;");
            pw.println("}");
        }

        
        generateSimpleChild(modifier + "_" + type + "_" + LCHILD_SUFFIX,
                            type, modifier, dir, parentName,
                            SIMPLE_LCHILD_FIELDS_NUM);
        generateSimpleChild(modifier + "_" + type + "_" + SCHILD_SUFFIX,
                            type, modifier, dir, parentName,
                            SIMPLE_SCHILD_FIELDS_NUM);
    }

    
    private static void generateSimpleChild(String className, String type,
                                            String modifier, String dir,
                                            String parentName, int num)
                                                  throws FileNotFoundException {
        try (PrintWriter pw = getPrintWriter(dir, className + EXT)) {

            pw.println(PACKAGE);
            pw.println(CLASS_BEGIN + className + " extends " + parentName + " {");
            for (int i = 0; i < num; i++)
                pw.println(modifier + " " + type + " " + FIELD_NAME + (i + 1)
                         + ";");
            pw.println("public Object objC = null;");
            pw.println("}");
        }
    }

    
    private static void generateCombined(PrintStream out, String dir,
                                 String modifier) throws FileNotFoundException {

        
        String parentName = modifier + "_combination_" + PARENT_SUFFIX;
        try (PrintWriter pw = getPrintWriter(dir, parentName + EXT)){

            pw.println(PACKAGE);
            pw.println(CLASS_BEGIN + parentName + " {");
            String pattern = PARENT_FIELD_PREFIX + FIELD_NAME;
            for (int i = 0; i < COMBINED_PARENT_FIELDS_NUM; ) {
                for (int j = 0; j < TYPES.length; j++) {
                    pw.println(modifier + " " + TYPES[j] + " "
                             + pattern + (i + 1) + ", "
                             + pattern + (i + 2) + "[], "
                             + pattern + (i + 3) + "[][];");
                    i = i + 3;
                }
            }
            pw.println("public Object objP = null;");
            pw.println("}");
        }

        
        generateCombinedChild(modifier + "_combination_" + LCHILD_SUFFIX,
                              modifier, dir, parentName,
                              COMBINED_LCHILD_FIELDS_NUM);
        generateCombinedChild(modifier + "_combination_" + SCHILD_SUFFIX,
                              modifier, dir, parentName,
                              COMBINED_SCHILD_FIELDS_NUM);
    }

    
    private static void generateCombinedChild(String className, String modifier,
                                              String dir, String parentName,
                                              int num)
                                                  throws FileNotFoundException {
        try (PrintWriter pw = getPrintWriter(dir, className + EXT)) {

            pw.println(PACKAGE);
            pw.println(CLASS_BEGIN + className + " extends " + parentName + " {");
            for (int i = 0; i < num; )
                for (int j = 0; j < TYPES.length; j++) {
                    pw.println(modifier + " " + TYPES[j] + " "
                             + FIELD_NAME + (i + 1) + ", "
                             + FIELD_NAME + (i + 2) + "[], "
                             + FIELD_NAME + (i + 3) + "[][];");
                    i = i + 3;
                    if (i >= num)
                        break;
                }
            pw.println("public Object objC = null;");
            pw.println("}");
        }
    }

    
    private static PrintWriter getPrintWriter(String dir, String name)
                                                  throws FileNotFoundException {
        FileOutputStream stream = new FileOutputStream(new File(dir, name));
        return new PrintWriter(stream, true);
    }
}
