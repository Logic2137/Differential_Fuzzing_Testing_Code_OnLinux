
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.Robot;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;


public class TestCCEOnEditEvent {
    private Container contentPane = null;
    private JTextArea textArea = null;
    private static Robot robot;
    private static JFrame frame;
    public static void main(String[] arguments) throws Exception {
        try{
            robot = new Robot();
            robot.setAutoDelay(50);
            TestCCEOnEditEvent test = new TestCCEOnEditEvent();
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    test.createAndShowGUI();
                }
            });

            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.waitForIdle();
        } finally {
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run(){
                    frame.dispose();
                }
            });
        }
    }

    public void createAndShowGUI() {
        frame = new JFrame();
        contentPane  = frame.getContentPane();
        createTextArea();
        frame.setSize(200, 200);
        frame.setVisible(true);
    }

    private void createTextArea() {
        textArea = new JTextArea("Text Area") {
            @Override
            protected Document createDefaultModel() {
                return new PlainDocument() {
                    @Override
                    protected void fireUndoableEditUpdate(UndoableEditEvent event) {
                        Object[] listeners = listenerList.getListenerList();
                        for (int index = listeners.length - 2; index >= 0; index -= 2) {
                            Object listenerType = listeners[index];
                            if (listenerType == UndoableEditListener.class) {
                                super.fireUndoableEditUpdate(event);
                            }
                        }
                    }
                };
            }
        };
        textArea.getDocument().addUndoableEditListener(new UndoManager() {
            @Override
            public void undoableEditHappened(UndoableEditEvent event) {
                CompoundEdit edit = null;
                try {
                    edit = (CompoundEdit) event.getEdit();
                }
                catch(ClassCastException e ) {
                    throw new RuntimeException("Class Cast Exception is thrown on (CompoundEdit) UndoableEditEvent.getEdit()");
                }
                AbstractDocument.DefaultDocumentEvent documentEvent =
                        (AbstractDocument.DefaultDocumentEvent) edit;
                DocumentEvent.EventType editType = documentEvent.getType();
                int editOffset = documentEvent.getOffset();
                int editLength = documentEvent.getLength();

            }
        });
        try {
            textArea.setSelectionStart(textArea.getLineEndOffset(0));
        }
        catch (BadLocationException exception) {
        }
        JScrollPane scrollPane = new JScrollPane(textArea,
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        contentPane.add(scrollPane);
    }
}
