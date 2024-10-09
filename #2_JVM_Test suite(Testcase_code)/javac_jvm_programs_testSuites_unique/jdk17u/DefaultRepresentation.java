import java.awt.datatransfer.DataFlavor;
import java.io.InputStream;

public final class DefaultRepresentation extends InputStream {

    public static void main(String[] args) {
        DataFlavor df = new DataFlavor(DefaultRepresentation.class, "stream");
        if (df.getDefaultRepresentationClass() != InputStream.class) {
            throw new RuntimeException("InputStream class is expected");
        }
        if (!df.isRepresentationClassInputStream()) {
            throw new RuntimeException("true is expected");
        }
    }

    @Override
    public int read() {
        return 0;
    }
}
