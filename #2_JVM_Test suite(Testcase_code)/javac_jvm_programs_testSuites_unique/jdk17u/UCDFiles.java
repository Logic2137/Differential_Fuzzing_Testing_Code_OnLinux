import java.nio.file.Path;
import java.nio.file.Paths;

public class UCDFiles {

    public static Path UCD_DIR = Paths.get(System.getProperty("test.root"), "..", "..", "make", "data", "unicodedata");

    public static Path BLOCKS = UCD_DIR.resolve("Blocks.txt");

    public static Path DERIVED_PROPS = UCD_DIR.resolve("DerivedCoreProperties.txt");

    public static Path GRAPHEME_BREAK_PROPERTY = UCD_DIR.resolve("auxiliary").resolve("GraphemeBreakProperty.txt");

    public static Path GRAPHEME_BREAK_TEST = UCD_DIR.resolve("auxiliary").resolve("GraphemeBreakTest.txt");

    public static Path NORMALIZATION_TEST = UCD_DIR.resolve("NormalizationTest.txt");

    public static Path PROP_LIST = UCD_DIR.resolve("PropList.txt");

    public static Path PROPERTY_VALUE_ALIASES = UCD_DIR.resolve("PropertyValueAliases.txt");

    public static Path SCRIPTS = UCD_DIR.resolve("Scripts.txt");

    public static Path SPECIAL_CASING = UCD_DIR.resolve("SpecialCasing.txt");

    public static Path UNICODE_DATA = UCD_DIR.resolve("UnicodeData.txt");

    public static Path EMOJI_DATA = UCD_DIR.resolve("emoji").resolve("emoji-data.txt");
}
