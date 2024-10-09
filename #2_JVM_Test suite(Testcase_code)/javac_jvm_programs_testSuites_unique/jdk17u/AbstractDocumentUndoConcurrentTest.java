import javax.swing.text.PlainDocument;
import javax.swing.text.StringContent;
import javax.swing.undo.UndoManager;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.concurrent.CyclicBarrier;

public class AbstractDocumentUndoConcurrentTest {

    static CyclicBarrier barrier = new CyclicBarrier(3);

    private static PlainDocument doc1;

    private static PlainDocument doc2;

    private static Format format1 = new DecimalFormat("<Test1 0000>");

    private static Format format2 = new DecimalFormat("<Test22 0000>");

    public static void main(String[] args) throws Exception {
        test();
        System.out.println(doc1.getText(0, doc1.getLength()));
        System.out.println(doc2.getText(0, doc2.getLength()));
        System.out.println("ok");
    }

    private static void test() throws Exception {
        doc1 = new PlainDocument(new StringContent());
        final UndoManager undoManager = new UndoManager();
        doc1.addUndoableEditListener(undoManager);
        doc1.insertString(0, "<Test1 XXXX>", null);
        doc2 = new PlainDocument(new StringContent());
        doc2.addUndoableEditListener(undoManager);
        doc2.insertString(0, "<Test22 XXXX>", null);
        Thread t1 = new Thread("Thread doc1") {

            @Override
            public void run() {
                try {
                    barrier.await();
                    for (int i = 0; i < 1000; i++) {
                        doc1.insertString(0, format1.format(i), null);
                        if (doc1.getLength() > 100)
                            doc1.remove(0, 12);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                System.out.println("t1 done");
            }
        };
        Thread t2 = new Thread("Thread doc2") {

            @Override
            public void run() {
                try {
                    barrier.await();
                    for (int i = 0; i < 1000; i++) {
                        doc2.insertString(0, format2.format(i), null);
                        if (doc2.getLength() > 100)
                            doc2.remove(0, 13);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                System.out.println("t2 done");
            }
        };
        Thread t3 = new Thread("Undo/Redo Thread") {

            @Override
            public void run() {
                try {
                    barrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 1000; i++) {
                    if (undoManager.canUndoOrRedo()) {
                        undoManager.undoOrRedo();
                    }
                    if (undoManager.canUndo()) {
                        undoManager.undo();
                    }
                }
                System.out.println("t3 done");
            }
        };
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
    }
}
