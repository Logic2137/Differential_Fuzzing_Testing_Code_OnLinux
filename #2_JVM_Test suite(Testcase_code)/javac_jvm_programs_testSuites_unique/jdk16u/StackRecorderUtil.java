

import java.lang.StackWalker.Option;
import java.lang.StackWalker.StackFrame;
import java.util.*;


public class StackRecorderUtil implements Iterable<StackRecorderUtil.TestFrame> {
    private List<TestFrame> testFrames = new LinkedList();

    private boolean compareClasses;
    private boolean compareClassNames = true;
    private boolean compareMethodNames = true;
    private boolean compareSTEs;

    public StackRecorderUtil(Set<StackWalker.Option> swOptions) {
        compareClasses = swOptions.contains(Option.RETAIN_CLASS_REFERENCE);
        compareSTEs = true;
    }

    
    public void add(Class declaringClass, String methodName, String fileName) {
        testFrames.add(0, new TestFrame(declaringClass, methodName, fileName));
    }

    public int frameCount() { return testFrames.size(); }

    
    public void compareFrame(int index, StackFrame sf) {
        TestFrame tf = testFrames.get(index);
        if (compareClasses) {
            if (!tf.declaringClass.equals(sf.getDeclaringClass())) {
                throw new RuntimeException("Expected class: " +
                  tf.declaringClass.toString() + ", but got: " +
                  sf.getDeclaringClass().toString());
            }
        } else {
            boolean caught = false;
            try {
                sf.getDeclaringClass();
            } catch (UnsupportedOperationException e) {
                caught = true;
            }
            if (!caught) {
                throw new RuntimeException("StackWalker did not have " +
                  "RETAIN_CLASS_REFERENCE Option, but did not throw " +
                  "UnsupportedOperationException");
            }
        }

        if (compareClassNames && !tf.className().equals(sf.getClassName())) {
            throw new RuntimeException("Expected class name: " + tf.className() +
                    ", but got: " + sf.getClassName());
        }
        if (compareMethodNames && !tf.methodName.equals(sf.getMethodName())) {
            throw new RuntimeException("Expected method name: " + tf.methodName +
                    ", but got: " + sf.getMethodName());
        }
        if (compareSTEs) {
            StackTraceElement ste = sf.toStackTraceElement();
            if (!(ste.getClassName().equals(tf.className()) &&
                  ste.getMethodName().equals(tf.methodName)) &&
                  ste.getFileName().equals(tf.fileName)) {
                throw new RuntimeException("Expected StackTraceElement info: " +
                        tf + ", but got: " + ste);
            }
            if (!Objects.equals(ste.getClassName(), sf.getClassName())
                || !Objects.equals(ste.getMethodName(), sf.getMethodName())
                || !Objects.equals(ste.getFileName(), sf.getFileName())
                || !Objects.equals(ste.getLineNumber(), sf.getLineNumber())
                || !Objects.equals(ste.isNativeMethod(), sf.isNativeMethod())) {
                throw new RuntimeException("StackFrame and StackTraceElement differ: " +
                        "sf=" + sf + ", ste=" + ste);
            }
        }
    }

    public Iterator<TestFrame> iterator() {
        return testFrames.iterator();
    }

    
    public static class TestFrame {
        public Class declaringClass;
        public String methodName;
        public String fileName = null;

        public TestFrame (Class declaringClass, String methodName, String fileName) {
            this.declaringClass = declaringClass;
            this.methodName = methodName;
            this.fileName = fileName;
        }
        public String className() {
            return declaringClass.getName();
        }
        public String toString() {
            return "TestFrame: " + className() + "." + methodName +
                    (fileName == null ? "" : "(" + fileName + ")");
        }
    }
}
