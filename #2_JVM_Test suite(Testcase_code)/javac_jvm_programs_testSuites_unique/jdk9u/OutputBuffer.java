

package jdk.test.lib.process;

public class OutputBuffer {
  private final String stdout;
  private final String stderr;

  
  public OutputBuffer(String stdout, String stderr) {
    this.stdout = stdout;
    this.stderr = stderr;
  }

  
  public String getStdout() {
    return stdout;
  }

  
  public String getStderr() {
    return stderr;
  }
}
