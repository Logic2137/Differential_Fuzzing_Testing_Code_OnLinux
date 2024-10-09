



import javax.swing.*;
import javax.swing.text.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;


public class bug8158734 {

    public static final String TYPE = "test/test";
    public static final String TYPE_2 = "test2/test2";

    static boolean myClassloaderWasUsed = false;

    static class MyEditorKit extends EditorKit {
        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public ViewFactory getViewFactory() {
            return null;
        }

        @Override
        public Action[] getActions() {
            return new Action[0];
        }

        @Override
        public Caret createCaret() {
            return null;
        }

        @Override
        public Document createDefaultDocument() {
            return null;
        }

        @Override
        public void read(InputStream in, Document doc, int pos) throws IOException, BadLocationException {
        }

        @Override
        public void write(OutputStream out, Document doc, int pos, int len) throws IOException, BadLocationException {

        }

        @Override
        public void read(Reader in, Document doc, int pos) throws IOException, BadLocationException {
        }

        @Override
        public void write(Writer out, Document doc, int pos, int len) throws IOException, BadLocationException {
        }
    }

    static class MyClassloader extends ClassLoader {
        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            myClassloaderWasUsed = true;
            return super.loadClass(name);
        }
    }

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JEditorPane c = new JEditorPane();
                c.setContentType(TYPE);

                final MyClassloader loader = new MyClassloader();
                JEditorPane.registerEditorKitForContentType(TYPE_2, MyEditorKit.class.getName(), loader);
                JEditorPane.registerEditorKitForContentType(TYPE_2, MyEditorKit.class.getName(), null);
                JEditorPane.createEditorKitForContentType(TYPE_2);

                if (myClassloaderWasUsed) {
                    throw new RuntimeException("Class loader has not been removed for '" + TYPE_2 + "' type");
                }
            }
        });

    }
}
