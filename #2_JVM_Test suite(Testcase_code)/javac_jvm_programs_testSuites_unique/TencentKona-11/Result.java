

package tests;

import java.nio.file.Path;

public class Result {
    private final int exitCode;
    private final String message;
    private final Path imageFile;

    public Result(int exitCode, String message, Path imageFile) {
        this.exitCode = exitCode;
        this.message = message;
        this.imageFile = imageFile;
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getMessage() {
        return message;
    }

    public Path getFile() {
        return imageFile;
    }

    public void assertFailure() {
        assertFailure(null);
    }

    public void assertFailure(String expected) {
        if (getExitCode() == 0) {
            System.err.println(getMessage());
            throw new AssertionError("Failure expected: " + getFile());
        }
        if (getExitCode() != 1 && getExitCode() != 2) {
            System.err.println(getMessage());
            throw new AssertionError("Abnormal exit: " + getFile());
        }
        if (expected != null) {
            if (expected.isEmpty()) {
                throw new AssertionError("Expected error is empty");
            }
            if (!getMessage().matches(expected) && !getMessage().contains(expected)) {
                System.err.println(getMessage());
                throw new AssertionError("Output does not fit regexp: " + expected);
            }
        }
        System.err.println("Failed as expected. " + (expected != null ? expected : ""));
        System.err.println(getMessage());
    }

    public Path assertSuccess() {
        if (getExitCode() != 0) {
            System.err.println(getMessage());
            throw new AssertionError("Unexpected failure: " + getExitCode());
        }
        return getFile();
    }
}
