import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Test8030118 implements DocumentListener, Runnable {

    private final CountDownLatch latch = new CountDownLatch(1);

    private final PlainDocument doc = new PlainDocument();

    private Test8030118(String string) throws Exception {
        this.doc.addDocumentListener(this);
        this.doc.insertString(0, string, null);
    }

    @Override
    public void run() {
        try {
            this.doc.remove(0, this.doc.getLength());
        } catch (BadLocationException exception) {
            throw new Error("unexpected", exception);
        }
        this.latch.countDown();
    }

    @Override
    public void insertUpdate(DocumentEvent event) {
        new Thread(this).start();
        try {
            this.latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            throw new Error("unexpected", exception);
        }
        try {
            event.getDocument().getText(event.getOffset(), event.getLength());
        } catch (BadLocationException exception) {
            throw new Error("concurrent modification", exception);
        }
    }

    @Override
    public void removeUpdate(DocumentEvent event) {
    }

    @Override
    public void changedUpdate(DocumentEvent event) {
    }

    public static void main(String[] args) throws Exception {
        new Test8030118("string");
    }
}
