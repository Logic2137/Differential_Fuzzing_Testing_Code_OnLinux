import java.awt.datatransfer.DataFlavor;

public class EqualsHashCodeSymmetryTest {

    private static final DataFlavor[] dataFlavors = { DataFlavor.stringFlavor, DataFlavor.imageFlavor, DataFlavor.javaFileListFlavor, DataFlavor.allHtmlFlavor, DataFlavor.selectionHtmlFlavor, DataFlavor.fragmentHtmlFlavor, createFlavor("text/html; class=java.lang.String"), new DataFlavor(String.class, "My test flavor number 1"), new DataFlavor(String.class, "My test flavor number 2"), new DataFlavor(StringBuilder.class, "My test flavor number 1") };

    public static void main(String[] args) {
        testEqualsSymmetry();
        testEqualsHashCodeConsistency();
        testSimpleCollision();
    }

    private static void testEqualsSymmetry() {
        for (DataFlavor flavor1 : dataFlavors) {
            for (DataFlavor flavor2 : dataFlavors) {
                if (flavor1.equals(flavor2) != flavor2.equals(flavor1)) {
                    throw new RuntimeException(String.format("Equals is not symmetric for %s and %s", flavor1, flavor2));
                }
            }
        }
    }

    private static void testEqualsHashCodeConsistency() {
        for (DataFlavor flavor1 : dataFlavors) {
            for (DataFlavor flavor2 : dataFlavors) {
                if ((flavor1.equals(flavor2) && flavor1.hashCode() != flavor2.hashCode())) {
                    throw new RuntimeException(String.format("Equals and hash code not consistent for %s and %s", flavor1, flavor2));
                }
            }
        }
    }

    private static void testSimpleCollision() {
        if (createFlavor("text/html; class=java.lang.String").hashCode() == DataFlavor.allHtmlFlavor.hashCode()) {
            throw new RuntimeException("HashCode collision because the document parameter is not used");
        }
    }

    private static DataFlavor createFlavor(String mime) {
        try {
            return new DataFlavor(mime);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
