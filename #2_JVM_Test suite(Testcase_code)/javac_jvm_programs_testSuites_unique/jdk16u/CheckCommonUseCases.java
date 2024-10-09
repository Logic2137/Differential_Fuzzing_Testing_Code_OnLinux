

import java.awt.EventQueue;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public final class CheckCommonUseCases {

    public static void main(String[] args) throws Exception {
        EventQueue.invokeAndWait(() -> {
            JPasswordField pf = new JPasswordField();
            
            checkDifferentTextLength(pf);
            
            countListeners(pf);
        });
    }

    private static void countListeners(JPasswordField pf) {
        AtomicInteger insert = new AtomicInteger();
        AtomicInteger update = new AtomicInteger();
        AtomicInteger remove = new AtomicInteger();
        pf.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                insert.incrementAndGet();
                System.err.println("e = " + e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                remove.incrementAndGet();
                System.err.println("e = " + e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update.incrementAndGet();
                System.err.println("e = " + e);
            }
        });
        
        pf.setText("aaa");
        if (remove.get() != 0 || update.get() != 0 || insert.get() > 1) {
            System.err.println("remove = " + remove);
            System.err.println("update = " + update);
            System.err.println("insert = " + insert);
            throw new RuntimeException("Unexpected number of listeners");
        }
        insert.set(0);
        update.set(0);
        remove.set(0);

        
        pf.setText("bbb");
        if (remove.get() > 1 || update.get() > 1 || insert.get() > 1) {
            System.err.println("remove = " + remove);
            System.err.println("update = " + update);
            System.err.println("insert = " + insert);
            throw new RuntimeException("Unexpected number of listeners");
        }
        insert.set(0);
        update.set(0);
        remove.set(0);

        
        pf.setText("");
        if (remove.get() > 1 || update.get() > 0 || insert.get() > 0) {
            System.err.println("remove = " + remove);
            System.err.println("update = " + update);
            System.err.println("insert = " + insert);
            throw new RuntimeException("Unexpected number of listeners");
        }
    }

    private static void checkDifferentTextLength(JPasswordField pf) {
        
        for (int i = 0 ; i < 100; ++i){
            String expected = ("" + i).repeat(i);
            pf.setText(expected);
            String actual = Arrays.toString(pf.getPassword());
            if (actual.equals(expected)){
                System.err.println("Expected: " + expected);
                System.err.println("Actual: " + actual);
                throw new RuntimeException();
            }
        }
        
        for (int i = 99; i >= 0; --i){
            String expected = ("" + i).repeat(i);
            pf.setText(expected);
            String actual = Arrays.toString(pf.getPassword());
            if (actual.equals(expected)){
                System.err.println("Expected: " + expected);
                System.err.println("Actual: " + actual);
                throw new RuntimeException();
            }
        }
    }
}
