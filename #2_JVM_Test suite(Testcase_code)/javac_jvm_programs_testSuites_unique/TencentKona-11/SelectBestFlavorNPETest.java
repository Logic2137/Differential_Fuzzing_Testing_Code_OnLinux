



import java.awt.datatransfer.DataFlavor;

public class SelectBestFlavorNPETest {

    public static void main(String[] args) {

        DataFlavor flavor1 = new DataFlavor("text/plain; charset=unicode; class=java.io.InputStream",
                "Flavor 1");
        DataFlavor flavor2 = new DataFlavor("text/plain; class=java.io.InputStream", "Flavor 2");
        DataFlavor[] flavors = new DataFlavor[]{flavor1, flavor2};
        try {
            DataFlavor best = DataFlavor.selectBestTextFlavor(flavors);
            System.out.println("best=" + best);
        } catch (NullPointerException e1) {
            throw new RuntimeException("Test FAILED because of NPE in selectBestTextFlavor");
        }
    }

}
