



import java.lang.Character.UnicodeBlock;
import java.lang.reflect.Field;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Locale;

public class CheckBlocks {

    static boolean err = false;
    static Class<?> clazzUnicodeBlock;

    public static void main(String[] args) throws Exception {
        generateBlockList();

        try {
            clazzUnicodeBlock = Class.forName("java.lang.Character$UnicodeBlock");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class.forName(\"java.lang.Character$UnicodeBlock\") failed.");
        }

        for (Block blk : blocks) {
            test4830803_1(blk);
            test4830803_2();
            test4886934(blk);
        }

        test8202771();

        if (err) {
            throw new RuntimeException("Failed");
        } else {
            System.out.println("Passed");
        }
    }

    
    private static void test4830803_1(Block blk) throws Exception {

        
        String blkName = blk.getName();

        
        switch (blkName) {
            case "COMBINING_DIACRITICAL_MARKS_FOR_SYMBOLS":
                blkName = "COMBINING_MARKS_FOR_SYMBOLS";
                System.out.println("*** COMBINING_DIACRITICAL_MARKS_FOR_SYMBOLS"
                        + " is replaced with COMBINING_MARKS_FOR_SYMBOLS"
                        + " for backward compatibility.");
                break;
            case "GREEK_AND_COPTIC":
                blkName = "GREEK";
                System.out.println("*** GREEK_AND_COPTIC is replaced with GREEK"
                        + " for backward compatibility.");
                break;
            case "CYRILLIC_SUPPLEMENT":
                blkName = "CYRILLIC_SUPPLEMENTARY";
                System.out.println("*** CYRILLIC_SUPPLEMENT is replaced with"
                        + " CYRILLIC_SUPPLEMENTARY for backward compatibility.");
                break;
            default:
                break;
        }

        String expectedBlock = null;
        try {
            expectedBlock = clazzUnicodeBlock.getField(blkName).getName();
        } catch (NoSuchFieldException | SecurityException e) {
            System.err.println("Error: " + blkName + " was not found.");
            err = true;
            return;
        }

        String canonicalBlockName = blk.getOriginalName();
        String idBlockName = expectedBlock;
        String regexBlockName = toRegExString(canonicalBlockName);

        if (regexBlockName == null) {
            System.err.println("Error: Block name which was processed with regex was null.");
            err = true;
            return;
        }

        if (!expectedBlock.equals(UnicodeBlock.forName(canonicalBlockName).toString())) {
            System.err.println("Error #1: UnicodeBlock.forName(\"" +
                    canonicalBlockName + "\") returned wrong value.\n\tGot: " +
                    UnicodeBlock.forName(canonicalBlockName) +
                    "\n\tExpected: " + expectedBlock);
            err = true;
        }

        if (!expectedBlock.equals(UnicodeBlock.forName(idBlockName).toString())) {
            System.err.println("Error #2: UnicodeBlock.forName(\"" +
                    idBlockName + "\") returned wrong value.\n\tGot: " +
                    UnicodeBlock.forName(idBlockName) +
                    "\n\tExpected: " + expectedBlock);
            err = true;
        }

        if (!expectedBlock.equals(UnicodeBlock.forName(regexBlockName).toString())) {
            System.err.println("Error #3: UnicodeBlock.forName(\"" +
                    regexBlockName + "\") returned wrong value.\n\tGot: " +
                    UnicodeBlock.forName(regexBlockName) +
                    "\n\tExpected: " + expectedBlock);
            err = true;
        }
    }

    
    private static void test4830803_2() {
        boolean threwExpected = false;

        try {
            UnicodeBlock block = UnicodeBlock.forName("notdefined");
        }
        catch(IllegalArgumentException e) {
            threwExpected = true;
        }

        if (threwExpected == false) {
            System.err.println("Error: UnicodeBlock.forName(\"notdefined\") should throw IllegalArgumentException.");
            err = true;
        }
    }

    
    private static String toRegExString(String str) {
        String[] tokens = null;
        StringBuilder retStr = new StringBuilder();
        try {
            tokens = str.split(" ");
        }
        catch(java.util.regex.PatternSyntaxException e) {
            return null;
        }
        for(int x=0; x < tokens.length; ++x) {
            retStr.append(tokens[x]);
        }
        return retStr.toString();
    }

