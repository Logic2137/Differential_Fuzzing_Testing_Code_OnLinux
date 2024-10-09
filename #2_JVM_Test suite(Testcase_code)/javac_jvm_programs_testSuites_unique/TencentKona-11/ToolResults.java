
package common;

import java.util.List;
import java.util.stream.Collectors;


public class ToolResults {

    public int getExitCode() {
        return exitCode;
    }

    public List<String> getStdout() {
        return stdout;
    }

    public List<String> getStderr() {
        return stderr;
    }

    public String getStdoutString() {
        return stdout.stream().collect(Collectors.joining(System.getProperty("line.separator")));
    }

    
    public String getStdoutLine(int ndx) {
        return stdout.get(ndx);
    }

    public ToolResults(int exitCode, List<String> stdin, List<String> stderr) {
        this.exitCode = exitCode;
        this.stdout = stdin;
        this.stderr = stderr;
    }

    public ToolResults(ToolResults rawResults) {
        this(rawResults.exitCode, rawResults.stdout, rawResults.stderr);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Exit code: ").append(exitCode).append("\n");
        sb.append("stdout:");
        stdout.stream().forEach((s) -> {
            sb.append(s).append("\n");
        });
        sb.append("stderr:");
        stderr.stream().forEach((s) -> {
            sb.append(s).append("\n");
        });
        return sb.toString();
    }

    private final int exitCode;
    private final List<String> stdout;
    private final List<String> stderr;

}
