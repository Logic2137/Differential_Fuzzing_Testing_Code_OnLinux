
package lib.jdb;

import java.util.Arrays;
import java.util.stream.Collectors;

public class JdbCommand {

    final String cmd;

    boolean allowExit = false;

    public JdbCommand(String cmd) {
        this.cmd = cmd;
    }

    public JdbCommand allowExit() {
        allowExit = true;
        return this;
    }

    public static JdbCommand run(String... params) {
        return new JdbCommand("run " + Arrays.stream(params).collect(Collectors.joining(" ")));
    }

    public static JdbCommand cont() {
        return new JdbCommand("cont");
    }

    public static JdbCommand dump(String what) {
        return new JdbCommand("dump " + what);
    }

    public static JdbCommand quit() {
        return new JdbCommand("quit").allowExit();
    }

    public static JdbCommand stopAt(String targetClass, int lineNum) {
        return new JdbCommand("stop at " + targetClass + ":" + lineNum);
    }

    public static JdbCommand stopIn(String targetClass, String methodName) {
        return new JdbCommand("stop in " + targetClass + "." + methodName);
    }

    public static JdbCommand clear(String targetClass, int lineNum) {
        return new JdbCommand("clear " + targetClass + ":" + lineNum);
    }

    public enum ExType {

        uncaught, caught, all
    }

    public static JdbCommand catch_(String classId) {
        return new JdbCommand("catch " + classId);
    }

    public static JdbCommand catch_(ExType eType, String classId) {
        return catch_(eType.toString() + " " + classId);
    }

    public static JdbCommand ignore(String classId) {
        return new JdbCommand("ignore " + classId);
    }

    public static JdbCommand ignore(ExType eType, String classId) {
        return ignore(eType.toString() + " " + classId);
    }

    public static JdbCommand step() {
        return new JdbCommand("step");
    }

    public static JdbCommand stepUp() {
        return new JdbCommand("step up");
    }

    public static JdbCommand next() {
        return new JdbCommand("next");
    }

    public static JdbCommand where(String threadId) {
        return new JdbCommand("where " + threadId);
    }

    public static JdbCommand eval(String expr) {
        return new JdbCommand("eval " + expr);
    }

    public static JdbCommand print(String expr) {
        return new JdbCommand("print " + expr);
    }

    public static JdbCommand locals() {
        return new JdbCommand("locals");
    }

    public static JdbCommand set(String lvalue, String expr) {
        return new JdbCommand("set " + lvalue + " = " + expr);
    }

    public static JdbCommand lock(String expr) {
        return new JdbCommand("lock " + expr);
    }

    public static JdbCommand methods(String classId) {
        return new JdbCommand("methods " + classId);
    }

    public static JdbCommand trace(boolean go, String mode, Integer threadId) {
        return new JdbCommand(" trace" + (go ? " go" : "") + (mode != null ? " " + mode : "") + (threadId != null ? " " + threadId.toString() : ""));
    }

    public static JdbCommand trace() {
        return trace(false, null, null);
    }

    public static JdbCommand traceMethods(boolean go, Integer threadId) {
        return trace(go, "methods", threadId);
    }

    public static JdbCommand traceMethodExit(boolean go, Integer threadId) {
        return trace(go, "method exit", threadId);
    }

    public static JdbCommand traceMethodExits(boolean go, Integer threadId) {
        return trace(go, "method exits", threadId);
    }

    public static JdbCommand untrace() {
        return new JdbCommand("untrace");
    }

    public static JdbCommand watch(String classId, String fieldName) {
        return new JdbCommand("watch " + classId + "." + fieldName);
    }

    public static JdbCommand pop() {
        return new JdbCommand("pop");
    }

    public static JdbCommand redefine(String classId, String classFileName) {
        return new JdbCommand("redefine " + classId + " " + classFileName);
    }
}
