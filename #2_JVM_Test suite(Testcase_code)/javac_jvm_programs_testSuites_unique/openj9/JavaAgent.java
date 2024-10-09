package org.openj9.test.utilities;



import java.lang.instrument.Instrumentation;
import java.lang.instrument.ClassDefinition;

public class JavaAgent {
    private static Instrumentation instrumentation = null;
	private static String agentArguments = null;

    public static void premain(String agentArgs, Instrumentation inst) {
		instrumentation = inst;
		agentArguments = agentArgs;
    }

    public static void redefineClass(Class<?> theClass, byte[] classBytes) throws Throwable {
		ClassDefinition cdf = new ClassDefinition( theClass, classBytes );
		instrumentation.redefineClasses(cdf);
	}
}
