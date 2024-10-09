

import java.util.Arrays;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.RootDoc;

@SuppressWarnings("deprecation")
public class ToyDoclet {

    public static boolean start(RootDoc root) {
        String whoami = "I am a toy doclet";
        root.printNotice("Notice: " + whoami);
        boolean status = false;
        for (ClassDoc cls : root.classes()) {
            if (!status) status = true;
            root.printNotice("Classes: " + cls);
            printClassMembers(root, cls);
        }
        for (ClassDoc cls : root.specifiedClasses()) {
            if (!status) status = true;
            root.printNotice("Specified-classes: " + cls);
            printClassMembers(root, cls);
        }
        for (PackageDoc pkg : root.specifiedPackages()) {
            if (!status) status = true;
            root.printNotice("Specified-packages: " + pkg);
        }
        return status;
    }

    static void printClassMembers(RootDoc root, ClassDoc cls) {
        root.printNotice("Members for: " + cls);
        root.printNotice("  extends " + Arrays.asList(cls.superclass()));
        root.printNotice("  Fields: ");
        printMembers(root, cls.fields());
        root.printNotice("  Constructor: ");
        printMembers(root, cls.constructors());
        root.printNotice("  Method: ");
        printMembers(root, cls.methods());
        if (cls.superclass() != null && !cls.superclassType().toString().equals("java.lang.Object"))
            printClassMembers(root, cls.superclass());
    }

    static void printMembers(RootDoc root, ProgramElementDoc[] pgmDocs) {
        for (ProgramElementDoc pgmDoc : pgmDocs) {
            root.printNotice("     " + pgmDoc + ", Comments: " + pgmDoc.getRawCommentText());
        }
    }

    public static int optionLength(String option) {
        System.out.println("option: " + option);
        return 0;  
    }
}
