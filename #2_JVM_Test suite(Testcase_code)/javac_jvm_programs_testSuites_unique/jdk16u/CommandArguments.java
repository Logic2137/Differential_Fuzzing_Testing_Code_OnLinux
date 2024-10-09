
package jdk.jpackage.test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandArguments<T> {

    CommandArguments() {
        args = new ArrayList<>();
    }

    final public T addArgument(String v) {
        args.add(v);
        return (T) this;
    }

    final public T addArguments(List<String> v) {
        args.addAll(v);
        return (T) this;
    }

    final public T addArgument(Path v) {
        return addArgument(v.toString());
    }

    final public T addArguments(String... v) {
        return addArguments(Arrays.asList(v));
    }

    final public T addPathArguments(List<Path> v) {
        return addArguments(v.stream().map((p) -> p.toString()).collect(
                Collectors.toList()));
    }

    final public List<String> getAllArguments() {
        return List.copyOf(args);
    }

    protected void verifyMutable() {
        if (!isMutable()) {
            throw new UnsupportedOperationException(
                    "Attempt to modify immutable object");
        }
    }

    protected boolean isMutable() {
        return true;
    }

    protected List<String> args;
}
