import java.awt.datatransfer.DataFlavor;

public class HeadlessDataFlavor {

    public static void main(String[] args) throws Exception {
        DataFlavor df;
        df = new DataFlavor();
        df = new DataFlavor("text/plain", "plain jane text");
        df = new DataFlavor("text/html", "HTML text");
        df = new DataFlavor("text/plain");
        df = new DataFlavor("text/html");
        df.toString();
    }
}
