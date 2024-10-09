

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;


public class LocaleCmdOverrides {

    

    public static void main(String[] args) {
        Map<String, String> props = commandLineDefines();
        System.out.printf("props: %s%n", props);
        test("user.language", props);
        test("user.country", props);
        test("user.script", props);
        test("user.variant", props);
    }

    
    static void test(String baseName, Map<String, String> args) {
        validateArg(baseName,"",  args);
        validateArg(baseName,".display", args);
        validateArg(baseName,".format", args);
    }

    
    static void validateArg(String name, String ext, Map<String, String> args) {
        String extName = name.concat(ext);
        String arg = args.get(extName);
        String prop = System.getProperty(extName);
        if (arg == null && prop == null) {
            System.out.printf("No values for %s%n", extName);
        } else {
            System.out.printf("validateArg %s: arg: %s, prop: %s%n", extName, arg, prop);
        }

        if (arg != null) {
            if (!Objects.equals(arg, prop)) {
                throw new RuntimeException(extName + ": -D value should match property: "
                        + arg + " != " + prop);
            }
        } else if (prop != null) {
            
            
            if (ext != null && !ext.isEmpty()) {
                String value = System.getProperty(name);
                if (Objects.equals(value, prop)) {
                    throw new RuntimeException(extName + " property should not be equals to "
                            + name + " property: " + prop);
                }
            }
        }
    }

    
    static HashMap<String, String> commandLineDefines() {
        HashMap<String, String> props = new HashMap<>();
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        List<String> args = runtime.getInputArguments();
        System.out.printf("args: %s%n", args);
        for (String arg : args) {
            if (arg.startsWith("-Duser.")) {
                String[] kv = arg.substring(2).split("=");
                switch (kv.length) {
                    case 1:
                        props.put(kv[0], "");
                        break;
                    case 2:
                        props.put(kv[0], kv[1]);
                        break;
                    default:
                        throw new IllegalArgumentException("Illegal property syntax: " + arg);
                }
            }
        }
        return props;
    }
}