    private static void test4886934(Block blk) {
        String blkName = blk.getName();
        String blkOrigName = blk.getOriginalName();
        UnicodeBlock block;
        String blockName;

        
        switch (blkName) {
            case "COMBINING_DIACRITICAL_MARKS_FOR_SYMBOLS":
                blkName = "COMBINING_MARKS_FOR_SYMBOLS";
                System.out.println("*** COMBINING_DIACRITICAL_MARKS_FOR_SYMBOLS"
                        + " is replaced with COMBINING_MARKS_FOR_SYMBOLS"
                        + " for backward compatibility.");
                break;
            case "GREEK_AND_COPTIC":
                blkName = "GREEK";
                System.out.println("*** GREEK_AND_COPTIC is replaced with GREEK"
                        + " for backward compatibility.");
                break;
            case "CYRILLIC_SUPPLEMENT":
                blkName = "CYRILLIC_SUPPLEMENTARY";
                System.out.println("*** CYRILLIC_SUPPLEMENT is replaced with"
                        + " CYRILLIC_SUPPLEMENTARY for backward compatibility.");
                break;
            default:
                break;
        }

        for (int ch = blk.getBegin(); ch <= blk.getEnd(); ch++) {
            block = UnicodeBlock.of(ch);
            if (block == null) {
                System.err.println("Error: The block for " + blkName
                        + " is missing. Please check java.lang.Character.UnicodeBlock.");
                err = true;
                break;
            }
            blockName = block.toString();
            if (!blockName.equals(blkName)) {
                System.err.println("Error: Character(0x"
                        + Integer.toHexString(ch).toUpperCase()
                        + ") should be in \"" + blkName + "\" block "
                        + "(Block name is \"" + blkOrigName + "\")"
                        + " but found in \"" + blockName + "\" block.");
                err = true;
            }
        }
    }

    
    private static void test8202771() {
        Field[] fields = clazzUnicodeBlock.getFields();

        for (Field f : fields) {
            
            if (f.getAnnotation(Deprecated.class) != null) {
                continue;
            }

            String blkName = f.getName();
            switch (blkName) {
                case "COMBINING_MARKS_FOR_SYMBOLS":
                    validateBlock("COMBINING_DIACRITICAL_MARKS_FOR_SYMBOLS");
                    break;
                case "GREEK":
                    validateBlock("GREEK_AND_COPTIC");
                    break;
                case "CYRILLIC_SUPPLEMENTARY":
                    validateBlock("CYRILLIC_SUPPLEMENT");
                    break;
                default:
                    validateBlock(blkName);
                    break;
            }
        }
    }

    private static void validateBlock(String blkName) {
        for (Block block : blocks) {
            String blockName = block.getName();
            if (blockName.equals(blkName)) {
                return;
            }
        }
        err = true;
        System.err.println(blkName + " is not a valid Unicode Block.");
    }

    
    public static HashSet<Block> blocks = new HashSet<>();

    private static void generateBlockList() throws Exception {
        File blockData = new File(System.getProperty("test.src", "."),
                "Blocks.txt");
        try (BufferedReader f = new BufferedReader(new FileReader(blockData))) {
            String line;
            while ((line = f.readLine()) != null) {
                if (line.length() == 0 || line.charAt(0) == '#') {
                    continue;
                }

                int index1 = line.indexOf('.');
                int begin = Integer.parseInt(line.substring(0, index1), 16);
                int index2 = line.indexOf(';');
                int end = Integer.parseInt(line.substring(index1 + 2, index2), 16);
                String name = line.substring(index2 + 1).trim();

                System.out.println("  Adding a Block(" + Integer.toHexString(begin) + ", " + Integer.toHexString(end)
                        + ", " + name + ")");
                blocks.add(new Block(begin, end, name));
            }
        }
    }
}

class Block {

    public Block() {
        blockBegin = 0;
        blockEnd = 0;
        blockName = null;
    }

    public Block(int begin, int end, String name) {
        blockBegin = begin;
        blockEnd = end;
        blockName = name.replaceAll("[ -]", "_").toUpperCase(Locale.ENGLISH);
        originalBlockName = name;
    }

    public int getBegin() {
        return blockBegin;
    }

    public int getEnd() {
        return blockEnd;
    }

    public String getName() {
        return blockName;
    }

    public String getOriginalName() {
        return originalBlockName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Block)) return false;

        Block other = (Block)obj;
        return other.blockBegin == blockBegin &&
                other.blockEnd == blockEnd &&
                other.blockName.equals(blockName) &&
                other.originalBlockName.equals(originalBlockName);
    }
    int blockBegin, blockEnd;
    String blockName, originalBlockName;
}
