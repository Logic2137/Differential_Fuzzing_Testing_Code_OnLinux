
package jdk.test.failurehandler.value;

public interface ValueParser {

    Object parse(Class<?> type, String value, String delimiter);
}
