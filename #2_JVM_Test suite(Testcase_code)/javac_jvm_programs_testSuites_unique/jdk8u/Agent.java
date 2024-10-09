import java.security.*;
import java.lang.instrument.*;
import java.lang.reflect.*;
import java.lang.management.ManagementFactory;
import com.sun.tools.attach.VirtualMachine;

class A {

    void m() {
    }
}

class B extends A {

    void m() {
    }
}

class C extends A {

    void m() {
    }
}

class Test {

    static public void m() throws Exception {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 20000; i++) {
            m1(a);
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 4; i++) {
            m1(b);
        }
    }

    static boolean m1(A a) {
        boolean res = Agent.m2(a);
        return res;
    }

    static public A a = new A();

    static public B b = new B();

    static public C c = new C();
}

public class Agent implements ClassFileTransformer {

    static class MemoryChunk {

        MemoryChunk other;

        long[] array;

        MemoryChunk(MemoryChunk other) {
            other = other;
            array = new long[1024 * 1024 * 1024];
        }
    }

    static public boolean m2(A a) {
        boolean res = false;
        if (a.getClass() == B.class) {
            a.m();
        } else {
            res = true;
        }
        return res;
    }

    static public void main(String[] args) throws Exception {
        Test.m();
        String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
        int p = nameOfRunningVM.indexOf('@');
        String pid = nameOfRunningVM.substring(0, p);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 10; i++) {
            System.gc();
        }
        try {
            VirtualMachine vm = VirtualMachine.attach(pid);
            vm.loadAgent(System.getProperty("test.classes", ".") + "/agent.jar", "");
            vm.detach();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Test.m();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 10; i++) {
            System.gc();
        }
    }

    public synchronized byte[] transform(final ClassLoader classLoader, final String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        System.out.println("Transforming class " + className);
        return classfileBuffer;
    }

    public static void redefine(String agentArgs, Instrumentation instrumentation, Class to_redefine) {
        try {
            instrumentation.retransformClasses(to_redefine);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void agentmain(String agentArgs, Instrumentation instrumentation) throws Exception {
        Agent transformer = new Agent();
        instrumentation.addTransformer(transformer, true);
        redefine(agentArgs, instrumentation, Test.class);
    }
}
