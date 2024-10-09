
package javax.tools;

public interface Tool {

    default String name() {
        return "upgraded Tool interface";
    }
}
