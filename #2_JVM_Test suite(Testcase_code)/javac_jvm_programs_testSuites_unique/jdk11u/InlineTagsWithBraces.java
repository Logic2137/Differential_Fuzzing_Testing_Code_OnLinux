



import com.sun.javadoc.*;


public class InlineTagsWithBraces extends Doclet {

    private static String[] expectedTags = {
        "Text", "@code", "Text",
        "@bold", "Text", "@code", "Text",
        "@maybe", "Text"
    };
    private static String[] expectedText = {
        "This is a ", "test", " comment.\n" +
        " It is ", "{@underline only} a test", ".\n" +
        " We would like some code\n" +
        " ", "for (int i : nums) { doit(i); } return; ", "\n" +
        " to be embedded ", "{even {a couple {of levels}}} deep", "."
    };


    public static void main(String[] args) {
        String thisFile = "" +
            new java.io.File(System.getProperty("test.src", "."),
                             "InlineTagsWithBraces.java");

        if (com.sun.tools.javadoc.Main.execute(
                "javadoc",
                "InlineTagsWithBraces",
                InlineTagsWithBraces.class.getClassLoader(),
                new String[] {"-Xwerror", thisFile}) != 0)
            throw new Error("Javadoc encountered warnings or errors.");
    }

    public static boolean start(RootDoc root) {
        ClassDoc cd = root.classes()[0];
        Tag[] tags = cd.inlineTags();

        for (int i = 0; i < tags.length; i++) {
            if (!tags[i].name().equals(expectedTags[i]) ||
                        !tags[i].text().equals(expectedText[i])) {
                throw new Error("Tag \"" + tags[i] + "\" not as expected");
            }
        }

        return true;
    }
}
