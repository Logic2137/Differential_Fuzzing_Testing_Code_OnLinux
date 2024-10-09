import java.io.InputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.util.Timer;
import java.util.TimerTask;

public class RedefineClassWithNativeMethodAgent {

    static Class clz;

    public static void premain(String agentArgs, final Instrumentation inst) throws Exception {
        String s = agentArgs.substring(0, agentArgs.indexOf(".class"));
        clz = Class.forName(s.replace('/', '.'));
        InputStream in;
        Module m = clz.getModule();
        if (m != null) {
            in = m.getResourceAsStream(agentArgs);
        } else {
            ClassLoader loader = RedefineClassWithNativeMethodAgent.class.getClassLoader();
            in = loader.getResourceAsStream(agentArgs);
        }
        if (in == null) {
            throw new Exception("Cannot find class: " + agentArgs);
        }
        byte[] buffer = in.readAllBytes();
        new Timer(true).schedule(new TimerTask() {

            public void run() {
                try {
                    System.out.println("Instrumenting");
                    ClassDefinition cld = new ClassDefinition(clz, buffer);
                    inst.redefineClasses(new ClassDefinition[] { cld });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 500);
    }
}
