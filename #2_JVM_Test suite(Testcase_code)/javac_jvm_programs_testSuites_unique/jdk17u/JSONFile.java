
package compiler.compilercontrol.share;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Objects;
import java.util.Stack;

public class JSONFile implements AutoCloseable {

    private final Stack<Element> stack;

    private final String fileName;

    private final PrintStream out;

    private int spaces;

    public enum Element {

        OBJECT, ARRAY, PAIR, VALUE
    }

    public JSONFile() {
        this("directives_file.json");
    }

    public JSONFile(String fileName) {
        this.spaces = 0;
        this.stack = new Stack<>();
        this.fileName = fileName;
        try {
            out = new PrintStream(fileName);
        } catch (FileNotFoundException e) {
            throw new Error("TESTBUG: can't open/create file " + fileName, e);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public Element getElement() {
        if (stack.empty()) {
            return null;
        }
        return stack.peek();
    }

    public JSONFile write(Element element, String... value) {
        if (value.length > 1) {
            throw new Error("TESTBUG: Unexpected value length: " + value.length);
        }
        if (!stack.empty()) {
            if (stack.peek() == Element.VALUE) {
                out.print(", ");
                stack.pop();
            }
        }
        switch(element) {
            case OBJECT:
                out.print("{");
                spaces++;
                stack.push(Element.VALUE);
                break;
            case ARRAY:
                out.print("[");
                stack.push(Element.VALUE);
                break;
            case PAIR:
                fillSpaces();
                Objects.requireNonNull(value, "TESTBUG: " + element + "requires a value to be set");
                out.print(value[0] + ": ");
                break;
            case VALUE:
                Objects.requireNonNull(value, "TESTBUG: " + element + "requires a value to be set");
                out.print(value[0]);
                break;
        }
        stack.push(element);
        return this;
    }

    private void fillSpaces() {
        out.println();
        for (int i = 0; i < spaces; i++) {
            out.print("  ");
        }
    }

    public JSONFile end() {
        if (!stack.empty()) {
            Element prev = stack.pop();
            while (prev != Element.OBJECT && prev != Element.ARRAY && !stack.empty()) {
                prev = stack.pop();
            }
            switch(prev) {
                case OBJECT:
                    spaces--;
                    fillSpaces();
                    out.print("}");
                    break;
                case ARRAY:
                    out.print("]");
                    break;
                default:
                    throw new Error("TESTBUG: Incorrect end. " + "Wrong type found: " + prev);
            }
        } else {
            throw new Error("TESTBUG: Incorrect end. Empty stack");
        }
        return this;
    }

    @Override
    public void close() {
        out.close();
    }
}
